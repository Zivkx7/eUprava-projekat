import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApplicationService } from '../../services/application';
import { JobOfferService } from '../../services/job-offer';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-application',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './application.html',
  styleUrls: ['./application.css']
})
export class Application implements OnInit {
  applications: any[] = [];
  offers: any[] = [];
  showForm = false;
  selectedOfferId = '';
  statuses = ['SUBMITTED', 'INTERVIEW', 'OFFER', 'REJECTED'];

  constructor(
    private applicationService: ApplicationService,
    private jobOfferService: JobOfferService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    this.load();
    if (this.authService.isCandidate()) {
      this.jobOfferService.getAll().subscribe(d => this.offers = d);
    }
  }

  load() {
    if (this.authService.isCandidate()) {
      const id = localStorage.getItem('candidateId');
      if (id) {
        this.applicationService.getByCandidate(id).subscribe(d => this.applications = d);
      }
    } else {
      this.applicationService.getAll().subscribe(d => this.applications = d);
    }
  }

  apply() {
    const candidateId = localStorage.getItem('candidateId');
    if (!candidateId || !this.selectedOfferId) {
      alert('Odaberite ponudu!');
      return;
    }
    this.applicationService.apply({ candidateId, jobOfferId: this.selectedOfferId }).subscribe(() => {
      this.load();
      this.showForm = false;
      this.selectedOfferId = '';
      alert('Prijava poslata. Pokrenuta je verifikacija obrazovanja sa Fakultetom.');
    });
  }

  withdraw(id: string) {
    if (confirm('Povući prijavu?')) {
      this.applicationService.withdraw(id).subscribe(() => this.load());
    }
  }

  changeStatus(app: any, status: string) {
    this.applicationService.updateStatus(app.id, status).subscribe(() => this.load());
  }
}
