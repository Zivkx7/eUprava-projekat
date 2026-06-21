import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EducationRecordService } from '../../services/education-record';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-education-record',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './education-record.html',
  styleUrls: ['./education-record.css']
})
export class EducationRecord implements OnInit {
  records: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form: any = this.emptyForm();

  constructor(private educationService: EducationRecordService, public authService: AuthService) {}

  ngOnInit() {
    this.load();
  }

  emptyForm() {
    return {
      candidateId: localStorage.getItem('candidateId') || '',
      facultyId: '', facultyName: '', programId: '', programName: '',
      studentId: '', degree: 'BSc', startDate: '', endDate: '',
      graduated: false, graduationDate: '', avgGradeSnapshot: null
    };
  }

  load() {
    if (this.authService.isCandidate()) {
      const id = localStorage.getItem('candidateId');
      if (id) {
        this.educationService.getByCandidate(id).subscribe(d => this.records = d);
      }
    } else {
      this.educationService.getAll().subscribe(d => this.records = d);
    }
  }

  openForm(record?: any) {
    this.showForm = true;
    if (record) {
      this.editingId = record.id;
      this.form = { ...record };
    } else {
      this.editingId = null;
      this.form = this.emptyForm();
    }
  }

  save() {
    if (!this.form.candidateId || !this.form.facultyName || !this.form.degree) {
      alert('Kandidat, naziv fakulteta i nivo studija su obavezni!');
      return;
    }
    const req = this.editingId
      ? this.educationService.update(this.editingId, this.form)
      : this.educationService.create(this.form);
    req.subscribe(() => {
      this.load();
      this.showForm = false;
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.educationService.delete(id).subscribe(() => this.load());
    }
  }

  verify(id: string) {
    this.educationService.verify(id).subscribe((res) => {
      alert(res.verified
        ? 'Obrazovanje verifikovano kod Fakulteta. Zvanični prosek: ' + (res.avgGradeSnapshot ?? 'N/A')
        : 'Verifikacija nije uspela (student nije pronađen ili Fakultet nedostupan).');
      this.load();
    });
  }
}
