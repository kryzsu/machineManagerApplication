import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ViewComponent } from './list/view.component';
import { ViewDetailComponent } from './detail/view-detail.component';
import { ViewUpdateComponent } from './update/view-update.component';
import { ViewDeleteDialogComponent } from './delete/view-delete-dialog.component';
import { ViewRoutingModule } from './route/view-routing.module';

@NgModule({
  imports: [SharedModule, ViewRoutingModule],
  declarations: [ViewComponent, ViewDetailComponent, ViewUpdateComponent, ViewDeleteDialogComponent],
  entryComponents: [ViewDeleteDialogComponent],
})
export class ViewModule {}
