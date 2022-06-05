import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRawmaterial, Rawmaterial } from '../rawmaterial.model';
import { RawmaterialService } from '../service/rawmaterial.service';

@Component({
  selector: 'jhi-rawmaterial-update',
  templateUrl: './rawmaterial-update.component.html',
})
export class RawmaterialUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(3)]],
    comment: [],
    grade: [null, [Validators.required]],
    dimension: [null, [Validators.required]],
    coating: [null, [Validators.required]],
    supplier: [null, [Validators.required]],
  });

  constructor(protected rawmaterialService: RawmaterialService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rawmaterial }) => {
      this.updateForm(rawmaterial);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rawmaterial = this.createFromForm();
    if (rawmaterial.id !== undefined) {
      this.subscribeToSaveResponse(this.rawmaterialService.update(rawmaterial));
    } else {
      this.subscribeToSaveResponse(this.rawmaterialService.create(rawmaterial));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRawmaterial>>): void {
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

  protected updateForm(rawmaterial: IRawmaterial): void {
    this.editForm.patchValue({
      id: rawmaterial.id,
      name: rawmaterial.name,
      comment: rawmaterial.comment,
      grade: rawmaterial.grade,
      dimension: rawmaterial.dimension,
      coating: rawmaterial.coating,
      supplier: rawmaterial.supplier,
    });
  }

  protected createFromForm(): IRawmaterial {
    return {
      ...new Rawmaterial(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      dimension: this.editForm.get(['dimension'])!.value,
      coating: this.editForm.get(['coating'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
    };
  }
}
