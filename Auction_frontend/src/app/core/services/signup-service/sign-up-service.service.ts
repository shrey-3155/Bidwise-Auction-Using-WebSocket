import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class SignUpServiceService {
  endpoint: string = environment.apiUrl+ '/user/register';
  locationEndpoint : string = environment.apiUrl+ '/locale/countries';
  cityEndpoint : String = environment.apiUrl+ '/locale/cities';
  // headers = new HttpHeaders().set('Content-Type', 'application/json');
  constructor(private http: HttpClient, public router: Router) { }

  signUpUser(userDetails:any): Observable<any>{
    let api = `${this.endpoint}`;
    return this.http.post(api,userDetails).pipe(catchError(this.handleError));
  }

  fetchAllCountries(): Observable<any>{
    let api = `${this.locationEndpoint}`;
    return this.http.get(api).pipe(catchError(this.handleError))
  }

  fetchAllCities(country: String):Observable<any>{
    return this.http.get(`${this.cityEndpoint}`, { params: { countryName: country.toString() } }).pipe(catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    let msg = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      msg = error.error.message;
    } else {
      // server-side error
      msg = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(msg);
  }
}
