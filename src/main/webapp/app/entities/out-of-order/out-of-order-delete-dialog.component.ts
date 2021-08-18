import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOutOfOrder } from 'app/shared/model/out-of-order.model';
import { OutOfOrderService } from './out-of-order.service';

@Component({
  templateUrl: './out-of-order-delete-dialog.component.html',
})
export class OutOfOrderDeleteDialogComponent {
  outOfOrder?: IOutOfOrder;

  constructor(
    protected outOfOrderService: OutOfOrderService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.outOfOrderService.delete(id).subscribe(() => {
      this.eventManager.broadcast('outOfOrderListModification');
      this.activeModal.close();
    });
  }
}
