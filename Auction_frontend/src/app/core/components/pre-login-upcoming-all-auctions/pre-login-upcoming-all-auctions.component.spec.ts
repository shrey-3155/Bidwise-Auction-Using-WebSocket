import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreLoginUpcomingAllAuctionsComponent } from './pre-login-upcoming-all-auctions.component';

describe('PreLoginUpcomingAllAuctionsComponent', () => {
  let component: PreLoginUpcomingAllAuctionsComponent;
  let fixture: ComponentFixture<PreLoginUpcomingAllAuctionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PreLoginUpcomingAllAuctionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PreLoginUpcomingAllAuctionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
