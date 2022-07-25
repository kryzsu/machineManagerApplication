import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { TreeModule } from 'primeng/tree';
import { ContextMenuModule } from 'primeng/contextmenu';

import { SharedModule } from 'app/shared/shared.module';
import { CalendarComponent } from './calendar/calendar.component';
import { perspectiveState } from './perspective.route';
import { PerpsTimelineComponent } from './component/perps-timeline/perps-timeline.component';
import { PerpsTreeComponent } from './component/perps-tree/perps-tree.component';
import { AccordionModule } from 'primeng/accordion';
import { OrderListModule } from 'primeng/orderlist';
import { DropdownModule } from 'primeng/dropdown';
import { IntervalFilterComponent } from './component/interval-filter/interval-filter.component';
import { MachineDetailsComponent } from './component/machine-details/machine-details.component';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { DataViewModule } from 'primeng/dataview';

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
    AccordionModule,
    ContextMenuModule,
    OrderListModule,
    DropdownModule,
    ButtonModule,
    CardModule,
    TagModule,
    DataViewModule,
  ],
  declarations: [CalendarComponent, PerpsTimelineComponent, PerpsTreeComponent, IntervalFilterComponent, MachineDetailsComponent],
})
export class PerspectiveModule {}
