import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../services/student';
import { AuthService } from '../../services/auth';
import { NotificationService } from '../../services/notification';
import { ProgramService } from '../../services/program';

@Component({
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student.html',
  styleUrls: ['./student.css']
})
export class Student implements OnInit {
  students: any[] = [];
  programs: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { indexNo: '', name: '', email: '', status: 'ACTIVE', password: '', programId: '' };
  statuses = ['ACTIVE', 'GRADUATED', 'SUSPENDED'];

  constructor(
    private studentService: StudentService,
    private programService: ProgramService,
    public authService: AuthService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.load();
    this.programService.getAll().subscribe(d => this.programs = d);
  }

  load() {
    this.studentService.getAll().subscribe(data => this.students = data);
  }

  openForm(student?: any) {
    this.showForm = true;
    if (student) {
      this.editingId = student.id;
      this.form = { indexNo: student.indexNo, name: student.name, email: student.email, status: student.status, password: '', programId: student.programId };
    } else {
      this.editingId = null;
      this.form = { indexNo: '', name: '', email: '', status: 'ACTIVE', password: '', programId: '' };
    }
  }

  save() {
    if (!this.form.indexNo || !this.form.name || !this.form.email || !this.form.status) {
      this.notif.error('Sva polja su obavezna!');
      return;
    }
    if (!this.editingId && !this.form.password) {
      this.notif.error('Lozinka je obavezna!');
      return;
    }
    if (!this.form.email.includes('@')) {
      this.notif.error('Email nije ispravan!');
      return;
    }
    if (!this.form.programId) {
      this.notif.error('Odaberite program!');
      return;
    }
    if (this.editingId) {
      this.studentService.update(this.editingId, this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Student uspešno izmenjen!');
      });
    } else {
      this.studentService.create(this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Student uspešno dodan!');
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.studentService.delete(id).subscribe(() => {
        this.load();
        this.notif.success('Student uspešno obrisan!');
      });
    }
  }

  getProgramName(programId: string): string {
    return this.programs.find(p => p.id === programId)?.name || '';
  }
}