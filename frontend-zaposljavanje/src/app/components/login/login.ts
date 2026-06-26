import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  private readonly otherAppUrl = 'http://localhost:4200/login';

  form = { username: '', password: '' };
  error = '';

  constructor(
    private authService: AuthService,
    private http: HttpClient,
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

        if (res.role === 'CANDIDATE') {
          this.http.get<any>(`http://localhost:8082/api/candidates/by-email?email=${encodeURIComponent(this.form.username)}`)
            .subscribe({
              next: (c) => { localStorage.setItem('candidateId', c.id); this.router.navigate(['/home']); },
              error: () => this.router.navigate(['/home'])
            });
        } else if (res.role === 'EMPLOYER') {
          this.http.get<any>(`http://localhost:8082/api/employers/by-email?email=${encodeURIComponent(this.form.username)}`)
            .subscribe({
              next: (e) => { localStorage.setItem('employerId', e.id); this.router.navigate(['/home']); },
              error: () => this.router.navigate(['/home'])
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
