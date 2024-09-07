import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFundsComponent } from './add-funds.component';

describe('AddFundsComponent', () => {
  let component: AddFundsComponent;
  let fixture: ComponentFixture<AddFundsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddFundsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddFundsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
