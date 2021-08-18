import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IView, View } from '../view.model';
import { ViewService } from '../service/view.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';

@Component({
  selector: 'jhi-view-update',
  templateUrl: './view-update.component.html',
})
export class ViewUpdateComponent implements OnInit {
  isSaving = false;

  machinesSharedCollection: IMachine[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    machines: [],
  });

  constructor(
    protected viewService: ViewService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ view }) => {
      this.updateForm(view);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const view = this.createFromForm();
    if (view.id !== undefined) {
      this.subscribeToSaveResponse(this.viewService.update(view));
    } else {
      this.subscribeToSaveResponse(this.viewService.create(view));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IView>>): void {
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

  protected updateForm(view: IView): void {
    this.editForm.patchValue({
      id: view.id,
      name: view.name,
      machines: view.machines,
    });

    this.machinesSharedCollection = this.machineService.addMachineToCollectionIfMissing(
      this.machinesSharedCollection,
      ...(view.machines ?? [])
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

  protected createFromForm(): IView {
    return {
      ...new View(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      machines: this.editForm.get(['machines'])!.value,
    };
  }
}
