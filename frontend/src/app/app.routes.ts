import { Routes } from '@angular/router';
import { Faculty } from './components/faculty/faculty';
import { Student } from './components/student/student';
import { Course } from './components/course/course';
import { Program } from './components/program/program';
import { Enrollment } from './components/enrollment/enrollment';
import { Exam } from './components/exam/exam';
import { Employee } from './components/employee/employee';

 

export const routes: Routes = [
  { path: '', redirectTo: 'faculties', pathMatch: 'full' },
  { path: 'faculties', component: Faculty },
  { path: 'students', component: Student },
  { path: 'programs', component: Program },
  { path: 'courses', component: Course },
  { path: 'enrollments', component: Enrollment },
  { path: 'exams', component: Exam },
  { path: 'employees', component: Employee},
];