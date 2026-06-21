import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class Profile implements OnInit {
  private base = 'http://localhost:8082/api';
  candidate: any = null;
  employer: any = null;
  cv: any = null;

  constructor(public authService: AuthService, private http: HttpClient) {}

  ngOnInit() {
    if (this.authService.isCandidate()) {
      const id = localStorage.getItem('candidateId');
      if (id) {
        this.http.get<any>(`${this.base}/candidates/${id}`).subscribe(c => this.candidate = c);
      }
    } else if (this.authService.isEmployer()) {
      const id = localStorage.getItem('employerId');
      if (id) {
        this.http.get<any>(`${this.base}/employers/${id}`).subscribe(e => this.employer = e);
      }
    }
  }

  generateCV() {
    const id = localStorage.getItem('candidateId');
    if (id) {
      this.http.get<any>(`${this.base}/candidates/${id}/cv`).subscribe(cv => this.cv = cv);
    }
  }
}
