import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './core/components/landing-page/landing-page.component';
import { LoginPageComponent } from './core/components/login-page/login-page.component';
import { SignupPageComponent } from './core/components/signup-page/signup-page.component';
import { AboutUsComponent } from './core/components/about-us/about-us.component';
import { PostLoginLandingPageComponent } from './core/components/post-login-landing-page/post-login-landing-page.component';
import { LoginServiceService } from './core/services/login-service/login-service.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ForgetPasswordComponent } from './core/components/forget-password/forget-password.component';
import { ResetPasswordComponent } from './core/components/reset-password/reset-password.component';
import { PricingComponent } from './core/components/pricing/pricing.component';
import { ItemCategoryListingComponent } from './core/components/item-category-listing/item-category-listing.component';
import { SellerPortalComponent } from './core/components/seller-portal/seller-portal.component';
import { ItemListingComponent } from './core/components/item-listing/item-listing.component';
import { AuctionComponent } from './core/components/auction/auction.component';
import { AddFundsComponent } from './core/components/add-funds/add-funds.component';
import { TradebookComponent } from './core/components/tradebook/tradebook.component';
import { UserProfileComponent } from './core/components/user-profile/user-profile.component';
import { UpcomingAllAuctionsComponent } from './core/components/upcoming-all-auctions/upcoming-all-auctions.component';
import { PreLoginUpcomingAllAuctionsComponent } from './core/components/pre-login-upcoming-all-auctions/pre-login-upcoming-all-auctions.component';
import { authGuard } from './core/services/auth.guard';
import { SuggestedAllAuctionsComponent } from './core/components/suggested-all-auctions/suggested-all-auctions.component';

const routes: Routes = [
  // {
  //   path:'account',
  //   loadChildren:()=>
  //     import('./modules/account.module').then((m)=>m.AccountModule),
  // },
  {path:'', redirectTo:'landingpage',pathMatch:'full'},
  {path:'landingpage',component:LandingPageComponent},
  {path:'login',component:LoginPageComponent},
  {path:'signup',component:SignupPageComponent},
  {path: 'about-us', component: AboutUsComponent},
  {path: 'postLogin', component: PostLoginLandingPageComponent, canActivate:[authGuard]},
  {path: 'forget-password', component: ForgetPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  { path: 'pricing', component: PricingComponent },
  { path: 'itemCategory', component: ItemCategoryListingComponent, canActivate: [authGuard] },
  { path: 'sellerPortal', component: SellerPortalComponent, canActivate: [authGuard] },
  { path: 'itemListing', component: ItemListingComponent, canActivate: [authGuard] },
  { path: 'auction/:id', component: AuctionComponent, canActivate: [authGuard] },
  { path: 'upcoming-all-auctions', component: UpcomingAllAuctionsComponent, canActivate: [authGuard] },
  { path: 'pre-login-upcoming-all-auctions', component: PreLoginUpcomingAllAuctionsComponent },
  { path: 'addFunds', component: AddFundsComponent, canActivate: [authGuard] },
  { path: 'tradebook', component: TradebookComponent, canActivate: [authGuard] },
  { path: 'profile', component: UserProfileComponent, canActivate: [authGuard] },
  {path:'suggested-all-auctions', component: SuggestedAllAuctionsComponent, canActivate: [authGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  // providers:[
  //   LoginServiceService,
  //   {
  //     provide: HTTP_INTERCEPTORS,
  //     useClass: tokenInterceptorInterceptor,
  //     multi: true
  //   }
  // ]
})
export class AppRoutingModule { }
