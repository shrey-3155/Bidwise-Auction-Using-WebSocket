import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SignupPageComponent } from './components/signup-page/signup-page.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { SharedModule } from '../shared/shared.module';
import { AboutUsComponent } from './components/about-us/about-us.component';
import { RouterModule } from '@angular/router';
import { PostLoginLandingPageComponent } from './components/post-login-landing-page/post-login-landing-page.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { PricingComponent } from './components/pricing/pricing.component';
import { ItemCategoryListingComponent } from './components/item-category-listing/item-category-listing.component';
import { SellerPortalComponent } from './components/seller-portal/seller-portal.component';
import { ItemListingComponent } from './components/item-listing/item-listing.component';
import { AuctionComponent } from './components/auction/auction.component';
import { UpcomingAllAuctionsComponent } from './components/upcoming-all-auctions/upcoming-all-auctions.component';
import { PreLoginUpcomingAllAuctionsComponent } from './components/pre-login-upcoming-all-auctions/pre-login-upcoming-all-auctions.component';
import { AddFundsComponent } from './components/add-funds/add-funds.component';
import { TradebookComponent } from './components/tradebook/tradebook.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { NgChartsModule } from 'ng2-charts';
import { SuggestedAllAuctionsComponent } from './components/suggested-all-auctions/suggested-all-auctions.component';

@NgModule({
  declarations: [
    LoginPageComponent,
    SignupPageComponent,
    LandingPageComponent,
    AboutUsComponent,
    PostLoginLandingPageComponent,
    ForgetPasswordComponent,
    ResetPasswordComponent,
    PricingComponent,
    PostLoginLandingPageComponent,
    ItemCategoryListingComponent,
    SellerPortalComponent,
    ItemListingComponent,
    AuctionComponent,
    UpcomingAllAuctionsComponent,
    PreLoginUpcomingAllAuctionsComponent,
    TradebookComponent,
    UserProfileComponent,
    AddFundsComponent,
    SuggestedAllAuctionsComponent],
  exports : [
    LandingPageComponent,
    AboutUsComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgChartsModule
  ],
})
export class CoreModule { }
