import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputComponent } from './input/input.component';
import { FooterComponent } from './footer/footer.component';
import { HeroComponent } from './hero/hero.component';
import { PageBodyHeaderComponent } from './page-body-header/page-body-header.component';



@NgModule({
  declarations: [
    InputComponent,
    PageBodyHeaderComponent,
  ],
  imports: [
    CommonModule
  ],
})
export class InputModule { }
