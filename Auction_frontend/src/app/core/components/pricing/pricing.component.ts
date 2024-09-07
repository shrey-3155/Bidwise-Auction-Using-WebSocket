import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AccountServiceService } from '../../services/account-service/account-service.service';
import { AuctionServiceService } from '../../services/auction-service/auction-service.service';

@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
  styleUrls: ['./pricing.component.css']
})
export class PricingComponent {
  pricingForm: FormGroup;
  isPremiumUser: boolean = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private userService: AuctionServiceService
  ) {
    this.pricingForm = this.fb.group({
      cardNumber: ['', Validators.required],
      expiry: ['', Validators.required],
      cvc: ['', Validators.required],
      cardHolderName: ['', Validators.required],
      email: ['',[Validators.required, Validators.email]],
      terms: [false, [Validators.requiredTrue]]
    });
  }

  ngOnInit(): void {
    this.checkPremiumStatus();
  }

  checkPremiumStatus(): void {
    this.userService.getUserPremiumStatus().subscribe(
      (response: any) => {
        this.isPremiumUser = response.data;
        console.log('Is Premium User:', this.isPremiumUser);
      },
      (error) => {
        console.error('Error checking premium status:', error);
      }
    );
  }


  submitPayment(): void {
    const paymentDetails = this.pricingForm.value;
    const token = localStorage.getItem('token');  // Assuming you store JWT in localStorage after login
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post('http://172.17.3.242:8080/api/v1/membership/upgrade-to-premium', paymentDetails, { headers }).subscribe(
      (response: any) => {
        Swal.fire({
          title: 'Success!',
          text: 'You have been upgraded to a premium member!',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['/postLogin']);
        });
      },
      (error) => {
        console.error('Payment submission error:', error);
        Swal.fire({
          title: 'Error!',
          text: 'There was an error processing your payment. Please try again.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }

  cancelSubscription(): void {
    // Call the backend API to cancel the subscription
    this.http.post('http://172.17.3.242:8080/api/v1/membership/cancelPremium', {}).subscribe(
      (response: any) => {
        Swal.fire({
          title: 'Success!',
          text: 'Your subscription has been canceled.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          // Optionally refresh the page or navigate to another page
          this.router.navigate(['/postLogin']);
        });
      },
      (error) => {
        Swal.fire({
          title: 'Error!',
          text: 'There was an error canceling your subscription. Please try again.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }
}
