import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpcomingAllAuctionsComponent } from './upcoming-all-auctions.component';

describe('UpcomingAllAuctionsComponent', () => {
  let component: UpcomingAllAuctionsComponent;
  let fixture: ComponentFixture<UpcomingAllAuctionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpcomingAllAuctionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpcomingAllAuctionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
