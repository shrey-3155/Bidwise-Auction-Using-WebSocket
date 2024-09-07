import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemListingComponent } from './item-listing.component';

describe('ItemListingComponent', () => {
  let component: ItemListingComponent;
  let fixture: ComponentFixture<ItemListingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ItemListingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
