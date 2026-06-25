import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class JobOfferService {
  private api = 'http://localhost:8082/api/job-offers';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getById(id: string): Observable<any> {
    return this.http.get<any>(`${this.api}/${id}`);
  }

  search(q: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/search?q=${encodeURIComponent(q)}`);
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
}
