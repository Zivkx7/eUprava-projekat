import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course';
import { ExamService } from '../../services/exam';
import { ExamRegistrationService } from '../../services/exam-registration';
import { StudentService } from '../../services/student';
import { EmployeeService } from '../../services/employee';
import { AuthService } from '../../services/auth';
import { NotificationService } from '../../services/notification';

@Component({
  selector: 'app-exam',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './exam.html',
  styleUrls: ['./exam.css']
})
export class Exam implements OnInit {
  exams: any[] = [];
  courses: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { courseId: '', dateTime: '', room: '' };
  gradeForm = { registrationId: '', grade: 0 };
  showGradeForm = false;

  // prijave studenta
  myRegistrations: any[] = [];

  // prikaz prijavljenih (profesor/admin)
  showRegistrations = false;
  selectedExam: any = null;
  examRegistrations: any[] = [];

  constructor(
    private examService: ExamService,
    private courseService: CourseService,
    private registrationService: ExamRegistrationService,
    private studentService: StudentService,
    private employeeService: EmployeeService,
    public authService: AuthService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.load();
    this.loadCourses();
    if (this.authService.isStudent()) {
      this.loadMyRegistrations();
    }
  }

  loadCourses() {
    if (this.authService.isProfessor()) {
      const employeeId = localStorage.getItem('employeeId');
      if (employeeId) {
        this.employeeService.getCourses(employeeId).subscribe(d => this.courses = d);
      }
    } else {
      this.courseService.getAll().subscribe(d => this.courses = d);
    }
  }

 load() {
  if (this.authService.isStudent()) {
    const studentId = localStorage.getItem('studentId');
    if (studentId) {
      this.studentService.getStudentCourses(studentId).subscribe(courses => {
        const courseIds = courses.map((c: any) => c.id);
        // uzmi sve prijave studenta da vidimo koje je polozio
        this.registrationService.getByStudent(studentId).subscribe(regs => {
          const passedCourseNames = regs
            .filter((r: any) => r.status === 'PASSED')
            .map((r: any) => r.courseName);
          this.examService.getAll().subscribe(allExams => {
            this.exams = allExams.filter((e: any) => {
              const courseName = this.getCourseName(e.courseId);
              // prikazi ispit samo ako je predmet upisan I nije polozen
              return courseIds.includes(e.courseId) && !passedCourseNames.includes(courseName);
            });
          });
        });
      });
    }
  } else if (this.authService.isProfessor()) {
    const employeeId = localStorage.getItem('employeeId');
    if (employeeId) {
      this.employeeService.getCourses(employeeId).subscribe(courses => {
        const courseIds = courses.map((c: any) => c.id);
        this.examService.getAll().subscribe(allExams => {
          this.exams = allExams.filter((e: any) => courseIds.includes(e.courseId));
        });
      });
    }
  } else {
    this.examService.getAll().subscribe(data => this.exams = data);
  }
}

  loadMyRegistrations() {
    const studentId = localStorage.getItem('studentId');
    if (studentId) {
      this.registrationService.getByStudent(studentId).subscribe(d => this.myRegistrations = d);
    }
  }

  isRegistered(examId: string): boolean {
    return this.myRegistrations.some(r => r.examId === examId);
  }

  registerForExam(examId: string) {
    const studentId = localStorage.getItem('studentId');
    if (!studentId) return;
    this.registrationService.register({ studentId, examId }).subscribe({
      next: () => {
        this.loadMyRegistrations();
        this.notif.success('Uspešno ste prijavili ispit!');
      },
      error: () => this.notif.error('Već ste prijavljeni na ovaj ispit!')
    });
  }

  cancelRegistration(examId: string) {
    const reg = this.myRegistrations.find(r => r.examId === examId);
    if (reg) {
      this.registrationService.cancel(reg.id).subscribe(() => {
        this.loadMyRegistrations();
        this.notif.success('Prijava otkazana!');
      });
    }
  }

  viewRegistrations(exam: any) {
    this.selectedExam = exam;
    this.showRegistrations = true;
    this.registrationService.getByExam(exam.id).subscribe(d => this.examRegistrations = d);
  }

  closeRegistrations() {
    this.showRegistrations = false;
    this.selectedExam = null;
    this.examRegistrations = [];
  }

  openForm(exam?: any) {
    this.showForm = true;
    if (exam) {
      this.editingId = exam.id;
      this.form = { courseId: exam.courseId, dateTime: exam.dateTime, room: exam.room };
    } else {
      this.editingId = null;
      this.form = { courseId: '', dateTime: '', room: '' };
    }
  }

  save() {
    if (!this.form.courseId || !this.form.dateTime || !this.form.room) {
      this.notif.error('Sva polja su obavezna!');
      return;
    }
    if (this.editingId) {
      this.examService.update(this.editingId, this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Ispit uspešno izmenjen!');
      });
    } else {
      this.examService.create(this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Ispit uspešno zakazan!');
      });
    }
  }

openGradeForm(registration: any) {
  this.gradeForm = { registrationId: registration.id, grade: 0 };
  this.showGradeForm = true;
}

saveGrade() {
  if (!this.gradeForm.grade || this.gradeForm.grade < 5 || this.gradeForm.grade > 10) {
    this.notif.error('Ocena mora biti između 5 i 10!');
    return;
  }
  this.registrationService.grade(this.gradeForm.registrationId, this.gradeForm.grade).subscribe({
    next: () => {
      this.viewRegistrations(this.selectedExam);
      this.showGradeForm = false;
      this.notif.success('Ocena uspešno uneta!');
    },
    error: () => {
      this.notif.error('Ocena se ne može uneti pre održavanja ispita!');
    }
  });
}

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.examService.delete(id).subscribe(() => {
        this.load();
        this.notif.success('Ispit uspešno obrisan!');
      });
    }
  }

  getCourseName(courseId: string): string {
    return this.courses.find(c => c.id === courseId)?.name || '';
  }
}