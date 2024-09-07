import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageBodyHeaderComponent } from './page-body-header.component';

describe('PageBodyHeaderComponent', () => {
  let component: PageBodyHeaderComponent;
  let fixture: ComponentFixture<PageBodyHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PageBodyHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PageBodyHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
