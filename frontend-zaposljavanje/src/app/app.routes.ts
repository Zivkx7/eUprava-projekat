import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Home } from './components/home/home';
import { Profile } from './components/profile/profile';
import { Employer } from './components/employer/employer';
import { JobOffer } from './components/job-offer/job-offer';
import { Candidate } from './components/candidate/candidate';
import { EducationRecord } from './components/education-record/education-record';
import { Application } from './components/application/application';
import { Interview } from './components/interview/interview';
import { FairCheckIn } from './components/fair-check-in/fair-check-in';
import { Recommendation } from './components/recommendation/recommendation';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'home', component: Home, canActivate: [authGuard] },
  { path: 'profile', component: Profile, canActivate: [authGuard] },
  { path: 'employers', component: Employer, canActivate: [authGuard] },
  { path: 'job-offers', component: JobOffer, canActivate: [authGuard] },
  { path: 'candidates', component: Candidate, canActivate: [authGuard] },
  { path: 'education-records', component: EducationRecord, canActivate: [authGuard] },
  { path: 'applications', component: Application, canActivate: [authGuard] },
  { path: 'interviews', component: Interview, canActivate: [authGuard] },
  { path: 'fair-checkins', component: FairCheckIn, canActivate: [authGuard] },
  { path: 'recommendations', component: Recommendation, canActivate: [authGuard] },
];
