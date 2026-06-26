import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { StudentService } from '../../services/student';
import { EmployeeService } from '../../services/employee';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  private readonly otherAppUrl = 'http://localhost:4201/login';

  form = { username: '', password: '' };
  error = '';

  constructor(
    private authService: AuthService,
    private studentService: StudentService,
    private employeeService: EmployeeService,
    private router: Router
  ) {}

  switchApp() {
    window.location.href = this.otherAppUrl;
  }

  login() {
    if (!this.form.username || !this.form.password) {
      this.error = 'Korisničko ime i lozinka su obavezni!';
      return;
    }
    this.authService.login(this.form).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('username', this.form.username);

        if (res.role === 'STUDENT') {
          this.studentService.getByEmail(this.form.username).subscribe((student: any) => {
            localStorage.setItem('programId', student.programId);
            localStorage.setItem('studentId', student.id);
            this.router.navigate(['/home']);
          });
        } else if (res.role === 'PROFESSOR') {
          this.employeeService.getByEmail(this.form.username).subscribe((employee: any) => {
            localStorage.setItem('employeeId', employee.id);
            this.router.navigate(['/home']);
          });
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: () => {
        this.error = 'Pogrešno korisničko ime ili lozinka!';
      }
    });
  }
}