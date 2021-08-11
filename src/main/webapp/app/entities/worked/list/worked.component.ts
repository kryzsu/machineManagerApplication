import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorked } from '../worked.model';
import { WorkedService } from '../service/worked.service';
import { WorkedDeleteDialogComponent } from '../delete/worked-delete-dialog.component';

@Component({
  selector: 'jhi-worked',
  templateUrl: './worked.component.html',
})
export class WorkedComponent implements OnInit {
  workeds?: IWorked[];
  isLoading = false;

  constructor(protected workedService: WorkedService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.workedService.query().subscribe(
      (res: HttpResponse<IWorked[]>) => {
        this.isLoading = false;
        this.workeds = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWorked): number {
    return item.id!;
  }

  delete(worked: IWorked): void {
    const modalRef = this.modalService.open(WorkedDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.worked = worked;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
