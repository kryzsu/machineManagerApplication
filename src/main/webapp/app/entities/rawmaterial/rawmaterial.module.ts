import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RawmaterialComponent } from './list/rawmaterial.component';
import { RawmaterialDetailComponent } from './detail/rawmaterial-detail.component';
import { RawmaterialUpdateComponent } from './update/rawmaterial-update.component';
import { RawmaterialDeleteDialogComponent } from './delete/rawmaterial-delete-dialog.component';
import { RawmaterialRoutingModule } from './route/rawmaterial-routing.module';

@NgModule({
  imports: [SharedModule, RawmaterialRoutingModule],
  declarations: [RawmaterialComponent, RawmaterialDetailComponent, RawmaterialUpdateComponent, RawmaterialDeleteDialogComponent],
  entryComponents: [RawmaterialDeleteDialogComponent],
})
export class RawmaterialModule {}
