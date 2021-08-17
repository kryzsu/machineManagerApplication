import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { CalendarComponent } from './calendar/calendar.component';
import { perspectiveState } from './perspective.route';
import { MachineManagerApplicationSharedModule } from 'app/shared/shared.module';
import { PerpsTimelineComponent } from './perps-timeline/perps-timeline.component';

@NgModule({
  imports: [
    CommonModule,
    MachineManagerApplicationSharedModule,
    RouterModule.forChild(perspectiveState),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    DragDropModule,
  ],
  declarations: [CalendarComponent, PerpsTimelineComponent],
})
export class PerspectiveModule {}
