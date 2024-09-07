import { TestBed } from '@angular/core/testing';

import { AddFundsService } from './add-funds.service';

describe('AddFundsService', () => {
  let service: AddFundsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddFundsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
