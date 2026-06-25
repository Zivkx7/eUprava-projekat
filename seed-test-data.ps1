<#
  seed-test-data.ps1
  -----------------
  Ubacuje dummy podatke u oba mikroservisa (Fakultet 8081 i Sluzba za zaposljavanje 8082)
  preko REST API-ja i testira njihovu komunikaciju (verifikacija studenta + rangiranje po GPA).

  Skripta je IDEMPOTENTNA - moze se pokrenuti vise puta bez kreiranja duplikata.

  Preduslov: oba backend-a su pokrenuta (8081 i 8082) i oba MySQL kontejnera rade.
  Pokretanje:  powershell -ExecutionPolicy Bypass -File .\seed-test-data.ps1
#>

$ErrorActionPreference = "Stop"
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$F = "http://localhost:8081"   # Fakultet
$E = "http://localhost:8082"   # Sluzba za zaposljavanje

# ---------- helpers ----------
function Invoke-Json {
    param([string]$Method, [string]$Url, $Body = $null, [string]$Token = $null)
    $headers = @{}
    if ($Token) { $headers["Authorization"] = "Bearer $Token" }
    $params = @{ Method = $Method; Uri = $Url; Headers = $headers; ContentType = 'application/json; charset=utf-8' }
    if ($null -ne $Body) {
        $json = ($Body | ConvertTo-Json -Depth 6 -Compress)
        $params.Body = [System.Text.Encoding]::UTF8.GetBytes($json)
    }
    return Invoke-RestMethod @params
}

function Get-Token {
    param([string]$BaseUrl, [string]$Username, [string]$Password, [string]$Role)
    try {
        return (Invoke-Json POST "$BaseUrl/api/auth/login" @{ username = $Username; password = $Password }).token
    } catch {
        return (Invoke-Json POST "$BaseUrl/api/auth/register" @{ username = $Username; password = $Password; role = $Role }).token
    }
}

function Step($msg) { Write-Host "  -> $msg" -ForegroundColor DarkGray }
function Section($msg) { Write-Host "`n=== $msg ===" -ForegroundColor Cyan }
function Ok($msg) { Write-Host "  [OK] $msg" -ForegroundColor Green }

# =====================================================================
Section "FAKULTET (8081) - autentifikacija"
$facToken = Get-Token $F "admin@ftn.rs" "admin" "ADMIN"
Ok "admin@ftn.rs ulogovan"

Section "FAKULTET - studijski program"
$progName = "Softversko inzenjerstvo i informacione tehnologije"
$program = (Invoke-Json GET "$F/api/programs" $null $facToken) | Where-Object { $_.name -eq $progName } | Select-Object -First 1
if (-not $program) { $program = Invoke-Json POST "$F/api/programs" @{ name = $progName; degree = "BACHELOR" } $facToken }
Ok "Program: $($program.name) [$($program.id)]"

Section "FAKULTET - predmeti"
$courseDefs = @(
    @{ code = "SIIT-OOP"; name = "Objektno orijentisano programiranje"; ects = 8 },
    @{ code = "SIIT-BP";  name = "Baze podataka";                        ects = 7 },
    @{ code = "SIIT-WEB"; name = "Web programiranje";                    ects = 6 },
    @{ code = "SIIT-AI";  name = "Vestacka inteligencija";               ects = 6 }
)
$allCourses = Invoke-Json GET "$F/api/courses" $null $facToken
$courses = @()
foreach ($cd in $courseDefs) {
    $c = $allCourses | Where-Object { $_.code -eq $cd.code } | Select-Object -First 1
    if (-not $c) {
        $c = Invoke-Json POST "$F/api/courses" @{ programId = $program.id; code = $cd.code; name = $cd.name; ects = $cd.ects } $facToken
    }
    $courses += $c
    Step "$($c.code) - $($c.name)"
}

Section "FAKULTET - studenti + ocene (GPA)"
# indexNo i email moraju da se poklope sa kandidatima u Sluzbi (za verifikaciju)
$studentDefs = @(
    @{ indexNo = "RA 1/2021";  name = "Marko Markovic";   email = "marko.markovic@uns.ac.rs";   status = "ACTIVE";    grades = @(10,10,9,9) },
    @{ indexNo = "RA 15/2021"; name = "Jovana Jovanovic"; email = "jovana.jovanovic@uns.ac.rs"; status = "ACTIVE";    grades = @(8,7,9,8)  },
    @{ indexNo = "RA 42/2020"; name = "Nikola Nikolic";   email = "nikola.nikolic@uns.ac.rs";   status = "GRADUATED"; grades = @(7,6,8,7)  }
)
$allStudents = Invoke-Json GET "$F/api/students" $null $facToken
foreach ($sd in $studentDefs) {
    $s = $allStudents | Where-Object { $_.indexNo -eq $sd.indexNo } | Select-Object -First 1
    if (-not $s) {
        $s = Invoke-Json POST "$F/api/students" @{
            indexNo = $sd.indexNo; name = $sd.name; email = $sd.email
            status = $sd.status; password = "student"; programId = $program.id
        } $facToken
    }
    # upis na predmete + ocene
    $existing = Invoke-Json GET "$F/api/enrollments/student/$($s.id)" $null $facToken
    for ($i = 0; $i -lt $courses.Count; $i++) {
        $course = $courses[$i]
        $grade = $sd.grades[$i]
        $en = $existing | Where-Object { $_.courseId -eq $course.id } | Select-Object -First 1
        if (-not $en) {
            $en = Invoke-Json POST "$F/api/enrollments" @{ studentId = $s.id; courseId = $course.id } $facToken
        }
        Invoke-Json PUT "$F/api/enrollments/$($en.id)/grade?grade=$grade" $null $facToken | Out-Null
    }
    $gpa = Invoke-Json GET "$F/api/students/$($s.id)/gpa" $null $facToken
    Ok "$($sd.name) ($($sd.indexNo)) - status $($sd.status), GPA $gpa"
}

Section "FAKULTET - zaposleni (profesori)"
$employeeDefs = @(
    @{ fullName = "Prof. dr Dragan Ivanovic"; role = "Redovni profesor"; email = "dragan.ivanovic@ftn.rs" },
    @{ fullName = "Doc. dr Ana Anic";         role = "Docent";           email = "ana.anic@ftn.rs" }
)
$allEmployees = Invoke-Json GET "$F/api/employees" $null $facToken
foreach ($ed in $employeeDefs) {
    $emp = $allEmployees | Where-Object { $_.email -eq $ed.email } | Select-Object -First 1
    if (-not $emp) {
        $emp = Invoke-Json POST "$F/api/employees" @{ fullName = $ed.fullName; role = $ed.role; email = $ed.email; password = "profesor" } $facToken
    }
    Step "$($emp.fullName) - $($emp.role)"
}

# =====================================================================
Section "SLUZBA (8082) - autentifikacija"
$empToken = Get-Token $E "admin@zaposljavanje.rs" "admin" "ADMIN"
Ok "admin@zaposljavanje.rs ulogovan"

Section "SLUZBA - poslodavac"
$employerEmail = "hr@vegait.rs"
try { $employer = Invoke-Json GET "$E/api/employers/by-email?email=$employerEmail" $null $empToken } catch { $employer = $null }
if (-not $employer) {
    $employer = Invoke-Json POST "$E/api/employers" @{ name = "Vega IT"; sector = "IT"; email = $employerEmail; password = "employer" } $empToken
}
Ok "Poslodavac: $($employer.name) [$($employer.id)]"

Section "SLUZBA - oglasi za posao"
$offerDefs = @(
    @{ title = "Junior Java Developer";      description = "Backend pozicija, Spring Boot, MySQL"; location = "Novi Sad" },
    @{ title = "Frontend Angular Developer"; description = "Angular + TypeScript SPA";             location = "Beograd" }
)
$existingOffers = Invoke-Json GET "$E/api/job-offers/employer/$($employer.id)" $null $empToken
$offers = @()
foreach ($od in $offerDefs) {
    $o = $existingOffers | Where-Object { $_.title -eq $od.title } | Select-Object -First 1
    if (-not $o) {
        $o = Invoke-Json POST "$E/api/job-offers" @{ title = $od.title; description = $od.description; location = $od.location; employerId = $employer.id } $empToken
    }
    $offers += $o
    Step "$($o.title) - $($o.location)"
}
$mainOffer = $offers[0]

Section "SLUZBA - kandidati + obrazovni zapisi"
# Prva tri kandidata se poklapaju sa studentima Fakulteta (verifikacija USPEVA).
# Cetvrti ima pogresan indeks i mejl (verifikacija PADA - negativan test).
$candidateDefs = @(
    @{ fullName = "Marko Markovic";   email = "marko.markovic@uns.ac.rs";   indexNo = "RA 1/2021";  studentEmail = "marko.markovic@uns.ac.rs"   },
    @{ fullName = "Jovana Jovanovic"; email = "jovana.jovanovic@uns.ac.rs"; indexNo = "RA 15/2021"; studentEmail = "jovana.jovanovic@uns.ac.rs" },
    @{ fullName = "Nikola Nikolic";   email = "nikola.nikolic@uns.ac.rs";   indexNo = "RA 42/2020"; studentEmail = "nikola.nikolic@uns.ac.rs"   },
    @{ fullName = "Petar Petrovic";   email = "petar.petrovic@gmail.com";   indexNo = "RA 99/2099"; studentEmail = "nepostojeci@uns.ac.rs"      }
)
$candidates = @()
foreach ($cd in $candidateDefs) {
    try { $cand = Invoke-Json GET "$E/api/candidates/by-email?email=$($cd.email)" $null $empToken } catch { $cand = $null }
    if (-not $cand) {
        $cand = Invoke-Json POST "$E/api/candidates" @{ fullName = $cd.fullName; email = $cd.email; password = "candidate" } $empToken
    }
    # obrazovni zapis (samo indeks + studentski mejl; ostalo popunjava Fakultet pri verifikaciji)
    $recs = Invoke-Json GET "$E/api/education-records/candidate/$($cand.id)" $null $empToken
    $rec = $recs | Where-Object { $_.indexNo -eq $cd.indexNo } | Select-Object -First 1
    if (-not $rec) {
        $rec = Invoke-Json POST "$E/api/education-records" @{
            candidateId = $cand.id; indexNo = $cd.indexNo; studentEmail = $cd.studentEmail
            startDate = "2020-10-01"; endDate = "2024-09-30"
        } $empToken
    }
    $candidates += [pscustomobject]@{ cand = $cand; recId = $rec.id; def = $cd }
    Step "$($cd.fullName) - indeks $($cd.indexNo)"
}

Section "KOMUNIKACIJA - verifikacija obrazovnih zapisa kod Fakulteta (8082 -> 8081)"
foreach ($c in $candidates) {
    $v = Invoke-Json POST "$E/api/education-records/$($c.recId)/verify" $null $empToken
    if ($v.verified) {
        Write-Host ("  [VERIFIKOVAN] {0,-18} program={1}, prosek={2}, diplomirao={3}" -f $c.def.fullName, $v.programName, $v.avgGradeSnapshot, $v.graduated) -ForegroundColor Green
    } else {
        Write-Host ("  [ODBIJEN]     {0,-18} (indeks ili mejl se ne poklapaju sa Fakultetom)" -f $c.def.fullName) -ForegroundColor Yellow
    }
}

Section "SLUZBA - prijave na oglas '$($mainOffer.title)'"
# Prijava automatski ponovo verifikuje obrazovne zapise kandidata sa Fakultetom.
foreach ($c in $candidates) {
    $apps = Invoke-Json GET "$E/api/applications/candidate/$($c.cand.id)" $null $empToken
    $app = $apps | Where-Object { $_.jobOfferId -eq $mainOffer.id } | Select-Object -First 1
    if (-not $app) {
        $app = Invoke-Json POST "$E/api/applications" @{ candidateId = $c.cand.id; jobOfferId = $mainOffer.id } $empToken
    }
    $c | Add-Member -NotePropertyName appId -NotePropertyValue $app.id -Force
    Step "$($c.def.fullName) prijavljen ($($app.status))"
}

Section "SLUZBA - intervju + pozivnica + sajam (za prvog kandidata)"
$first = $candidates[0]
Invoke-Json PUT "$E/api/applications/$($first.appId)/status?status=INTERVIEW" $null $empToken | Out-Null
Step "Status prijave -> INTERVIEW"
$interviews = Invoke-Json GET "$E/api/interviews/application/$($first.appId)" $null $empToken
$interview = $interviews | Select-Object -First 1
if (-not $interview) {
    $interview = Invoke-Json POST "$E/api/interviews" @{
        applicationId = $first.appId; dateTime = "2026-07-15T10:00:00"; location = "Vega IT, Novi Sad"; type = "in-person"
    } $empToken
}
Step "Intervju zakazan: $($interview.dateTime) @ $($interview.location)"
$invite = Invoke-Json POST "$E/api/interviews/$($interview.id)/invite" $null $empToken
Step "Pozivnica poslata"
$checkins = Invoke-Json GET "$E/api/fair-checkins/candidate/$($first.cand.id)" $null $empToken
if (-not ($checkins | Select-Object -First 1)) {
    Invoke-Json POST "$E/api/fair-checkins" @{ candidateId = $first.cand.id } $empToken | Out-Null
}
Step "Prijava na sajam zaposljavanja (QR) - OK"

# =====================================================================
Section "DOKAZ KOMUNIKACIJE - rang lista kandidata po zvanicnom GPA (live poziv Fakultetu)"
$ranking = Invoke-Json GET "$E/api/recommendations?offerId=$($mainOffer.id)" $null $empToken
$ranking | ForEach-Object {
    $gpa = if ($null -ne $_.officialGPA) { $_.officialGPA } else { "n/a" }
    Write-Host ("  #{0}  {1,-18} GPA={2}" -f $_.rank, $_.fullName, $gpa) -ForegroundColor White
}

Section "DOKAZ KOMUNIKACIJE - lista zaposlenih sa Fakulteta (8082 -> 8081)"
$facEmployees = Invoke-Json GET "$E/api/recommendations/faculty-employees" $null $empToken
$facEmployees | ForEach-Object { Step "$($_.fullName) - $($_.role)" }

Write-Host "`n==================================================" -ForegroundColor Cyan
Write-Host " GOTOVO - dummy podaci ubaceni, komunikacija provarena." -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host @"

Nalozi za prijavu:
  Fakultet (4200):    admin@ftn.rs / admin
  Sluzba (4201):      admin@zaposljavanje.rs / admin
  Poslodavac:         hr@vegait.rs / employer
  Kandidat:           marko.markovic@uns.ac.rs / candidate
  Student (Fakultet): marko.markovic@uns.ac.rs / student
"@ -ForegroundColor Gray
