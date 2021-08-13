import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MachineManagerApplicationSharedModule } from 'app/shared/shared.module';
import { ViewComponent } from './view.component';
import { ViewDetailComponent } from './view-detail.component';
import { ViewUpdateComponent } from './view-update.component';
import { ViewDeleteDialogComponent } from './view-delete-dialog.component';
import { viewRoute } from './view.route';

@NgModule({
  imports: [MachineManagerApplicationSharedModule, RouterModule.forChild(viewRoute)],
  declarations: [ViewComponent, ViewDetailComponent, ViewUpdateComponent, ViewDeleteDialogComponent],
  entryComponents: [ViewDeleteDialogComponent],
})
export class MachineManagerApplicationViewModule {}
