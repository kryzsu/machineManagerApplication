import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMachine, Machine } from '../machine.model';
import { MachineService } from '../service/machine.service';

@Component({
  selector: 'jhi-machine-update',
  templateUrl: './machine-update.component.html',
})
export class MachineUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    createDateTime: [],
    updateDateTime: [],
    deleted: [],
  });

  constructor(protected machineService: MachineService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ machine }) => {
      if (machine.id === undefined) {
        const today = dayjs().startOf('day');
        machine.createDateTime = today;
        machine.updateDateTime = today;
      }

      this.updateForm(machine);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const machine = this.createFromForm();
    if (machine.id !== undefined) {
      this.subscribeToSaveResponse(this.machineService.update(machine));
    } else {
      this.subscribeToSaveResponse(this.machineService.create(machine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMachine>>): void {
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

  protected updateForm(machine: IMachine): void {
    this.editForm.patchValue({
      id: machine.id,
      name: machine.name,
      description: machine.description,
      createDateTime: machine.createDateTime ? machine.createDateTime.format(DATE_TIME_FORMAT) : null,
      updateDateTime: machine.updateDateTime ? machine.updateDateTime.format(DATE_TIME_FORMAT) : null,
      deleted: machine.deleted,
    });
  }

  protected createFromForm(): IMachine {
    return {
      ...new Machine(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      createDateTime: this.editForm.get(['createDateTime'])!.value
        ? dayjs(this.editForm.get(['createDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updateDateTime: this.editForm.get(['updateDateTime'])!.value
        ? dayjs(this.editForm.get(['updateDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
