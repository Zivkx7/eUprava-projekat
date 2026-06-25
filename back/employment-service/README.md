# Mikroservis „Služba za zapošljavanje"

Deo projekta iz predmeta **Tehnologije i sistemi eUprave**. Ovaj mikroservis pokriva
poslove zapošljavanja (poslodavci, ponude, kandidati, prijave, intervjui, sajam zapošljavanja)
i komunicira sa mikroservisom **Fakultet** radi verifikacije obrazovanja i preuzimanja
zvaničnog proseka (GPA) za rangiranje kandidata.

## Tehnologije
- Spring Boot 3.3.5 (Java 17), slojevita arhitektura: Controller → Service → Repository → Model
- MySQL 8 (baza `zaposljavanje_db`)
- JWT autentifikacija (jjwt 0.11.5), BCrypt, role: `ADMIN`, `EMPLOYER`, `CANDIDATE`
- ZXing (generisanje QR koda za prijavu na sajam)
- Angular frontend: `../../frontend-zaposljavanje` (zaseban app)

## Portovi i baza (bez kolizije sa Fakultetom)
| | Fakultet | Služba za zapošljavanje |
|---|---|---|
| Backend port | 8081 | **8082** |
| MySQL port | 3307 | **3308** |
| Baza | `fakultet_db` | `zaposljavanje_db` |
| Frontend | 4200 | **4201** |

## Pokretanje

```bash
# 1) Baza (MySQL na portu 3308)
cd back/employment-service
docker compose up -d

# 2) Backend (port 8082)
./mvnw spring-boot:run        # ili: ./mvnw clean package && java -jar target/employment-service-0.0.1-SNAPSHOT.jar

# 3) Frontend (port 4201)
cd ../../frontend-zaposljavanje
npm install
npm start                     # ng serve --port 4201
```

Podrazumevani admin nalog (kreira se automatski): **admin@zaposljavanje.rs / admin**.

> Za zavisne funkcije (verifikacija obrazovanja, rangiranje po GPA) potrebno je da je
> mikroservis **Fakultet** pokrenut na `http://localhost:8081` (podesivo preko
> `faculty.service.url` u `application.properties`).

## Entiteti
`Employer`, `JobOffer`, `Candidate`, `EducationRecord`, `Application`, `Interview`,
`FairCheckIn`, `User` (+ `Role`). `RecommendationEngine` je komponenta za rangiranje kandidata.

## REST API (prefiks `/api`)
| Resurs | Putanja | Napomena |
|---|---|---|
| Auth | `POST /api/auth/login`, `POST /api/auth/register` | JWT |
| Poslodavci | `/api/employers` | CRUD, `GET /by-email` |
| Ponude | `/api/job-offers` | CRUD, `GET /search?q=`, `GET /employer/{id}` |
| Kandidati | `/api/candidates` | CRUD, `GET /search?name=`, `GET /{id}/cv` |
| Obrazovanje | `/api/education-records` | CRUD, `POST /{id}/verify` (Fakultet) |
| Prijave | `/api/applications` | `POST` (apply), `DELETE /{id}` (withdraw), `PUT /{id}/status`, filteri |
| Intervjui | `/api/interviews` | CRUD, `POST /{id}/invite` (sendInvite) |
| Sajam | `/api/fair-checkins` | `POST` (QR check-in), CRUD |
| Rang lista | `/api/recommendations?offerId=` | rangiranje po zvaničnom GPA |
| | `/api/recommendations/suggest?offerId=` | predlog kandidata |
| | `/api/recommendations/faculty-employees` | lista zaposlenih sa Fakulteta |

## Integracija sa Fakultetom (`/internal`)
Sistem radi sa **jednim fakultetom** (FTN, Novi Sad). Verifikacija studenta ide preko
**broja indeksa i studentskog mejla** (ne preko UUID-a), uz zajednički tajni ključ
`X-Internal-Key` (`faculty.internal.key`, mora biti isti kod oba servisa).

`FacultyClient` poziva:
- `POST /internal/students/verify` — telo `{ indexNo, email }`; vraća zvanične podatke
  studenta (`verified, studentId, name, programName, degree, graduated, status, officialGPA`)
  ako se indeks i mejl poklope. Koristi se za verifikaciju obrazovanja i rangiranje.
- `GET /internal/employees` — lista zaposlenih (verifikacija radnih mesta).

**Tok:** kandidat u obrazovni zapis unese samo **broj indeksa i studentski mejl**. Pri
verifikaciji (ručno ili automatski pri prijavi na ponudu) Služba poziva Fakultet, koji
vraća zvanične podatke — oni se upisuju u zapis (`verified = true`, program/nivo/prosek).
`RecommendationEngine` zatim koristi zvanični GPA za rang listu; kandidati bez dostupnog
GPA idu na dno liste.
