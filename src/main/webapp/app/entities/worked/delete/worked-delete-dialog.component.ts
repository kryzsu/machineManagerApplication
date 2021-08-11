import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorked } from '../worked.model';
import { WorkedService } from '../service/worked.service';

@Component({
  templateUrl: './worked-delete-dialog.component.html',
})
export class WorkedDeleteDialogComponent {
  worked?: IWorked;

  constructor(protected workedService: WorkedService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workedService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
