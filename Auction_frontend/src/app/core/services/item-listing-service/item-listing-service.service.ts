import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs';
import { Route, Router } from '@angular/router';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ItemListingServiceService {
  private baseUrl = environment.apiUrl;

  addItemEndPoint: string = this.baseUrl+ '/item/additem';
  getAllItemsEndPoint: string = this.baseUrl+'/item/getitems';
  deleteItemEndPoint: String = this.baseUrl+ '/item/deleteItemListed';

  constructor(private http: HttpClient,public router:Router) { }
  addItemForAuction(itemDetails:any): Observable<any>{
    let api = `${this.addItemEndPoint}`;
    return this.http.post(api,itemDetails).pipe(catchError(this.handleError));
  }


  getAllItems():Observable<any>{
    let api = `${this.getAllItemsEndPoint}`;
    return this.http.get(api).pipe(catchError(this.handleError));
  }

  deleteItem(itemId: number): Observable<any> {
    return this.http.delete(`${this.deleteItemEndPoint}`, { params: { itemId: itemId.toString() } }).pipe(catchError(this.handleError));
  }

  getItemByItemId(itemId: any): Observable<any>{
    return this.http.get(this.baseUrl+'/item/itemsById',{params:{itemId:itemId.toString()}}).pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(errorMessage);
  }

  

}
