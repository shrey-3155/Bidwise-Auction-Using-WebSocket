import { Component } from '@angular/core';
import { PostLoginLandingPageServiceService } from '../../services/post-login-landing-page-service/post-login-landing-page-service.service';
import { Router } from '@angular/router';
import { AuctionSharedServiceService } from '../../services/auction-shared-service/auction-shared-service.service';

@Component({
  selector: 'app-upcoming-all-auctions',
  templateUrl: './upcoming-all-auctions.component.html',
  styleUrl: './upcoming-all-auctions.component.css'
})
export class UpcomingAllAuctionsComponent {
  allAuctions: any[] = [];

  constructor(private auctionService: PostLoginLandingPageServiceService, private router: Router, private auctionSharedService: AuctionSharedServiceService) { }

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
      this.router.navigate(['/auction',auction.itemId]  );
      console.log(`Bid now on item ID: ${auction.itemId}`);
  }

  
}
