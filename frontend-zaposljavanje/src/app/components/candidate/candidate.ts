import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CandidateService } from '../../services/candidate';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-candidate',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './candidate.html',
  styleUrls: ['./candidate.css']
})
export class Candidate implements OnInit {
  candidates: any[] = [];
  showForm = false;
  editingId: string | null = null;
  searchName = '';
  form = { fullName: '', email: '', password: '' };

  constructor(private candidateService: CandidateService, public authService: AuthService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.candidateService.getAll().subscribe(data => this.candidates = data);
  }

  search() {
    if (!this.searchName) {
      this.load();
      return;
    }
    this.candidateService.search(this.searchName).subscribe(data => this.candidates = data);
  }

  openForm(candidate?: any) {
    this.showForm = true;
    if (candidate) {
      this.editingId = candidate.id;
      this.form = { fullName: candidate.fullName, email: candidate.email, password: '' };
    } else {
      this.editingId = null;
      this.form = { fullName: '', email: '', password: '' };
    }
  }

  save() {
    if (!this.form.fullName || !this.form.email) {
      alert('Ime i email su obavezni!');
      return;
    }
    if (!this.editingId && !this.form.password) {
      alert('Lozinka je obavezna pri kreiranju!');
      return;
    }
    const req = this.editingId
      ? this.candidateService.update(this.editingId, this.form)
      : this.candidateService.create(this.form);
    req.subscribe(() => {
      this.load();
      this.showForm = false;
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.candidateService.delete(id).subscribe(() => this.load());
    }
  }
}
