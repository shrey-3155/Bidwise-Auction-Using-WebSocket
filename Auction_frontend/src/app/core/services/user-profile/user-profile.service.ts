import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  private endPoint = environment.apiUrl+ '/profile';
  constructor(private http: HttpClient) { }

  getUserProfile(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/details?userId=${userId}`);
  }

  getAuctionParticipation(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/auctionsParticipated?userId=${userId}`);
  }

  getBidStats(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/bidStats?userId=${userId}`);
  }

  getCategoryBidStats(userId: number): Observable<any> {
    return this.http.get<any>(`${this.endPoint}/categoryBidStats?userId=${userId}`);
  }
}
