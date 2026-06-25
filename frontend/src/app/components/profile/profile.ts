import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../services/student';
import { AuthService } from '../../services/auth';
import { EmployeeService } from '../../services/employee';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class Profile implements OnInit {
  profile: any = null;
  loading = true;
  gpa: number | null = null;
  myCourses: any[] = [];
  myPrograms: any[] = [];

  constructor(
    private studentService: StudentService,
    private employeeService: EmployeeService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    const username = this.authService.getUsername();
    if (!username) return;

if (this.authService.isStudent()) {
  this.studentService.getByEmail(username).subscribe({
    next: (data) => {
      this.profile = data;
      this.loading = false;
      this.studentService.getGPA(data.id).subscribe(g => this.gpa = g);
    },
    error: () => this.loading = false
  });
    } else if (this.authService.isProfessor()) {
      this.employeeService.getByEmail(username).subscribe({
        next: (data) => {
          this.profile = data;
          this.loading = false;
          this.employeeService.getCourses(data.id).subscribe(c => this.myCourses = c);
          this.employeeService.getPrograms(data.id).subscribe(p => this.myPrograms = p);
        },
        error: () => this.loading = false
      });
    }
  }
}