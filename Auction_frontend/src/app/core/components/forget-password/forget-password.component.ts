import { Component } from '@angular/core';
import { FormBuilder, FormGroup,Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ResetServiceService } from '../../services/reset-service/reset-service.service';
import Swal from 'sweetalert2';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrl: './forget-password.component.css'
})
export class ForgetPasswordComponent {

  email : string = "";
  forgetPasswordForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private passwordResetService: ResetServiceService,
    private router: Router
  ) {
    this.forgetPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    })
  }
  ForgetPasswordRedirect(){
    this.passwordResetService.sendResetLink(this.forgetPasswordForm.get('email')?.value)
        .subscribe({
          next: (response) => {
            console.log(response)
            if (response?.httpCode === 200){
              Swal.fire({
                title: 'Password Reset Link',
                text: 'Password Reset Link Sent Successfully!',
                icon: 'success',
                confirmButtonText: 'Ok'
              }).then(() => {
                this.router.navigate(['/landingpage']); 
              });
            } else {
              let errorMessage = response?.errorMessage || 'An unexpected error occurred. Please try again later.';
              Swal.fire({
                title: 'Password Reset Link Failed',
                text: errorMessage,
                icon: 'error',
                confirmButtonText: 'Retry'
              });
            }
          },
          error: (error) => {
            console.error('Password reset failed', error);
            let errorMessage = error?.error?.errorMessage || 'An unexpected error occurred. Please try again later.';
            Swal.fire({
              title: 'Password Reset Link Failed',
              text: errorMessage,
              icon: 'error',
              confirmButtonText: 'Retry'
            })
          },
          complete: () => {
            console.log('Sending Link Process Successful.')
          }
        });
    // this.router.navigate(['/forgetpassword'])
  }

}
