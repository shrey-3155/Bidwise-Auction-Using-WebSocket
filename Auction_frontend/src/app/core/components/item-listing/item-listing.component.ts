import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemListingServiceService } from '../../services/item-listing-service/item-listing-service.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { dateRangeValidator } from './dateRangeValidator';

import Swal from 'sweetalert2';
@Component({
  selector: 'app-item-listing',
  templateUrl: './item-listing.component.html',
  styleUrls: ['./item-listing.component.css']
})
export class ItemListingComponent implements OnInit {

  itemForm: FormGroup;
  categoryName: string = "";
  selectedFile: File | null = null;
  itemPhotoInvalid: boolean = false;
  itemPhotoTouched: boolean = false;

  constructor(
    private fb: FormBuilder,
    private itemService: ItemListingServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.itemForm = this.fb.group({
      itemName: ['', Validators.required],
      itemMaker: ['', Validators.required],
      description: ['', Validators.required],
      pricePaid: ['', [Validators.required, Validators.pattern('^[0-9]+(\.[0-9]{1,2})?$')]],
      currency: ['', Validators.required],
      itemCondition: ['', Validators.required],
      minBidAmount: ['', [Validators.required, Validators.pattern('^[0-9]+(\.[0-9]{1,2})?$')]],
      startDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endDate: ['', Validators.required],
      endTime: ['', Validators.required],
      categoryName: ''
    },
    { validators: dateRangeValidator() }
  );
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.categoryName = params['category'];
      this.itemForm.patchValue({
        categoryName: this.categoryName
      });
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
      this.itemPhotoInvalid = false;
      this.itemPhotoTouched = true;
    } else {
      this.selectedFile = null;
      this.itemPhotoInvalid = true;
      this.itemPhotoTouched = false;
    }
  }

  combineDateTime(date: string, time: string): string {
    return `${date}T${time}:00`;
  }

  onSubmit() {
    if (this.itemForm.valid && this.selectedFile) {
      const formData = new FormData();
      const formValues = this.itemForm.value;

      // Combine date and time fields into LocalDateTime strings
      const startDateTime = this.combineDateTime(formValues.startDate, formValues.startTime);
      const endDateTime = this.combineDateTime(formValues.endDate, formValues.endTime);

      // Append JSON data as part of FormData
      formData.append('itemDTO', new Blob([JSON.stringify({
        itemName: formValues.itemName,
        itemMaker: formValues.itemMaker,
        description: formValues.description,
        pricePaid: formValues.pricePaid,
        currency: formValues.currency,
        itemCondition: formValues.itemCondition,
        minBidAmount: formValues.minBidAmount,
        startTime: startDateTime,
        endTime: endDateTime,
        categoryName: this.categoryName
      })],{type:'application/json'}));

      // Append file to FormData
      // const validtTypes = ["image/jpeg","image/png","image/jpg"]
      formData.append('file', this.selectedFile);

      this.itemService.addItemForAuction(formData).subscribe(response => {
        Swal.fire({
          title: 'Item Added Successfully!',
          text: 'Your item has been submitted for auction.',
          icon: 'success',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'Ok'
        }).then(() => {
          this.router.navigate(['/sellerPortal']); 
        });;
      }, error => {
        console.error('Error adding item', error);
        Swal.fire({
          title: 'Error Adding Item',
          text: 'An error occurred while adding your item. Please try again.',
          icon: 'error',
          confirmButtonColor: '#dc3545',
        });
      });
    } else {
      this.itemPhotoTouched = true;
      this.itemPhotoInvalid = true;
    }
  }
}
