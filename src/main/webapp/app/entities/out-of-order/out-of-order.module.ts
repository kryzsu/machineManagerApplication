import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OutOfOrderComponent } from './list/out-of-order.component';
import { OutOfOrderDetailComponent } from './detail/out-of-order-detail.component';
import { OutOfOrderUpdateComponent } from './update/out-of-order-update.component';
import { OutOfOrderDeleteDialogComponent } from './delete/out-of-order-delete-dialog.component';
import { OutOfOrderRoutingModule } from './route/out-of-order-routing.module';

@NgModule({
  imports: [SharedModule, OutOfOrderRoutingModule],
  declarations: [OutOfOrderComponent, OutOfOrderDetailComponent, OutOfOrderUpdateComponent, OutOfOrderDeleteDialogComponent],
  entryComponents: [OutOfOrderDeleteDialogComponent],
})
export class OutOfOrderModule {}
