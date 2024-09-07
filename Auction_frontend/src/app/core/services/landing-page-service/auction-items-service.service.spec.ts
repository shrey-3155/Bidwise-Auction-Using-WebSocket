import { TestBed } from '@angular/core/testing';

import { AuctionItemsServiceService } from './auction-items-service.service';

describe('AuctionItemsServiceService', () => {
  let service: AuctionItemsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuctionItemsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
