import { TestBed } from '@angular/core/testing';

import { PostLoginLandingPageServiceService } from './post-login-landing-page-service.service';

describe('PostLoginLandingPageServiceService', () => {
  let service: PostLoginLandingPageServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostLoginLandingPageServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
