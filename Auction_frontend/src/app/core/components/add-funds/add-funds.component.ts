import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from "@angular/router"; 
import { AddFundsService } from '../../services/add-funds/add-funds.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-funds',
  templateUrl: './add-funds.component.html',
  styleUrl: './add-funds.component.css'
})
export class AddFundsComponent {

  addFundsForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private addFundService: AddFundsService,
    private router: Router
  ){ 
    this.addFundsForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      cardNumber: ['', Validators.required],
      expiry: ['', Validators.required],
      cvc: ['', Validators.required],
      cardholderName: ['', Validators.required],
      address: ['', Validators.required],
      country: ['', Validators.required],
      city: ['', Validators.required],
      amount: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
      currency: ['', Validators.required],
      terms: [false, Validators.requiredTrue]
    });
  }

  onSubmit(): void {
    if (this.addFundsForm.valid) {
      const userId = 1;
      const amount = this.addFundsForm.get('amount')?.value;

      this.addFundService.addFunds(userId, amount).subscribe(
        response => {
          Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Funds added successfully!',
          })
          this.addFundsForm.reset();
          this.router.navigate(['/postLogin'])
        },
        error => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error adding funds: ' + error.message,
          })
        }
      );
    }
  }

}
