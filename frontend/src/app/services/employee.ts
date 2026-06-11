import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private api = 'http://localhost:8081/api/employees';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.api);
  }

  getById(id: string): Observable<any> {
    return this.http.get<any>(`${this.api}/${id}`);
  }

  getByFaculty(facultyId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/faculty/${facultyId}`);
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
  getByEmail(email: string): Observable<any> {
  return this.http.get<any>(`${this.api}/by-email?email=${encodeURIComponent(email)}`);
}
}