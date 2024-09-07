import { TestBed } from '@angular/core/testing';

import { SignUpServiceService } from './sign-up-service.service';

describe('SignUpServiceService', () => {
  let service: SignUpServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SignUpServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
