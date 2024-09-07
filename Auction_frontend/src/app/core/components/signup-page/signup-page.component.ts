import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { SignUpServiceService } from '../../services/signup-service/sign-up-service.service';
import Swal from 'sweetalert2';
import { LoginServiceService } from '../../services/login-service/login-service.service';

@Component({
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrl: './signup-page.component.css'
})
export class SignupPageComponent implements OnInit {

  signupForm: FormGroup;
  cities: any = [];
  countries:any = [];
  allTimeZones: string[] = [
    'ACDT', 'ACST', 'ACT', 'ACWST', 'ADT', 'AEDT', 'AEST', 'AET', 'AFT', 'AKDT', 'AKST', 'ALMT', 'AMST', 'AMT',
    'ANAT', 'AQTT', 'ART', 'AST', 'AWST', 'AZOST', 'AZOT', 'AZT', 'BNT', 'BIOT', 'BIT', 'BOT', 'BRST', 'BRT',
    'BST', 'BTT', 'CAT', 'CCT', 'CDT', 'CEST', 'CET', 'CHADT', 'CHAST', 'CHOT', 'CHOST', 'CHST', 'CHUT', 'CIST',
    'CKT', 'CLST', 'CLT', 'COST', 'COT', 'CST', 'CT', 'CVT', 'CWST', 'CXT', 'DAVT', 'DDUT', 'DFT', 'EASST',
    'EAST', 'EAT', 'ECT', 'EDT', 'EEST', 'EET', 'EGST', 'EGT', 'EST', 'ET', 'FET', 'FJT', 'FKST', 'FKT', 'FNT',
    'GALT', 'GAMT', 'GET', 'GFT', 'GILT', 'GIT', 'GMT', 'GST', 'GYT', 'HDT', 'HAEC', 'HST', 'HKT', 'HMT', 'HOVST',
    'HOVT', 'ICT', 'IDLW', 'IDT', 'IOT', 'IRDT', 'IRKT', 'IRST', 'IST', 'JST', 'KALT', 'KGT', 'KOST', 'KRAT', 'KST',
    'LHST', 'LINT', 'MAGT', 'MART', 'MAWT', 'MDT', 'MET', 'MEST', 'MHT', 'MIST', 'MIT', 'MMT', 'MSK', 'MST', 'MT',
    'MUT', 'MVT', 'MYT', 'NCT', 'NDT', 'NFT', 'NOVT', 'NPT', 'NST', 'NT', 'NUT', 'NZDT', 'NZST', 'OMST', 'ORAT',
    'PDT', 'PET', 'PETT', 'PGT', 'PHOT', 'PHT', 'PHST', 'PKT', 'PMDT', 'PMST', 'PONT', 'PST', 'PT', 'PWT', 'PYST',
    'PYT', 'RET', 'ROTT', 'SAKT', 'SAMT', 'SAST', 'SBT', 'SCT', 'SDT', 'SGT', 'SLST', 'SRET', 'SRT', 'SST', 'SYOT',
    'TAHT', 'THA', 'TFT', 'TJT', 'TKT', 'TLT', 'TMT', 'TRT', 'TOT', 'TST', 'TVT', 'ULAST', 'ULAT', 'UTC', 'UYST',
    'UYT', 'UZT', 'VET', 'VLAT', 'VOLT', 'VOST', 'VUT', 'WAKT', 'WAST', 'WAT', 'WEST', 'WET', 'WIB', 'WIT', 'WITA',
    'WGST', 'WGT', 'WST', 'YAKT', 'YEKT'
  ];  passwordMismatch: boolean = false;

  constructor(
    private fb: FormBuilder, 
    private router: Router,
    public signUpService: SignUpServiceService,
    public route:ActivatedRoute,
    public loginService:LoginServiceService
  ) {
    this.signupForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      firstname  : ['', Validators.required],
      lastname: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      country: ['', Validators.required],
      city: ['', Validators.required],
      terms: [false, Validators.requiredTrue],
      timezone:'IST',
      isPremium:false,
      role:"USER"
    });
  }

  LoginRedirect() {
    this.router.navigate(['/login']);
  }

  postLoginRedirect() {
    if (this.signupForm.invalid || this.passwordMismatch) {
      return;
    }
    const { firstname, lastname, email, password, role, city, timezone, isPremium } = this.signupForm.value;
    const payload = { firstname, lastname, email, password, role, city, timezone, isPremium };  

    const observer = {
      next: (res: any) => {
        console.log(res);
        if (res?.httpCode === 200) {
          Swal.fire({
            title: 'Signup Successful',
            text: 'You have been registered successfully!',
            icon: 'success',
            confirmButtonText: 'Ok'
          }).then(() => {
            this.router.navigate(['/login']); 
          });
        } else {
          let errorMessage = res?.errorMessage || 'An unexpected error occurred. Please try again later.';
          Swal.fire({
            title: 'Signup Failed',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Retry'
          });
        }
      },
      error: (error: any) => {
        console.error('Signup error:', error);
        let errorMessage = error?.error?.errorMessage || 'An unexpected error occurred. Please try again later.';
        Swal.fire({
          title: 'Signup Failed',
          text: errorMessage,
          icon: 'error',
          confirmButtonText: 'Retry'
        });
      },
      complete: () => {
        console.log('Signup process completed.');
      }
    };
      this.signUpService.signUpUser(payload).subscribe(observer);
  }
  
  
  

  ngOnInit(): void {
    // Listen for changes in password and confirm password fields to check for mismatch
    this.signupForm.get('confirmPassword')?.valueChanges.subscribe(() => {
      this.checkPasswordMatch();
    });

    this.signupForm.get('country')?.valueChanges.subscribe(()=>{
      this.fetchCities(this.signupForm.get('country')?.value);
    })

    this.signUpService.fetchAllCountries().subscribe(
      (response) => {
        this.countries = response;
        console.log(this.countries);
      },
      (error) => {
        console.error('Error fetching Countries:', error);
      }
    );
  }

  fetchCities(country:String):void{
    this.signUpService.fetchAllCities(country).subscribe(
      (response) => {
        this.cities = response;
        console.log(this.cities);
      },
      (error) => {
        console.error('Error fetching cities:', error);
      }
    );
  }
  checkPasswordMatch(): void {
    const password = this.signupForm.get('password')?.value;
    const confirmPassword = this.signupForm.get('confirmPassword')?.value;
    this.passwordMismatch = password && confirmPassword && password !== confirmPassword;
  }

  onSubmit(): void {
    // Check form validity before submission
    if (this.signupForm.invalid || this.passwordMismatch) {
      return;
    }
    const formData = this.signupForm.value;
    this.router.navigate(['/login']);
  }

  navigateToLogin(): void {
    // Redirect to the login page
    this.router.navigate(['/login']);
  }
}
