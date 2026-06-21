import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InterviewService } from '../../services/interview';
import { ApplicationService } from '../../services/application';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-interview',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './interview.html',
  styleUrls: ['./interview.css']
})
export class Interview implements OnInit {
  interviews: any[] = [];
  applications: any[] = [];
  showForm = false;
  form = { applicationId: '', dateTime: '', location: '', type: 'in-person' };

  constructor(
    private interviewService: InterviewService,
    private applicationService: ApplicationService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    this.load();
    this.applicationService.getAll().subscribe(d => this.applications = d);
  }

  load() {
    this.interviewService.getAll().subscribe(d => this.interviews = d);
  }

  openForm() {
    this.showForm = true;
    this.form = { applicationId: '', dateTime: '', location: '', type: 'in-person' };
  }

  save() {
    if (!this.form.applicationId || !this.form.dateTime) {
      alert('Prijava i termin su obavezni!');
      return;
    }
    this.interviewService.schedule(this.form).subscribe(() => {
      this.load();
      this.showForm = false;
    });
  }

  sendInvite(id: string) {
    this.interviewService.sendInvite(id).subscribe((res) => {
      alert('Pozivnica poslata:\n\n' + res.message);
      this.load();
    });
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.interviewService.delete(id).subscribe(() => this.load());
    }
  }

  applicationLabel(a: any): string {
    return `${a.candidateName} → ${a.jobOfferTitle}`;
  }
}
