import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOutOfOrder, OutOfOrder } from '../out-of-order.model';
import { OutOfOrderService } from '../service/out-of-order.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';

@Component({
  selector: 'jhi-out-of-order-update',
  templateUrl: './out-of-order-update.component.html',
})
export class OutOfOrderUpdateComponent implements OnInit {
  isSaving = false;

  machinesSharedCollection: IMachine[] = [];

  editForm = this.fb.group({
    id: [],
    start: [null, [Validators.required]],
    end: [null, [Validators.required]],
    description: [null, [Validators.required, Validators.minLength(5)]],
    machines: [],
  });

  constructor(
    protected outOfOrderService: OutOfOrderService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ outOfOrder }) => {
      this.updateForm(outOfOrder);

      this.loadRelationshipsOptions();
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

  trackMachineById(index: number, item: IMachine): number {
    return item.id!;
  }

  getSelectedMachine(option: IMachine, selectedVals?: IMachine[]): IMachine {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOutOfOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(outOfOrder: IOutOfOrder): void {
    this.editForm.patchValue({
      id: outOfOrder.id,
      start: outOfOrder.start,
      end: outOfOrder.end,
      description: outOfOrder.description,
      machines: outOfOrder.machines,
    });

    this.machinesSharedCollection = this.machineService.addMachineToCollectionIfMissing(
      this.machinesSharedCollection,
      ...(outOfOrder.machines ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.machineService
      .query()
      .pipe(map((res: HttpResponse<IMachine[]>) => res.body ?? []))
      .pipe(
        map((machines: IMachine[]) =>
          this.machineService.addMachineToCollectionIfMissing(machines, ...(this.editForm.get('machines')!.value ?? []))
        )
      )
      .subscribe((machines: IMachine[]) => (this.machinesSharedCollection = machines));
  }

  protected createFromForm(): IOutOfOrder {
    return {
      ...new OutOfOrder(),
      id: this.editForm.get(['id'])!.value,
      start: this.editForm.get(['start'])!.value,
      end: this.editForm.get(['end'])!.value,
      description: this.editForm.get(['description'])!.value,
      machines: this.editForm.get(['machines'])!.value,
    };
  }
}
