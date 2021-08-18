import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { TreeModule } from 'primeng/tree';
import { SharedModule } from 'app/shared/shared.module';

import { CalendarComponent } from './calendar/calendar.component';
import { perspectiveState } from './perspective.route';
import { PerpsTimelineComponent } from './component/perps-timeline/perps-timeline.component';
import { PerpsTreeComponent } from './component/perps-tree/perps-tree.component';

@NgModule({
  imports: [
    SharedModule,
    CommonModule,
    RouterModule.forChild(perspectiveState),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    DragDropModule,
    TreeModule,
  ],
  declarations: [CalendarComponent, PerpsTimelineComponent, PerpsTreeComponent],
})
export class PerspectiveModule {}
