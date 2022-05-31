import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { IRawmaterial } from 'app/entities/rawmaterial/rawmaterial.model';
import { RawmaterialService } from 'app/entities/rawmaterial/service/rawmaterial.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  rawmaterialsSharedCollection: IRawmaterial[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(5)]],
    comment: [],
    weight: [null, [Validators.required]],
    rawmaterial: [],
  });

  constructor(
    protected productService: ProductService,
    protected rawmaterialService: RawmaterialService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackRawmaterialById(index: number, item: IRawmaterial): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      name: product.name,
      comment: product.comment,
      weight: product.weight,
      rawmaterial: product.rawmaterial,
    });

    this.rawmaterialsSharedCollection = this.rawmaterialService.addRawmaterialToCollectionIfMissing(
      this.rawmaterialsSharedCollection,
      product.rawmaterial
    );
  }

  protected loadRelationshipsOptions(): void {
    this.rawmaterialService
      .query()
      .pipe(map((res: HttpResponse<IRawmaterial[]>) => res.body ?? []))
      .pipe(
        map((rawmaterials: IRawmaterial[]) =>
          this.rawmaterialService.addRawmaterialToCollectionIfMissing(rawmaterials, this.editForm.get('rawmaterial')!.value)
        )
      )
      .subscribe((rawmaterials: IRawmaterial[]) => (this.rawmaterialsSharedCollection = rawmaterials));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      rawmaterial: this.editForm.get(['rawmaterial'])!.value,
    };
  }
}
