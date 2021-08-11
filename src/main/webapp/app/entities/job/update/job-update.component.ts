import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IJob, Job } from '../job.model';
import { JobService } from '../service/job.service';
import { IWorked } from 'app/entities/worked/worked.model';
import { WorkedService } from 'app/entities/worked/service/worked.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;

  workedsSharedCollection: IWorked[] = [];
  machinesSharedCollection: IMachine[] = [];

  editForm = this.fb.group({
    id: [],
    customerName: [null, [Validators.required]],
    days: [null, [Validators.required]],
    productName: [null, [Validators.required]],
    count: [null, [Validators.required]],
    productType: [],
    comment: [],
    createDateTime: [],
    updateDateTime: [],
    deleted: [],
    inProgress: [],
    daysDone: [],
    workeds: [],
    machine: [],
  });

  constructor(
    protected jobService: JobService,
    protected workedService: WorkedService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      if (job.id === undefined) {
        const today = dayjs().startOf('day');
        job.createDateTime = today;
        job.updateDateTime = today;
      }

      this.updateForm(job);

      this.loadRelationshipsOptions();
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

  trackWorkedById(index: number, item: IWorked): number {
    return item.id!;
  }

  trackMachineById(index: number, item: IMachine): number {
    return item.id!;
  }

  getSelectedWorked(option: IWorked, selectedVals?: IWorked[]): IWorked {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

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
    this.editForm.patchValue({
      id: job.id,
      customerName: job.customerName,
      days: job.days,
      productName: job.productName,
      count: job.count,
      productType: job.productType,
      comment: job.comment,
      createDateTime: job.createDateTime ? job.createDateTime.format(DATE_TIME_FORMAT) : null,
      updateDateTime: job.updateDateTime ? job.updateDateTime.format(DATE_TIME_FORMAT) : null,
      deleted: job.deleted,
      inProgress: job.inProgress,
      daysDone: job.daysDone,
      workeds: job.workeds,
      machine: job.machine,
    });

    this.workedsSharedCollection = this.workedService.addWorkedToCollectionIfMissing(this.workedsSharedCollection, ...(job.workeds ?? []));
    this.machinesSharedCollection = this.machineService.addMachineToCollectionIfMissing(this.machinesSharedCollection, job.machine);
  }

  protected loadRelationshipsOptions(): void {
    this.workedService
      .query()
      .pipe(map((res: HttpResponse<IWorked[]>) => res.body ?? []))
      .pipe(
        map((workeds: IWorked[]) =>
          this.workedService.addWorkedToCollectionIfMissing(workeds, ...(this.editForm.get('workeds')!.value ?? []))
        )
      )
      .subscribe((workeds: IWorked[]) => (this.workedsSharedCollection = workeds));

    this.machineService
      .query()
      .pipe(map((res: HttpResponse<IMachine[]>) => res.body ?? []))
      .pipe(
        map((machines: IMachine[]) => this.machineService.addMachineToCollectionIfMissing(machines, this.editForm.get('machine')!.value))
      )
      .subscribe((machines: IMachine[]) => (this.machinesSharedCollection = machines));
  }

  protected createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      customerName: this.editForm.get(['customerName'])!.value,
      days: this.editForm.get(['days'])!.value,
      productName: this.editForm.get(['productName'])!.value,
      count: this.editForm.get(['count'])!.value,
      productType: this.editForm.get(['productType'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createDateTime: this.editForm.get(['createDateTime'])!.value
        ? dayjs(this.editForm.get(['createDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updateDateTime: this.editForm.get(['updateDateTime'])!.value
        ? dayjs(this.editForm.get(['updateDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      deleted: this.editForm.get(['deleted'])!.value,
      inProgress: this.editForm.get(['inProgress'])!.value,
      daysDone: this.editForm.get(['daysDone'])!.value,
      workeds: this.editForm.get(['workeds'])!.value,
      machine: this.editForm.get(['machine'])!.value,
    };
  }
}
