import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FairCheckInService {
  private api = 'http://localhost:8082/api/fair-checkins';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getByCandidate(candidateId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/candidate/${candidateId}`);
  }

  checkIn(candidateId: string): Observable<any> {
    return this.http.post<any>(this.api, { candidateId });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
