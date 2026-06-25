import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecommendationService } from '../../services/recommendation';
import { JobOfferService } from '../../services/job-offer';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-recommendation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './recommendation.html',
  styleUrls: ['./recommendation.css']
})
export class Recommendation implements OnInit {
  offers: any[] = [];
  selectedOfferId = '';
  ranked: any[] = [];
  employees: any[] = [];
  loaded = false;

  constructor(
    private recommendationService: RecommendationService,
    private jobOfferService: JobOfferService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    this.jobOfferService.getAll().subscribe(d => this.offers = d);
  }

  rank() {
    if (!this.selectedOfferId) {
      alert('Odaberite ponudu!');
      return;
    }
    this.recommendationService.rank(this.selectedOfferId).subscribe(d => {
      this.ranked = d;
      this.loaded = true;
    });
  }

  suggest() {
    if (!this.selectedOfferId) {
      alert('Odaberite ponudu!');
      return;
    }
    this.recommendationService.suggest(this.selectedOfferId).subscribe(d => {
      this.ranked = d;
      this.loaded = true;
    });
  }

  loadFacultyEmployees() {
    this.recommendationService.facultyEmployees().subscribe(d => this.employees = d);
  }
}
