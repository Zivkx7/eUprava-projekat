import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course';
import { ExamService } from '../../services/exam';


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

  constructor(private examService: ExamService, private courseService: CourseService) {}

  ngOnInit() {
    this.load();
    this.courseService.getAll().subscribe(d => this.courses = d);
  }

  load() {
    this.examService.getAll().subscribe(data => this.exams = data);
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
    if (this.editingId) {
      this.examService.update(this.editingId, this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    } else {
      this.examService.create(this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.examService.delete(id).subscribe(() => this.load());
    }
  }

  getCourseName(courseId: string): string {
    return this.courses.find(c => c.id === courseId)?.name || '';
  }
}