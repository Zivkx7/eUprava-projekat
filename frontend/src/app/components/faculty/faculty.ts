import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FacultyService } from '../../services/faculty';


@Component({
  selector: 'app-faculty',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './faculty.html',
  styleUrls: ['./faculty.css']
})
export class Faculty implements OnInit {
  faculties: any[] = [];
  showForm = false;
  editingId: string | null = null;
  form = { name: '', address: '' };

  constructor(private facultyService: FacultyService) {}

  ngOnInit() { this.load(); }

  load() {
    this.facultyService.getAll().subscribe(data => this.faculties = data);
  }

  openForm(faculty?: any) {
    this.showForm = true;
    if (faculty) {
      this.editingId = faculty.id;
      this.form = { name: faculty.name, address: faculty.address };
    } else {
      this.editingId = null;
      this.form = { name: '', address: '' };
    }
  }

  save() {
    if (this.editingId) {
      this.facultyService.update(this.editingId, this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    } else {
      this.facultyService.create(this.form).subscribe(() => {
        this.load(); this.showForm = false;
      });
    }
  }

  delete(id: string) {
    if (confirm('Da li ste sigurni?')) {
      this.facultyService.delete(id).subscribe(() => this.load());
    }
  }
}