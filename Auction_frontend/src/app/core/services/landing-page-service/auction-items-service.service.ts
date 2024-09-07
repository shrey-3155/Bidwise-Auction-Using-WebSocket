import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuctionItemsServiceService {
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient,public router:Router) { }

  getUpcomingAuctions(): Observable<any> {
    return this.http.get<any[]>(`${this.apiUrl}/auction/upcoming`);
  }
}


