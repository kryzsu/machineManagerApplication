import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import * as R from 'ramda';
import * as dayjs from 'dayjs';

import { IJob, Job } from '../job.model';
import { JobService } from '../service/job.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { PerspectiveService } from '../../../perspective/perspective.service';
import { dateFitInterval, sortByNameCaseInsensitive, Interval } from '../../../util/common-util';
import { OutOfOrder } from '../../out-of-order/out-of-order.model';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  machinesSharedCollection: IMachine[] = [];
  customersSharedCollection: ICustomer[] = [];
  createDateTime?: dayjs.Dayjs | null;

  editForm = this.fb.group({
    id: [],
    estimation: [],
    productCount: [null, [Validators.required]],
    startDate: [],
    endDate: [],
    fact: [],
    orderNumber: [],
    drawingNumber: [],
    drawing: [],
    drawingContentType: [],
    worknumber: [null, [Validators.required]],
    products: [],
    machine: [],
    customer: [],
  });

  disabledIntervalList: Interval[] = [];

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected jobService: JobService,
    protected productService: ProductService,
    protected machineService: MachineService,
    protected customerService: CustomerService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected perspectiveService: PerspectiveService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);
      if (job.id !== undefined) {
        this.refreshMachineRelatedData();
      }
      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('machineManagerApplicationApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    job.createDateTime = this.createDateTime;
    // eslint-disable-next-line no-debugger
    debugger;

    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackMachineById(index: number, item: IMachine): number {
    return item.id!;
  }

  trackCustomerById(index: number, item: ICustomer): number {
    return item.id!;
  }

  getSelectedProduct(option: IProduct, selectedVals?: IProduct[]): IProduct {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  onMachineChange(): void {
    this.refreshMachineRelatedData();
  }

  refreshMachineRelatedData(): void {
    const machineId = this.editForm.get('machine')?.value?.id;
    const estimation = this.editForm.get('estimation')?.value;
    const startDate = this.editForm.get('startDate')?.value;

    if (machineId !== undefined) {
      if (R.isNil(startDate)) {
        this.perspectiveService
          .getNextDateForMachine(machineId, estimation)
          .pipe(map((response: HttpResponse<string>) => response.body ?? ''))
          .subscribe(newDate => {
            this.editForm.patchValue({ startDate: dayjs(newDate) });
          });
      }

      this.perspectiveService
        .getRelatedOutOfOrder(machineId)
        .pipe(map((response: HttpResponse<OutOfOrder[]>) => response.body ?? []))
        .subscribe((data: OutOfOrder[]) => {
          this.disabledIntervalList = data.map((item: OutOfOrder) => {
            const startDayjs = dayjs(item.start);
            const start: NgbDate = new NgbDate(startDayjs.year(), startDayjs.month() + 1, startDayjs.date());
            const endDayjs = dayjs(item.end);
            const end = new NgbDate(endDayjs.year(), endDayjs.month() + 1, endDayjs.date());

            return { start, end };
          });
        });
    }
  }

  isDisabled = (date: NgbDate): boolean => {
    const da = dayjs(`${date.year}-${date.month}-${date.day}`);
    const rv = da.day() === 0 || this.disabledIntervalList.some((interval: Interval) => dateFitInterval(interval, date));
    return rv;
  };

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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

  protected updateForm(job: IJob): void {
    this.createDateTime = job.createDateTime;
    // eslint-disable-next-line no-debugger
    debugger;

    this.editForm.patchValue({
      id: job.id,
      estimation: job.estimation,
      productCount: job.productCount,
      startDate: job.startDate,
      endDate: job.endDate,
      fact: job.fact,
      orderNumber: job.orderNumber,
      drawingNumber: job.drawingNumber,
      drawing: job.drawing,
      drawingContentType: job.drawingContentType,
      products: job.products,
      machine: job.machine,
      customer: job.customer,
      worknumber: job.worknumber,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      ...(job.products ?? [])
    );
    this.machinesSharedCollection = this.machineService.addMachineToCollectionIfMissing(this.machinesSharedCollection, job.machine);
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(this.customersSharedCollection, job.customer);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing(products, ...(this.editForm.get('products')!.value ?? []))
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.machineService
      .query()
      .pipe(map((res: HttpResponse<IMachine[]>) => res.body ?? []))
      .pipe(
        map((machines: IMachine[]) => this.machineService.addMachineToCollectionIfMissing(machines, this.editForm.get('machine')!.value))
      )
      .subscribe((machines: IMachine[]) => (this.machinesSharedCollection = machines));

    this.customerService
      .query({ size: 1000, sort: name })
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing(customers, this.editForm.get('customer')!.value)
        )
      )
      .subscribe((customers: ICustomer[]) => {
        this.customersSharedCollection = sortByNameCaseInsensitive(customers);
      });
  }

  protected createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      estimation: this.editForm.get(['estimation'])!.value,
      productCount: this.editForm.get(['productCount'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      fact: this.editForm.get(['fact'])!.value,
      orderNumber: this.editForm.get(['orderNumber'])!.value,
      drawingNumber: this.editForm.get(['drawingNumber'])!.value,
      drawingContentType: this.editForm.get(['drawingContentType'])!.value,
      drawing: this.editForm.get(['drawing'])!.value,
      worknumber: this.editForm.get(['worknumber'])!.value,
      products: this.editForm.get(['products'])!.value,
      machine: this.editForm.get(['machine'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
