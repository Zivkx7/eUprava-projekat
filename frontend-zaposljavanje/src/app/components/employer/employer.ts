import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployerService } from '../../services/employer';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-employer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employer.html',
  styleUrls: ['./employer.css']
})
export class Employer implements OnInit {
  employers: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { name: '', sector: '', email: '', password: '' };

  constructor(private employerService: EmployerService, public authService: AuthService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.employerService.getAll().subscribe(data => this.employers = data);
  }

  openForm(employer?: any) {
    this.showForm = true;
    if (employer) {
      this.editingId = employer.id;
      this.form = { name: employer.name, sector: employer.sector, email: employer.email, password: '' };
    } else {
      this.editingId = null;
      this.form = { name: '', sector: '', email: '', password: '' };
    }
  }

  save() {
    if (!this.form.name || !this.form.sector || !this.form.email) {
      alert('Naziv, sektor i email su obavezni!');
      return;
    }
    if (!this.editingId && !this.form.password) {
      alert('Lozinka je obavezna pri kreiranju!');
      return;
    }
    const req = this.editingId
      ? this.employerService.update(this.editingId, this.form)
      : this.employerService.create(this.form);
    req.subscribe(() => {
      this.load();
      this.showForm = false;
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.employerService.delete(id).subscribe(() => this.load());
    }
  }
}
