import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IView, View } from 'app/shared/model/view.model';
import { ViewService } from './view.service';
import { IMachine } from 'app/shared/model/machine.model';
import { MachineService } from 'app/entities/machine/machine.service';

@Component({
  selector: 'jhi-view-update',
  templateUrl: './view-update.component.html',
})
export class ViewUpdateComponent implements OnInit {
  isSaving = false;
  machines: IMachine[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    machines: [],
  });

  constructor(
    protected viewService: ViewService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ view }) => {
      this.updateForm(view);

      this.machineService.query().subscribe((res: HttpResponse<IMachine[]>) => (this.machines = res.body || []));
    });
  }

  updateForm(view: IView): void {
    this.editForm.patchValue({
      id: view.id,
      name: view.name,
      machines: view.machines,
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

  private createFromForm(): IView {
    return {
      ...new View(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      machines: this.editForm.get(['machines'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IView>>): void {
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
