import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemListingServiceService } from '../../services/item-listing-service/item-listing-service.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-seller-portal',
  templateUrl: './seller-portal.component.html',
  styleUrl: './seller-portal.component.css'
})
export class SellerPortalComponent implements OnInit  {

  items: any = [];

  constructor(
    private fb: FormBuilder,
    private itemService: ItemListingServiceService,
    private route: ActivatedRoute,
    private router: Router
  ){

  }

  getCurrentDateTime(): Date {
    return new Date(); // returns the current date and time
  }

  ngOnInit(): void {
    this.itemService.getAllItems().subscribe(
      (response) => {
        this.items = response;
      },
      (error) => {
        console.error('Error fetching items:', error);
      }
    );
  }

  deleteItem(itemId: number): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you really want to delete this item? This process cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.itemService.deleteItem(itemId).subscribe(
          (response) => {
            Swal.fire({
              title: 'Deleted!',
              text: 'Your item has been deleted.',
              icon: 'success',
              confirmButtonColor: '#3085d6',
              confirmButtonText: 'Ok'
            }).then(() => {
              this.ngOnInit();
            });
          },
          (error) => {
            console.error('Error deleting item:', error);
            Swal.fire({
              title: 'Error!',
              text: 'An error occurred while deleting your item. Please try again.',
              icon: 'error',
              confirmButtonColor: '#dc3545',
            });
          }
        );
      }
    });
  }
  
}
