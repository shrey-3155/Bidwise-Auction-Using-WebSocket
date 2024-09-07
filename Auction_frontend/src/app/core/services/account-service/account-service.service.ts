import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountServiceService {
  private baseUrl = environment.apiUrl;
  constructor(private http: HttpClient) { }

  getAccountFunds(){
    return this.http.get(this.baseUrl+'/account/balance');
  }
}
