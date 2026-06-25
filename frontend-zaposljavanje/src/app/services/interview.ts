import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class InterviewService {
  private api = 'http://localhost:8082/api/interviews';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getByApplication(applicationId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/application/${applicationId}`);
  }

  schedule(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  update(id: string, data: any): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}`, data);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  sendInvite(id: string): Observable<any> {
    return this.http.post<any>(`${this.api}/${id}/invite`, {});
  }
}
