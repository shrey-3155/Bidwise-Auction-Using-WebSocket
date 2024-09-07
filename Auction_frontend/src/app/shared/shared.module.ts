import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PreLoginHeaderComponent } from './UI/pre-login-header/pre-login-header.component';
import { LogoHeaderComponent } from './UI/logo-header/logo-header.component';
import { PostLoginHeaderComponent } from './UI/post-login-header/post-login-header.component';
import { FooterComponent } from './UI/footer/footer.component';
import { HeroComponent } from './UI/hero/hero.component';



@NgModule({
  declarations: [
    PreLoginHeaderComponent,
    LogoHeaderComponent,
    PostLoginHeaderComponent,
    FooterComponent,
    HeroComponent
  ],
 
  imports: [
    CommonModule
  ],
  exports:[
    PreLoginHeaderComponent,
    LogoHeaderComponent,
    PostLoginHeaderComponent,
    FooterComponent,
    HeroComponent
  ]
})
export class SharedModule { }
