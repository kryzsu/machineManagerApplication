import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMachine } from 'app/shared/model/machine.model';
import { MachineService } from './machine.service';

@Component({
  templateUrl: './machine-delete-dialog.component.html',
})
export class MachineDeleteDialogComponent {
  machine?: IMachine;

  constructor(protected machineService: MachineService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.machineService.delete(id).subscribe(() => {
      this.eventManager.broadcast('machineListModification');
      this.activeModal.close();
    });
  }
}
