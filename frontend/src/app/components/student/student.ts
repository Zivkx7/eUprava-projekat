import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../services/student';


@Component({
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student.html',
  styleUrls: ['./student.css']
})
export class Student implements OnInit {
  students: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { indexNo: '', name: '', email: '', status: 'ACTIVE' };
  statuses = ['ACTIVE', 'GRADUATED', 'SUSPENDED'];

  constructor(private studentService: StudentService) {}

  ngOnInit() { this.load(); }

  load() {
    this.studentService.getAll().subscribe(data => this.students = data);
  }

  openForm(student?: any) {
    this.showForm = true;
    if (student) {
      this.editingId = student.id;
      this.form = { indexNo: student.indexNo, name: student.name, email: student.email, status: student.status };
    } else {
      this.editingId = null;
      this.form = { indexNo: '', name: '', email: '', status: 'ACTIVE' };
    }
  }

  save() {
    if (this.editingId) {
      this.studentService.update(this.editingId, this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    } else {
      this.studentService.create(this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.studentService.delete(id).subscribe(() => this.load());
    }
  }
}