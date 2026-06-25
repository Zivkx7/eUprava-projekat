import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  private api = 'http://localhost:8082/api/applications';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getByCandidate(candidateId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/candidate/${candidateId}`);
  }

  getByJobOffer(jobOfferId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/job-offer/${jobOfferId}`);
  }

  getByStatus(status: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/status/${status}`);
  }

  apply(data: any): Observable<any> {
    return this.http.post<any>(this.api, data);
  }

  withdraw(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  updateStatus(id: string, status: string): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}/status?status=${status}`, {});
  }
}
