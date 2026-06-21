import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobOfferService } from '../../services/job-offer';
import { EmployerService } from '../../services/employer';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-job-offer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './job-offer.html',
  styleUrls: ['./job-offer.css']
})
export class JobOffer implements OnInit {
  offers: any[] = [];
  employers: any[] = [];
  showForm = false;
  editingId: string | null = null;
  searchQuery = '';
  form = { title: '', description: '', location: '', employerId: '' };

  constructor(
    private jobOfferService: JobOfferService,
    private employerService: EmployerService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    this.load();
    if (this.authService.isAdmin()) {
      this.employerService.getAll().subscribe(d => this.employers = d);
    }
  }

  load() {
    this.jobOfferService.getAll().subscribe(data => this.offers = data);
  }

  search() {
    if (!this.searchQuery) {
      this.load();
      return;
    }
    this.jobOfferService.search(this.searchQuery).subscribe(data => this.offers = data);
  }

  canManage(): boolean {
    return this.authService.isAdmin() || this.authService.isEmployer();
  }

  openForm(offer?: any) {
    this.showForm = true;
    const employerId = this.authService.isEmployer()
      ? (localStorage.getItem('employerId') || '')
      : '';
    if (offer) {
      this.editingId = offer.id;
      this.form = { title: offer.title, description: offer.description, location: offer.location, employerId: offer.employerId };
    } else {
      this.editingId = null;
      this.form = { title: '', description: '', location: '', employerId };
    }
  }

  save() {
    if (!this.form.title || !this.form.location || !this.form.employerId) {
      alert('Naziv, lokacija i poslodavac su obavezni!');
      return;
    }
    const req = this.editingId
      ? this.jobOfferService.update(this.editingId, this.form)
      : this.jobOfferService.create(this.form);
    req.subscribe(() => {
      this.load();
      this.showForm = false;
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.jobOfferService.delete(id).subscribe(() => this.load());
    }
  }
}
