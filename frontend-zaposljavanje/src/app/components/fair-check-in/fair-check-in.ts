import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FairCheckInService } from '../../services/fair-check-in';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-fair-check-in',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './fair-check-in.html',
  styleUrls: ['./fair-check-in.css']
})
export class FairCheckIn implements OnInit {
  checkIns: any[] = [];

  constructor(private fairService: FairCheckInService, public authService: AuthService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    if (this.authService.isCandidate()) {
      const id = localStorage.getItem('candidateId');
      if (id) {
        this.fairService.getByCandidate(id).subscribe(d => this.checkIns = d);
      }
    } else {
      this.fairService.getAll().subscribe(d => this.checkIns = d);
    }
  }

  checkIn() {
    const id = localStorage.getItem('candidateId');
    if (!id) {
      alert('Samo kandidati mogu da se prijave na sajam.');
      return;
    }
    this.fairService.checkIn(id).subscribe(() => this.load());
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.fairService.delete(id).subscribe(() => this.load());
    }
  }
}
