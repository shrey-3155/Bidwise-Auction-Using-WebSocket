import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { LoginServiceService } from '../../services/login-service/login-service.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  email: string = '';
  password: string = '';
  returnUrl: string = '/postLogin';  

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private loginService: LoginServiceService
  ) { }

  ngOnInit(): void {
    if(this.loginService.isAuthenticated()){
      this.router.navigate([this.returnUrl]);
    }
    // this.route.queryParams.subscribe(params => {
    //   this.returnUrl = params['returnUrl'] || this.returnUrl;
    // });
    // this.loginService.logout();
  }

  SignupRedirect() {
    this.router.navigate(['/signup']);
  }

  onSubmit() {
    const loginData = { email: this.email, password: this.password };
    this.loginService.login(loginData).subscribe(
      (response: any) => {
        if (response.access_token) {
          this.loginService.storeToken(response.access_token);
          Swal.fire({
            title: 'Login Successful',
            text: 'You have successfully logged in!',
            icon: 'success',
            confirmButtonText: 'Ok'
          }).then(() => {
            this.router.navigate([this.returnUrl]);
          });
        }
      },
      (error) => {
        console.error('Login failed', error);
        Swal.fire({
          title: 'Login Failed',
          text: 'Invalid credentials. Please try again.',
          icon: 'error',
          confirmButtonText: 'Ok'
        });
      }
    );
  }
}
