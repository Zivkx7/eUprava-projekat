import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EducationRecordService {
  private api = 'http://localhost:8082/api/education-records';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getByCandidate(candidateId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/candidate/${candidateId}`);
  }

  create(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  update(id: string, data: any): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}`, data);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  verify(id: string): Observable<any> {
    return this.http.post<any>(`${this.api}/${id}/verify`, {});
  }
}
