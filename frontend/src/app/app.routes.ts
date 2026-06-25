import { Routes } from '@angular/router';
import { Student } from './components/student/student';
import { Course } from './components/course/course';
import { Program } from './components/program/program';
import { Enrollment } from './components/enrollment/enrollment';
import { Exam } from './components/exam/exam';
import { Employee } from './components/employee/employee';
import { Login } from './components/login/login';
import { authGuard } from './guards/auth.guard';
import { Profile } from './components/profile/profile';
import { Home } from './components/home/home';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'home', component: Home, canActivate: [authGuard] },
  { path: 'students', component: Student, canActivate: [authGuard] },
  { path: 'programs', component: Program, canActivate: [authGuard] },
  { path: 'courses', component: Course, canActivate: [authGuard] },
  { path: 'enrollments', component: Enrollment, canActivate: [authGuard] },
  { path: 'exams', component: Exam, canActivate: [authGuard] },
  { path: 'employees', component: Employee, canActivate: [authGuard] },
  { path: 'profile', component: Profile, canActivate: [authGuard] },
];