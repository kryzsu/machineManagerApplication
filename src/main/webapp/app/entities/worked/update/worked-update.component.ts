import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWorked, Worked } from '../worked.model';
import { WorkedService } from '../service/worked.service';

@Component({
  selector: 'jhi-worked-update',
  templateUrl: './worked-update.component.html',
})
export class WorkedUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    day: [],
    comment: [],
  });

  constructor(protected workedService: WorkedService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ worked }) => {
      this.updateForm(worked);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const worked = this.createFromForm();
    if (worked.id !== undefined) {
      this.subscribeToSaveResponse(this.workedService.update(worked));
    } else {
      this.subscribeToSaveResponse(this.workedService.create(worked));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorked>>): void {
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

  protected updateForm(worked: IWorked): void {
    this.editForm.patchValue({
      id: worked.id,
      day: worked.day,
      comment: worked.comment,
    });
  }

  protected createFromForm(): IWorked {
    return {
      ...new Worked(),
      id: this.editForm.get(['id'])!.value,
      day: this.editForm.get(['day'])!.value,
      comment: this.editForm.get(['comment'])!.value,
    };
  }
}
