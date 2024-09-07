import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuggestedAllAuctionsComponent } from './suggested-all-auctions.component';

describe('SuggestedAllAuctionsComponent', () => {
  let component: SuggestedAllAuctionsComponent;
  let fixture: ComponentFixture<SuggestedAllAuctionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SuggestedAllAuctionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SuggestedAllAuctionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
