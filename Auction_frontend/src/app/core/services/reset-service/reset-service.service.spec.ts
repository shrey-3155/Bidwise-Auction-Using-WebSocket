import { TestBed } from '@angular/core/testing';

import { ResetServiceService } from './reset-service.service';

describe('ResetServiceService', () => {
  let service: ResetServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResetServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
