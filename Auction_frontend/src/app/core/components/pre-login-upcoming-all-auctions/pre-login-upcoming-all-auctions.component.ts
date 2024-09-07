import { Component } from '@angular/core';
import { PostLoginLandingPageServiceService } from '../../services/post-login-landing-page-service/post-login-landing-page-service.service';
import { AuctionItemsServiceService } from '../../services/landing-page-service/auction-items-service.service';
import { Router } from '@angular/router';
import { AuctionSharedServiceService } from '../../services/auction-shared-service/auction-shared-service.service';


@Component({
  selector: 'app-pre-login-upcoming-all-auctions',
  templateUrl: './pre-login-upcoming-all-auctions.component.html',
  styleUrl: './pre-login-upcoming-all-auctions.component.css'
})
export class PreLoginUpcomingAllAuctionsComponent {
  allAuctions: any[] = [];

  constructor(
    private auctionService: AuctionItemsServiceService, 
    private router: Router,
    private auctionSharedService: AuctionSharedServiceService, // Inject the service
  ) { }

  ngOnInit(): void {
    this.fetchAllAuctions();
  }

  fetchAllAuctions(): void {
    this.auctionService.getUpcomingAuctions().subscribe({
      next: (data) => {
        this.allAuctions = data;
        console.log('All Auctions:', this.allAuctions);
      },
      error: (error) => {
        console.error('Error fetching all auctions', error);
      }
    });
  }

  bidNow(auction: any): void {
    this.auctionSharedService.changeAuction(auction); 
    this.router.navigate(['/login'], {
      queryParams: { 
        returnUrl: `/auction`,
        itemId: auction.itemId 
      }
    });     
     console.log(`Bid now on item ID: ${auction.itemId}`);
  }

}
