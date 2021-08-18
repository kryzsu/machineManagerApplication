import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOutOfOrder } from '../out-of-order.model';
import { OutOfOrderService } from '../service/out-of-order.service';

@Component({
  templateUrl: './out-of-order-delete-dialog.component.html',
})
export class OutOfOrderDeleteDialogComponent {
  outOfOrder?: IOutOfOrder;

  constructor(protected outOfOrderService: OutOfOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.outOfOrderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
