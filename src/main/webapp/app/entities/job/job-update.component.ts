import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJob, Job } from 'app/shared/model/job.model';
import { JobService } from './job.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { IMachine } from 'app/shared/model/machine.model';
import { MachineService } from 'app/entities/machine/machine.service';

type SelectableEntity = IProduct | IMachine;

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;
  products: IProduct[] = [];
  machines: IMachine[] = [];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    estimation: [],
    productCount: [null, [Validators.required]],
    startDate: [],
    endDate: [],
    fact: [],
    orderNumber: [],
    products: [],
    machineId: [],
  });

  constructor(
    protected jobService: JobService,
    protected productService: ProductService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));

      this.machineService.query().subscribe((res: HttpResponse<IMachine[]>) => (this.machines = res.body || []));
    });
  }

  updateForm(job: IJob): void {
    this.editForm.patchValue({
      id: job.id,
      estimation: job.estimation,
      productCount: job.productCount,
      startDate: job.startDate,
      endDate: job.endDate,
      fact: job.fact,
      orderNumber: job.orderNumber,
      products: job.products,
      machineId: job.machineId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  private createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      estimation: this.editForm.get(['estimation'])!.value,
      productCount: this.editForm.get(['productCount'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      fact: this.editForm.get(['fact'])!.value,
      orderNumber: this.editForm.get(['orderNumber'])!.value,
      products: this.editForm.get(['products'])!.value,
      machineId: this.editForm.get(['machineId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IProduct[], option: IProduct): IProduct {
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
