import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ExamRegistrationService {
  private api = 'http://localhost:8081/api/exam-registrations';

  constructor(private http: HttpClient) {}

  register(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  getByStudent(studentId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/student/${studentId}`);
  }

  getByExam(examId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/exam/${examId}`);
  }
  grade(id: string, grade: number): Observable<any> {
  return this.http.put<any>(`${this.api}/${id}/grade?grade=${grade}`, {});
}

  cancel(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}