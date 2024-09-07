import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginServiceService } from '../../../core/services/login-service/login-service.service';

@Component({
  selector: 'app-pre-login-header',
  templateUrl: './pre-login-header.component.html',
  styleUrl: './pre-login-header.component.css'
})
export class PreLoginHeaderComponent {
  constructor(private router: Router,
              private loginService: LoginServiceService
  ) { }

  isAuth(){
    if(this.loginService.isAuthenticated()){
      this.router.navigate(['/postLogin']);
    } else {
      this.router.navigate(['/landingpage']);
    }
  }

  LoginRedirect(){
    this.router.navigate(['/login']);
  }

  SignupRedirect(){
    this.router.navigate(['/signup']);
  }

}
