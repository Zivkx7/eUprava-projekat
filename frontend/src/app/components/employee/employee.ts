import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee';
import { AuthService } from '../../services/auth';
import { NotificationService } from '../../services/notification';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee.html',
  styleUrls: ['./employee.css']
})
export class Employee implements OnInit {
  employees: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { fullName: '', role: 'Professor', email: '', password: '' };
  roles = ['Professor', 'Assistant', 'Admin'];

  constructor(
    private employeeService: EmployeeService,
    public authService: AuthService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.employeeService.getAll().subscribe(data => this.employees = data);
  }

  openForm(employee?: any) {
    this.showForm = true;
    if (employee) {
      this.editingId = employee.id;
      this.form = { fullName: employee.fullName, role: employee.role, email: employee.email, password: '' };
    } else {
      this.editingId = null;
      this.form = { fullName: '', role: 'Professor', email: '', password: '' };
    }
  }

  save() {
    if (!this.form.fullName || !this.form.email || !this.form.role) {
      this.notif.error('Sva polja su obavezna!');
      return;
    }
    if (!this.form.email.includes('@')) {
      this.notif.error('Email nije ispravan!');
      return;
    }
    if (!this.editingId && !this.form.password) {
      this.notif.error('Lozinka je obavezna!');
      return;
    }
    if (this.editingId) {
      this.employeeService.update(this.editingId, this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Zaposleni uspešno izmenjen!');
      });
    } else {
      this.employeeService.create(this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Zaposleni uspešno dodan!');
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.employeeService.delete(id).subscribe(() => {
        this.load();
        this.notif.success('Zaposleni uspešno obrisan!');
      });
    }
  }
}