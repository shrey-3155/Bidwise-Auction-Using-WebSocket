import { TestBed } from '@angular/core/testing';

import { AuctionSharedServiceService } from './auction-shared-service.service';

describe('AuctionSharedServiceService', () => {
  let service: AuctionSharedServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuctionSharedServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
