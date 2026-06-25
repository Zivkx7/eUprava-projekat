import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee';
import { CourseService } from '../../services/course';
import { ProgramService } from '../../services/program';
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
  allCourses: any[] = [];
  allPrograms: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { fullName: '', role: 'Professor', email: '', password: '' };
  roles = ['Professor', 'Assistant', 'Admin'];

  // za dodelu predmeta/programa
  showAssignForm = false;
  assignEmployee: any = null;
  employeeCourseIds: string[] = [];
  employeeProgramIds: string[] = [];

  constructor(
    private employeeService: EmployeeService,
    private courseService: CourseService,
    private programService: ProgramService,
    public authService: AuthService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.load();
    this.courseService.getAll().subscribe(d => this.allCourses = d);
    this.programService.getAll().subscribe(d => this.allPrograms = d);
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

  // ====== DODELA PREDMETA I PROGRAMA ======

  openAssignForm(employee: any) {
    this.assignEmployee = employee;
    this.showAssignForm = true;
    this.employeeService.getCourses(employee.id).subscribe(courses => {
      this.employeeCourseIds = courses.map(c => c.id);
    });
    this.employeeService.getPrograms(employee.id).subscribe(programs => {
      this.employeeProgramIds = programs.map(p => p.id);
    });
  }

  hasCourse(courseId: string): boolean {
    return this.employeeCourseIds.includes(courseId);
  }

  hasProgram(programId: string): boolean {
    return this.employeeProgramIds.includes(programId);
  }

  toggleCourse(courseId: string) {
    if (this.hasCourse(courseId)) {
      this.employeeService.removeCourse(this.assignEmployee.id, courseId).subscribe(() => {
        this.employeeCourseIds = this.employeeCourseIds.filter(id => id !== courseId);
        this.notif.success('Predmet uklonjen!');
      });
    } else {
      this.employeeService.addCourse(this.assignEmployee.id, courseId).subscribe(() => {
        this.employeeCourseIds.push(courseId);
        this.notif.success('Predmet dodeljen!');
      });
    }
  }

toggleProgram(programId: string) {
  if (this.hasProgram(programId)) {
    this.employeeService.removeProgram(this.assignEmployee.id, programId).subscribe(() => {
      this.employeeProgramIds = this.employeeProgramIds.filter(id => id !== programId);
      // ukloni i predmete tog programa
      const coursesToRemove = this.allCourses
        .filter(c => c.programId === programId && this.employeeCourseIds.includes(c.id));
      coursesToRemove.forEach(c => {
        this.employeeService.removeCourse(this.assignEmployee.id, c.id).subscribe(() => {
          this.employeeCourseIds = this.employeeCourseIds.filter(id => id !== c.id);
        });
      });
      this.notif.success('Program uklonjen!');
    });
  } else {
    this.employeeService.addProgram(this.assignEmployee.id, programId).subscribe(() => {
      this.employeeProgramIds.push(programId);
      this.notif.success('Program dodeljen!');
    });
  }
}
  getAvailableCourses(): any[] {
  return this.allCourses.filter(c => this.employeeProgramIds.includes(c.programId));
}

  closeAssignForm() {
    this.showAssignForm = false;
    this.assignEmployee = null;
  }
}