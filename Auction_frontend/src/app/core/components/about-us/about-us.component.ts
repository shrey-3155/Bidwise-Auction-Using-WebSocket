import { Component, OnInit } from '@angular/core';
import { LoginServiceService } from '../../services/login-service/login-service.service';

@Component({
  selector: 'app-about-us',
  templateUrl: './about-us.component.html',
  styleUrl: './about-us.component.css'
})
export class AboutUsComponent implements OnInit{

  isAuth: boolean = false;
  constructor(private loginservice: LoginServiceService) {}
 
  ngOnInit(): void {
    this.isAuth = this.loginservice.isAuthenticated() 
  }
}
