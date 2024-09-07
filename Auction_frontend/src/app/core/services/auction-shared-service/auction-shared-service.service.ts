import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuctionSharedServiceService { private auctionSource = new BehaviorSubject<any>(null);
  currentAuction = this.auctionSource.asObservable();

  constructor() { }

  changeAuction(auction: any) {
    this.auctionSource.next(auction);
  }
}
