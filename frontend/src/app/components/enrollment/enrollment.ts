import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EnrollmentService } from '../../services/enrollment';
import { StudentService } from '../../services/student';
import { CourseService } from '../../services/course';
import { AuthService } from '../../services/auth';
import { NotificationService } from '../../services/notification';

@Component({
  selector: 'app-enrollment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './enrollment.html',
  styleUrls: ['./enrollment.css']
})
export class Enrollment implements OnInit {
  enrollments: any[] = [];
  students: any[] = [];
  courses: any[] = [];
  filteredCourses: any[] = [];
  selectedStudentId = '';
  showForm = false;
  gradeForm = { enrollmentId: '', grade: 0 };
  showGradeForm = false;
  form = { studentId: '', courseId: '' };

  constructor(
    private enrollmentService: EnrollmentService,
    private studentService: StudentService,
    private courseService: CourseService,
    public authService: AuthService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.studentService.getAll().subscribe(d => this.students = d);
    this.courseService.getAll().subscribe(d => this.courses = d);
  }

  loadByStudent() {
    if (this.selectedStudentId) {
      this.enrollmentService.getByStudent(this.selectedStudentId).subscribe(d => this.enrollments = d);
    }
  }

  onStudentChange() {
    this.form.courseId = '';
    if (this.form.studentId) {
      const student = this.students.find(s => s.id === this.form.studentId);
      if (student) {
        this.filteredCourses = this.courses.filter(c => c.programId === student.programId);
      }
    } else {
      this.filteredCourses = [];
    }
  }

  enroll() {
    if (!this.form.studentId || !this.form.courseId) {
      this.notif.error('Odaberite studenta i predmet!');
      return;
    }
    this.enrollmentService.enroll(this.form).subscribe({
      next: () => {
        this.loadByStudent();
        this.showForm = false;
        this.notif.success('Student uspešno upisan na predmet!');
      },
      error: (err) => {
        this.notif.error('Greška: predmet nije u programu studenta!');
      }
    });
  }

  openGradeForm(enrollment: any) {
    this.gradeForm = { enrollmentId: enrollment.id, grade: enrollment.grade || 0 };
    this.showGradeForm = true;
  }

  saveGrade() {
    if (!this.gradeForm.grade || this.gradeForm.grade < 5 || this.gradeForm.grade > 10) {
      this.notif.error('Ocena mora biti između 5 i 10!');
      return;
    }
    this.enrollmentService.updateGrade(this.gradeForm.enrollmentId, this.gradeForm.grade).subscribe(() => {
      this.loadByStudent();
      this.showGradeForm = false;
      this.notif.success('Ocena uspešno unesena!');
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.enrollmentService.delete(id).subscribe(() => {
        this.loadByStudent();
        this.notif.success('Upis uspešno obrisan!');
      });
    }
  }

  getCourseName(courseId: string): string {
    return this.courses.find(c => c.id === courseId)?.name || '';
  }
  openEnrollForm() {
  this.showForm = true;
  if (this.selectedStudentId) {
    this.form.studentId = this.selectedStudentId;
    this.onStudentChange();
  }
}
}