import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course';
import { ProgramService } from '../../services/program';

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

  constructor(private courseService: CourseService, private programService: ProgramService) {}

  ngOnInit() { this.load(); this.programService.getAll().subscribe(d => this.programs = d); }

  load() {
    this.courseService.getAll().subscribe(data => this.courses = data);
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
    if (this.editingId) {
      this.courseService.update(this.editingId, this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    } else {
      this.courseService.create(this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.courseService.delete(id).subscribe(() => this.load());
    }
  }

  getProgramName(programId: string): string {
    return this.programs.find(p => p.id === programId)?.name || '';
  }
}