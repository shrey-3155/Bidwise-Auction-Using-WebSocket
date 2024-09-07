import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostLoginHeaderComponent } from './post-login-header.component';

describe('PostLoginHeaderComponent', () => {
  let component: PostLoginHeaderComponent;
  let fixture: ComponentFixture<PostLoginHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PostLoginHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostLoginHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
