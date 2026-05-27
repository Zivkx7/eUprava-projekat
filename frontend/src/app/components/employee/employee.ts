import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee';
import { FacultyService } from '../../services/faculty';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee.html',
  styleUrls: ['./employee.css']
})
export class Employee implements OnInit {
  employees: any[] = [];
  faculties: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { fullName: '', role: 'Professor', email: '', facultyId: '' };
  roles = ['Professor', 'Assistant', 'Admin'];

  constructor(private employeeService: EmployeeService, private facultyService: FacultyService) {}

  ngOnInit() {
    this.load();
    this.facultyService.getAll().subscribe(d => this.faculties = d);
  }

  load() {
    this.employeeService.getAll().subscribe(data => this.employees = data);
  }

  openForm(employee?: any) {
    this.showForm = true;
    if (employee) {
      this.editingId = employee.id;
      this.form = { fullName: employee.fullName, role: employee.role, email: employee.email, facultyId: employee.facultyId };
    } else {
      this.editingId = null;
      this.form = { fullName: '', role: 'Professor', email: '', facultyId: '' };
    }
  }

  save() {
    if (this.editingId) {
      this.employeeService.update(this.editingId, this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    } else {
      this.employeeService.create(this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.employeeService.delete(id).subscribe(() => this.load());
    }
  }

  getFacultyName(facultyId: string): string {
    return this.faculties.find(f => f.id === facultyId)?.name || '';
  }
}