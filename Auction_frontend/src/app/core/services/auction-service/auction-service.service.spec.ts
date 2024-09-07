import { TestBed } from '@angular/core/testing';

import { AuctionServiceService } from './auction-service.service';

describe('AuctionServiceService', () => {
  let service: AuctionServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuctionServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
