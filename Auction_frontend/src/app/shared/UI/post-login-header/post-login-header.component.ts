import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { LoginServiceService } from '../../../core/services/login-service/login-service.service';
@Component({
  selector: 'app-post-login-header',
  templateUrl: './post-login-header.component.html',
  styleUrl: './post-login-header.component.css'
})

export class PostLoginHeaderComponent {
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private logout: LoginServiceService
  ){}

  isAuth(){
    // alert("Called" + this.logout.isAuthenticated())
    if(this.logout.isAuthenticated()){
      this.router.navigate(['/postLogin']);
    } else {
      this.router.navigate(['/landingpage']);
    }
  }

  signout(){
    console.log("logged out");
      this.logout.logout();
    }
}
