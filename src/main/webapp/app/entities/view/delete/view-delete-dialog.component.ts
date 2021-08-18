import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IView } from '../view.model';
import { ViewService } from '../service/view.service';

@Component({
  templateUrl: './view-delete-dialog.component.html',
})
export class ViewDeleteDialogComponent {
  view?: IView;

  constructor(protected viewService: ViewService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.viewService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
