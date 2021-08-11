import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMachine } from '../machine.model';
import { MachineService } from '../service/machine.service';
import { MachineDeleteDialogComponent } from '../delete/machine-delete-dialog.component';

@Component({
  selector: 'jhi-machine',
  templateUrl: './machine.component.html',
})
export class MachineComponent implements OnInit {
  machines?: IMachine[];
  isLoading = false;

  constructor(protected machineService: MachineService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.machineService.query().subscribe(
      (res: HttpResponse<IMachine[]>) => {
        this.isLoading = false;
        this.machines = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMachine): number {
    return item.id!;
  }

  delete(machine: IMachine): void {
    const modalRef = this.modalService.open(MachineDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.machine = machine;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
