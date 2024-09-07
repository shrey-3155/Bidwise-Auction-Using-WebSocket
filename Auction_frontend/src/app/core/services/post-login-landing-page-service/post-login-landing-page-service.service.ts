import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostLoginLandingPageServiceService {
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient,public router:Router) { }

  getUpcomingAuctions(): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/auction/getAuction`);
  }
  getSuggestedAuctions(): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/auction/suggested`);
  }
}
