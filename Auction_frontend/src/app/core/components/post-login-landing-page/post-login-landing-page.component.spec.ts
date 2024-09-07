import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostLoginLandingPageComponent } from './post-login-landing-page.component';

describe('PostLoginLandingPageComponent', () => {
  let component: PostLoginLandingPageComponent;
  let fixture: ComponentFixture<PostLoginLandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PostLoginLandingPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostLoginLandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
