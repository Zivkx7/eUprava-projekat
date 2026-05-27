import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProgramService } from '../../services/program';
import { FacultyService } from '../../services/faculty';


@Component({
  selector: 'app-program',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './program.html',
  styleUrls: ['./program.css']
})
export class Program implements OnInit {
  programs: any[] = [];
  faculties: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { name: '', degree: 'BSc', facultyId: '' };
  degrees = ['BSc', 'MSc', 'PhD'];

  constructor(private programService: ProgramService, private facultyService: FacultyService) {}

  ngOnInit() { this.load(); this.facultyService.getAll().subscribe(d => this.faculties = d); }

  load() {
    this.programService.getAll().subscribe(data => this.programs = data);
  }

  openForm(program?: any) {
    this.showForm = true;
    if (program) {
      this.editingId = program.id;
      this.form = { name: program.name, degree: program.degree, facultyId: program.facultyId };
    } else {
      this.editingId = null;
      this.form = { name: '', degree: 'BSc', facultyId: '' };
    }
  }

  save() {
    if (this.editingId) {
      this.programService.update(this.editingId, this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    } else {
      this.programService.create(this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.programService.delete(id).subscribe(() => this.load());
    }
  }

  getFacultyName(facultyId: string): string {
    return this.faculties.find(f => f.id === facultyId)?.name || '';
  }
}