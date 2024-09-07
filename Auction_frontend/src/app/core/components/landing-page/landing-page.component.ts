import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuctionItemsServiceService } from '../../services/landing-page-service/auction-items-service.service';
import { AuctionSharedServiceService } from '../../services/auction-shared-service/auction-shared-service.service';
import { LoginServiceService } from '../../services/login-service/login-service.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit{
  
  constructor(
    private router: Router,
    private loginService : LoginServiceService
  ) { }

  ngOnInit(): void { 
    this.loginService.logout();
  }

  LoginRedirect(){
    this.router.navigate(['/login']);
  }

  SignupRedirect(){
    this.router.navigate(['/signup']);
  }

  AboutUsRedirect(){
    this.router.navigate(['/about-us']);
  }
  
}

