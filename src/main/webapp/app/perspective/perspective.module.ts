import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

import { CalendarComponent } from './calendar/calendar.component';
import { perspectiveState } from './perspective.route';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(perspectiveState),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
  ],
  declarations: [CalendarComponent],
})
export class PerspectiveModule {}
