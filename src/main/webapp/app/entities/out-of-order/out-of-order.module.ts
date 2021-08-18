import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MachineManagerApplicationSharedModule } from 'app/shared/shared.module';
import { OutOfOrderComponent } from './out-of-order.component';
import { OutOfOrderDetailComponent } from './out-of-order-detail.component';
import { OutOfOrderUpdateComponent } from './out-of-order-update.component';
import { OutOfOrderDeleteDialogComponent } from './out-of-order-delete-dialog.component';
import { outOfOrderRoute } from './out-of-order.route';

@NgModule({
  imports: [MachineManagerApplicationSharedModule, RouterModule.forChild(outOfOrderRoute)],
  declarations: [OutOfOrderComponent, OutOfOrderDetailComponent, OutOfOrderUpdateComponent, OutOfOrderDeleteDialogComponent],
  entryComponents: [OutOfOrderDeleteDialogComponent],
})
export class MachineManagerApplicationOutOfOrderModule {}
