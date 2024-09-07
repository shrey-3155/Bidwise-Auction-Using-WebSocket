import { Component } from '@angular/core';
import { LoginServiceService } from '../../../core/services/login-service/login-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logo-header',
  templateUrl: './logo-header.component.html',
  styleUrl: './logo-header.component.css'
})
export class LogoHeaderComponent {

  constructor(
    private loginService: LoginServiceService,
    private router: Router
  ) {}

  isAuth(){
    if(this.loginService.isAuthenticated()){
      this.router.navigate(['/postLogin']);
    } else {
      this.router.navigate(['/landingpage']);
    }
  }
}
