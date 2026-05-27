import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course';
import { EnrollmentService } from '../../services/enrollment';
import { StudentService } from '../../services/student';


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
  selectedStudentId = '';
  showForm = false;
  gradeForm = { enrollmentId: '', grade: 0 };
  showGradeForm = false;
  form = { studentId: '', courseId: '' };

  constructor(
    private enrollmentService: EnrollmentService,
    private studentService: StudentService,
    private courseService: CourseService
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

  enroll() {
    this.enrollmentService.enroll(this.form).subscribe(() => {
      this.loadByStudent(); this.showForm = false;
    });
  }

  openGradeForm(enrollment: any) {
    this.gradeForm = { enrollmentId: enrollment.id, grade: enrollment.grade || 0 };
    this.showGradeForm = true;
  }

  saveGrade() {
    this.enrollmentService.updateGrade(this.gradeForm.enrollmentId, this.gradeForm.grade).subscribe(() => {
      this.loadByStudent(); this.showGradeForm = false;
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.enrollmentService.delete(id).subscribe(() => this.loadByStudent());
    }
  }

  getCourseName(courseId: string): string {
    return this.courses.find(c => c.id === courseId)?.name || '';
  }
}