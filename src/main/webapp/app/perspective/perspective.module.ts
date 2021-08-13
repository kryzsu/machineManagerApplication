import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { perspectiveState } from './perspective.route';

@NgModule({
  imports: [CommonModule, RouterModule.forChild(perspectiveState)],
  declarations: [CalendarComponent],
})
export class PerspectiveModule {}
