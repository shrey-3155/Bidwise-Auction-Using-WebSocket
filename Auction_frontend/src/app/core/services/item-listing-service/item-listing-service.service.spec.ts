import { TestBed } from '@angular/core/testing';

import { ItemListingServiceService } from './item-listing-service.service';

describe('ItemListingServiceService', () => {
  let service: ItemListingServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItemListingServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
