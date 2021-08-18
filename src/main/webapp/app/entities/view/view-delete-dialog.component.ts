import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IView } from 'app/shared/model/view.model';
import { ViewService } from './view.service';

@Component({
  templateUrl: './view-delete-dialog.component.html',
})
export class ViewDeleteDialogComponent {
  view?: IView;

  constructor(protected viewService: ViewService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.viewService.delete(id).subscribe(() => {
      this.eventManager.broadcast('viewListModification');
      this.activeModal.close();
    });
  }
}
