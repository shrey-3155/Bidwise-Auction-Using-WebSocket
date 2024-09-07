import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuctionServiceService } from '../../services/auction-service/auction-service.service';
import { ToastrService } from 'ngx-toastr';
import { LoginServiceService } from '../../services/login-service/login-service.service';
import { Subscription, interval } from 'rxjs';
import { ItemListingServiceService } from '../../services/item-listing-service/item-listing-service.service';
import { AccountServiceService } from '../../services/account-service/account-service.service';

@Component({
  selector: 'app-auction',
  templateUrl: './auction.component.html',
  styleUrls: ['./auction.component.css'],
})
export class AuctionComponent implements OnInit, OnDestroy {
  formdata: FormGroup;
  userDetails: any;
  itemId: any;
  auctionDetails: any;
  itemDetails:any;
  startTime: Date | undefined;
  startMonth:number | undefined;
  endTime: Date | undefined;
  currentTime: Date | undefined;
  countdown: string | undefined;
  private intervalId: any;
  private timerSubscription: Subscription | undefined;
  isAuctionStarted = false;
  currentBid:any;
  hasBid = false;
  isAuctionClosed = false;
  accountDetails:any;
  funds:any;
  premiumDetails:any;
  isPremium = false;
  isSubscribed = false;

  constructor(
    private auctionService: AuctionServiceService,
    private itemListingService : ItemListingServiceService,
    private accountService:AccountServiceService,
    private route: ActivatedRoute,
    private loginService: LoginServiceService,
    private toastr: ToastrService
  ) {
    this.formdata = new FormGroup({
      currentBid: new FormControl(''),
      itemId: new FormControl(''),
      bid: new FormControl(),
    });
    this.currentTime = new Date();
  }

  ngOnInit(): void {
    // Temporary taking item Id as 1
    // this.itemId=this.route.snapshot.params['itemId'];
    console.log('The current time is:' + this.currentTime);
    this.itemId = this.route.snapshot.params['id'];
    console.log('The item Id is:' + this.itemId);
    // this.formdata.patchValue({
    //   itemId: 5
    //   //currentBid:101                         //TODO: Add the proper current Bid
    // });
    if (this.itemId != undefined) {
      this.loadItemData(this.itemId);
      this.loadAuctionData(this.itemId);
      this.loadAccountFundsData();
      this.loadUserPremiumData();
    }
    //this.subscribe();
  }

  subscribe() {
    console.log('Typed');
    this.isSubscribed=true;
    this.auctionService.getBidResponse(this.formdata);
  }

  bid() {
    console.log('The bidded amount entered is:'+this.formdata.get('bid'));
    //const currentBid = this.formdata.get('currentBid')?.value
    console.log('The current Bid is:'+this.formdata.get('currentBid')?.value);

    if(((Number(this.formdata.get('bid')?.value) < Number(this.itemDetails.minBidAmount)))
        ){
      this.toastr.error(
        'Please enter amount higher than the minimum Bid'
      );
    }
    else if((Number((this.formdata.get('bid')?.value) <= Number(this.formdata.get('currentBid')?.value)))){
      this.toastr.error(
        'Please enter amount higher than the current Bid'
      );
    }
    else if(Number(this.formdata.get('bid')?.value) > this.funds){
      this.toastr.error(
        'Not enough funds in the account'
      );
    }
    else{
      console.log("Typed OnSubmit2");
      this.auctionService.publish(this.formdata, this.loginService.getToken());
    }
  }

  loadItemData(itemId: any){
    this.itemListingService.getItemByItemId(itemId).subscribe(
      (itemInfo) => {
        console.log('Item info are:'+itemInfo);
        this.itemDetails = itemInfo.data[0];
        console.log('Item details are:'+this.itemDetails);
        this.formdata.patchValue({
          itemId: this.itemId,
          currentBid: this.itemDetails.minBidAmount                         //TODO: Add the proper current Bid
        });
      },
      error => {
        console.log(error)
      }
    )
  }

  loadAuctionData(itemId: any) {
    this.auctionService.getAuctionDetails(itemId).subscribe(
      (data) => {
        console.log(data);
        this.auctionDetails = data;
        this.startTime = new Date(this.auctionDetails.data.startTime);
        this.startMonth= this.startTime.getMonth()+1;
        this.endTime = new Date(this.auctionDetails.data.endTime);
        console.log('The startTime for the auction is:' + this.startTime);

        console.log('The endTime for the auction is:' + this.endTime);
        this.startCountdown(this.startTime, this.endTime);
      },
      error => {
        console.log(error);
      }
    )
  }

  loadAccountFundsData(){
    this.accountService.getAccountFunds().subscribe(
      (fundsDetails) => {
        console.log('The data fecthed from the account is:'+fundsDetails);
        this.accountDetails = fundsDetails;
        this.funds = this.accountDetails.data;
      },
      error => {
        console.log(error);
      }
    )
  }

  loadUserPremiumData(){
    this.auctionService.getUserPremiumStatus().subscribe(
      (premiumStatusData) => {
        console.log('The data fetched for the User Premium details is:'+premiumStatusData);
        this.premiumDetails = premiumStatusData;
        this.isPremium = this.premiumDetails.data;
      },
      error => {
        console.log(error);
      }
    )
  }

  startCountdown(startTime: Date, endTime: Date): void {
    const startTimeMillis = startTime.getTime();
    console.log('The startTimeMillis is:'+startTimeMillis);
    let endTimeMillis = endTime.getTime();
    console.log('The endTimeMillis is:'+endTimeMillis);
    if(!this.isPremium){
      console.log('The user is not a Premium User!');
      endTimeMillis=endTimeMillis - 45000;                                    //Update here if need to increase time for Premium User
      console.log('The new endTimeMillis is:'+endTimeMillis);
    }else{
      console.log('The user is a Premium User!');
    }
    console.log('The Final endTimeMillis is:'+endTimeMillis);
    //console.log('The startTime is:' + this.startTime);
    
    this.timerSubscription = interval(1000).subscribe(() => {
      this.currentTime = new Date();
      const currentTimeMillis = this.currentTime.getTime();
      let timeDifference: number;
      
      if (currentTimeMillis < startTimeMillis) {
        // Countdown to auction start
        timeDifference = startTimeMillis - currentTimeMillis;
        this.countdown = this.formatTimeDifference(timeDifference);
      } else if (currentTimeMillis >= startTimeMillis && currentTimeMillis <= endTimeMillis) {
        // Auction has started, countdown to auction end
        timeDifference = endTimeMillis - currentTimeMillis;
        this.isAuctionStarted = true;
        this.countdown = this.formatTimeDifference(timeDifference);
      } else {
        // Auction has ended
        this.isAuctionStarted = false;
        this.countdown = 'Auction has ended.';
        if(!this.isPremium){
          console.log('The use is not premium so syncing the post Auction');
          setTimeout(() => {
            this.auctionService.closeAuction(this.itemId).subscribe(
              (data) => {
                console.log(data);
              },
              (error) => {
                console.log(error);
              }
            );
          }, 46000);                                                //Update here for Time syncing the Post Auction API. Put 1 sec extra of what set above for premium user
        }else{
          console.log('The user is premium. Resolving the Post Auction')
          this.auctionService.closeAuction(this.itemId).subscribe(
            (data) =>{
              console.log(data);
            },
            error => {
              console.log(error);
            }
          );
        }

        if (this.timerSubscription) {
          this.timerSubscription.unsubscribe();
        }
      }
    });
  }

  getTimeDifference(startTime: Date, endTime: Date) {
    return endTime.getTime() - startTime.getTime();
  }

  formatTimeDifference(timeDifference: number): string {
    const days = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
    const hours = Math.floor((timeDifference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

    return `${days}d ${hours}h ${minutes}m ${seconds}s`;
  }

  ngOnDestroy() {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }
}
