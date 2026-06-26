import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent {
  private readonly otherAppUrl = 'http://localhost:4200/login';

  constructor(public authService: AuthService, private router: Router) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  switchApp() {
    window.location.href = this.otherAppUrl;
  }

  getRole(): string {
    return this.authService.getRole() || '';
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
}
