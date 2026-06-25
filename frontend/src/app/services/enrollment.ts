import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EnrollmentService {
  private api = 'http://localhost:8081/api/enrollments';

  constructor(private http: HttpClient) {}

  getByStudent(studentId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/student/${studentId}`);
  }

  getByCourse(courseId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/course/${courseId}`);
  }

  enroll(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  updateGrade(id: string, grade: number): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}/grade?grade=${grade}`, {});
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}