import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ResetServiceService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  sendResetLink(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/user/forgot-password`,{email});
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/user/reset-password`, {token, newPassword});
  }}
