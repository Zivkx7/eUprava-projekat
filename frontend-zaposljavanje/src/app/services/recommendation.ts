import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RecommendationService {
  private api = 'http://localhost:8082/api/recommendations';

  constructor(private http: HttpClient) {}

  rank(offerId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}?offerId=${offerId}`);
  }

  suggest(offerId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/suggest?offerId=${offerId}`);
  }

  facultyEmployees(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/faculty-employees`);
  }
}
