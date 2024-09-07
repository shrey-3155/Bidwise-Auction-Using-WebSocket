import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ResetServiceService } from '../../services/reset-service/reset-service.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  resetForm: FormGroup = this.fb.group({
    newPassword: ['', [Validators.required, Validators.minLength(9)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(9)]]
  });
  token: string = ''; // Initialize as empty string or null
  passwordMismatch: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private passwordResetService: ResetServiceService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || ''; // Ensures a string, avoiding null
    this.resetForm.get('confirmPassword')?.valueChanges.subscribe(() => {
      this.checkPasswordMatch();
    });

    // console.log(this.token);
  }

  
  checkPasswordMatch(): void {
    const password = this.resetForm.get('newPassword')?.value;
    const confirmPassword = this.resetForm.get('confirmPassword')?.value;
    this.passwordMismatch = password && confirmPassword && password !== confirmPassword;
  }


  onSubmit(): void {
    if (this.resetForm.valid) {
      const newPassword = this.resetForm.get('newPassword')!.value; 
      // console.log("sssss"+newPassword);
      
      this.passwordResetService.resetPassword(this.token, newPassword)
        .subscribe({
          next: (response) => {
            console.log(response)
            if (response?.httpCode === 200){
              Swal.fire({
                title: 'Reset password successful',
                text: 'Your password has been updated successfully!',
                icon: 'success',
                confirmButtonText: 'Ok'
              }).then(() => {
                this.router.navigate(['/login']); 
              });
            } else {
              let errorMessage = response?.errorMessage || 'An unexpected error occurred. Please try again later.';
              Swal.fire({
                title: 'Reset Password Failed',
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
              title: 'Reset Password Failed',
              text: errorMessage,
              icon: 'error',
              confirmButtonText: 'Retry'
            })
          },
          complete: () => {
            console.log('Password Reset Process Completed');
          }
        });
    }
  }
}
