import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkedComponent } from './list/worked.component';
import { WorkedDetailComponent } from './detail/worked-detail.component';
import { WorkedUpdateComponent } from './update/worked-update.component';
import { WorkedDeleteDialogComponent } from './delete/worked-delete-dialog.component';
import { WorkedRoutingModule } from './route/worked-routing.module';

@NgModule({
  imports: [SharedModule, WorkedRoutingModule],
  declarations: [WorkedComponent, WorkedDetailComponent, WorkedUpdateComponent, WorkedDeleteDialogComponent],
  entryComponents: [WorkedDeleteDialogComponent],
})
export class WorkedModule {}
