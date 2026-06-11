import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course';
import { ProgramService } from '../../services/program';
import { AuthService } from '../../services/auth';
import { EnrollmentService } from '../../services/enrollment';
import { NotificationService } from '../../services/notification';
import { StudentService } from '../../services/student';

@Component({
  selector: 'app-course',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './course.html',
  styleUrls: ['./course.css']
})
export class Course implements OnInit {
  courses: any[] = [];
  programs: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { code: '', name: '', ects: 6, programId: '' };

  constructor(
    private courseService: CourseService,
    private programService: ProgramService,
    public authService: AuthService,
    private enrollmentService: EnrollmentService,
    private studentService: StudentService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.load();
    this.programService.getAll().subscribe(d => this.programs = d);
  }

load() {
  if (this.authService.isStudent()) {
    const studentId = localStorage.getItem('studentId');
    if (studentId) {
      this.studentService.getStudentCourses(studentId).subscribe(data => this.courses = data);
    }
  } else {
    this.courseService.getAll().subscribe(data => this.courses = data);
  }
}
  openForm(course?: any) {
    this.showForm = true;
    if (course) {
      this.editingId = course.id;
      this.form = { code: course.code, name: course.name, ects: course.ects, programId: course.programId };
    } else {
      this.editingId = null;
      this.form = { code: '', name: '', ects: 6, programId: '' };
    }
  }

  save() {
    if (!this.form.code || !this.form.name || !this.form.programId) {
      this.notif.error('Sva polja su obavezna!');
      return;
    }
    if (this.form.ects <= 0) {
      this.notif.error('ECTS mora biti veći od 0!');
      return;
    }
    if (this.editingId) {
      this.courseService.update(this.editingId, this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Predmet uspešno izmenjen!');
      });
    } else {
      this.courseService.create(this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Predmet uspešno dodan!');
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.courseService.delete(id).subscribe(() => {
        this.load();
        this.notif.success('Predmet uspešno obrisan!');
      });
    }
  }

  getProgramName(programId: string): string {
    return this.programs.find(p => p.id === programId)?.name || '';
  }
}