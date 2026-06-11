import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProgramService } from '../../services/program';
import { AuthService } from '../../services/auth';
import { NotificationService } from '../../services/notification';

@Component({
  selector: 'app-program',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './program.html',
  styleUrls: ['./program.css']
})
export class Program implements OnInit {
  programs: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { name: '', degree: 'BSc' };
  degrees = ['BSc', 'MSc', 'PhD'];

  constructor(
    private programService: ProgramService,
    public authService: AuthService,
    private notif: NotificationService
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.programService.getAll().subscribe(data => this.programs = data);
  }

  openForm(program?: any) {
    this.showForm = true;
    if (program) {
      this.editingId = program.id;
      this.form = { name: program.name, degree: program.degree };
    } else {
      this.editingId = null;
      this.form = { name: '', degree: 'BSc' };
    }
  }

  save() {
    if (!this.form.name || !this.form.degree) {
      this.notif.error('Sva polja su obavezna!');
      return;
    }
    if (this.editingId) {
      this.programService.update(this.editingId, this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Program uspešno izmenjen!');
      });
    } else {
      this.programService.create(this.form).subscribe(() => {
        this.load();
        this.showForm = false;
        this.notif.success('Program uspešno dodan!');
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.programService.delete(id).subscribe(() => {
        this.load();
        this.notif.success('Program uspešno obrisan!');
      });
    }
  }
}