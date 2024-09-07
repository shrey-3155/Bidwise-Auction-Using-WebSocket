import { Component } from '@angular/core';
import { PostLoginLandingPageServiceService } from '../../services/post-login-landing-page-service/post-login-landing-page-service.service';
import { Router } from '@angular/router';
import { AuctionSharedServiceService } from '../../services/auction-shared-service/auction-shared-service.service';
 
@Component({
  selector: 'app-suggested-all-auctions',
  templateUrl: './suggested-all-auctions.component.html',
  styleUrl: './suggested-all-auctions.component.css'
})
export class SuggestedAllAuctionsComponent {
  allAuctions: any[] = [];
 
  constructor(
    private auctionService: PostLoginLandingPageServiceService,
    private router: Router,
    private auctionSharedService: AuctionSharedServiceService) { }
 
  ngOnInit(): void {
    this.fetchAllAuctions();
  }
 
  fetchAllAuctions(): void {
    this.auctionService.getSuggestedAuctions().subscribe({
      next: (response: any) => {
        const data = response.data;
        if (Array.isArray(data)) {
          this.allAuctions = data;
          console.log(this.allAuctions);
        } else {
          console.error('Expected data to be an array but got:', data);
        }
      },
      error: (error) => {
        console.error('Error fetching suggested auctions', error);
      }
    });
 
   
  }
 
  bidNow(auction: any): void {
    this.auctionSharedService.changeAuction(auction);  
      this.router.navigate(['/auction',auction.itemId]  );
      console.log(`Bid now on item ID: ${auction.itemId}`);
  }
 
}