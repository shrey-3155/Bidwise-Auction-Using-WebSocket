import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreLoginHeaderComponent } from './pre-login-header.component';

describe('PreLoginHeaderComponent', () => {
  let component: PreLoginHeaderComponent;
  let fixture: ComponentFixture<PreLoginHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PreLoginHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PreLoginHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
