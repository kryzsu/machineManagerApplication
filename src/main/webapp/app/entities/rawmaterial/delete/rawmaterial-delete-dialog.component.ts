import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRawmaterial } from '../rawmaterial.model';
import { RawmaterialService } from '../service/rawmaterial.service';

@Component({
  templateUrl: './rawmaterial-delete-dialog.component.html',
})
export class RawmaterialDeleteDialogComponent {
  rawmaterial?: IRawmaterial;

  constructor(protected rawmaterialService: RawmaterialService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rawmaterialService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
