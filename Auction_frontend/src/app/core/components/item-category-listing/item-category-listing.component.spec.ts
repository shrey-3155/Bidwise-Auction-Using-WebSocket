import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemCategoryListingComponent } from './item-category-listing.component';

describe('ItemCategoryListingComponent', () => {
  let component: ItemCategoryListingComponent;
  let fixture: ComponentFixture<ItemCategoryListingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ItemCategoryListingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemCategoryListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
