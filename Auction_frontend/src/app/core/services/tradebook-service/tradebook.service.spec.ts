import { TestBed } from '@angular/core/testing';

import { TradebookService } from './tradebook.service';

describe('TradebookService', () => {
  let service: TradebookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TradebookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
