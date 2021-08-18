import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IOutOfOrder, OutOfOrder } from 'app/shared/model/out-of-order.model';
import { OutOfOrderService } from './out-of-order.service';
import { IMachine } from 'app/shared/model/machine.model';
import { MachineService } from 'app/entities/machine/machine.service';

@Component({
  selector: 'jhi-out-of-order-update',
  templateUrl: './out-of-order-update.component.html',
})
export class OutOfOrderUpdateComponent implements OnInit {
  isSaving = false;
  machines: IMachine[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    description: [null, [Validators.required, Validators.minLength(5)]],
    machines: [],
  });

  constructor(
    protected outOfOrderService: OutOfOrderService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ outOfOrder }) => {
      this.updateForm(outOfOrder);

      this.machineService.query().subscribe((res: HttpResponse<IMachine[]>) => (this.machines = res.body || []));
    });
  }

  updateForm(outOfOrder: IOutOfOrder): void {
    this.editForm.patchValue({
      id: outOfOrder.id,
      date: outOfOrder.date,
      description: outOfOrder.description,
      machines: outOfOrder.machines,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const outOfOrder = this.createFromForm();
    if (outOfOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.outOfOrderService.update(outOfOrder));
    } else {
      this.subscribeToSaveResponse(this.outOfOrderService.create(outOfOrder));
    }
  }

  private createFromForm(): IOutOfOrder {
    return {
      ...new OutOfOrder(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      description: this.editForm.get(['description'])!.value,
      machines: this.editForm.get(['machines'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOutOfOrder>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IMachine): any {
    return item.id;
  }

  getSelected(selectedVals: IMachine[], option: IMachine): IMachine {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
