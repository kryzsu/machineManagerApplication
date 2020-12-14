import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMachine } from 'app/shared/model/machine.model';
import { MachineService } from './machine.service';
import { MachineDeleteDialogComponent } from './machine-delete-dialog.component';

@Component({
  selector: 'jhi-machine',
  templateUrl: './machine.component.html',
})
export class MachineComponent implements OnInit, OnDestroy {
  machines?: IMachine[];
  eventSubscriber?: Subscription;

  constructor(protected machineService: MachineService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.machineService.query().subscribe((res: HttpResponse<IMachine[]>) => (this.machines = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMachines();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMachine): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMachines(): void {
    this.eventSubscriber = this.eventManager.subscribe('machineListModification', () => this.loadAll());
  }

  delete(machine: IMachine): void {
    const modalRef = this.modalService.open(MachineDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.machine = machine;
  }
}
