import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerPortalComponent } from './seller-portal.component';

describe('SellerPortalComponent', () => {
  let component: SellerPortalComponent;
  let fixture: ComponentFixture<SellerPortalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SellerPortalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SellerPortalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
