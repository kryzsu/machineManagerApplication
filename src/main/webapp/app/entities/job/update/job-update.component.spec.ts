jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JobService } from '../service/job.service';
import { IJob, Job } from '../job.model';
import { IWorked } from 'app/entities/worked/worked.model';
import { WorkedService } from 'app/entities/worked/service/worked.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';

import { JobUpdateComponent } from './job-update.component';

describe('Component Tests', () => {
  describe('Job Management Update Component', () => {
    let comp: JobUpdateComponent;
    let fixture: ComponentFixture<JobUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let jobService: JobService;
    let workedService: WorkedService;
    let machineService: MachineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JobUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(JobUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      jobService = TestBed.inject(JobService);
      workedService = TestBed.inject(WorkedService);
      machineService = TestBed.inject(MachineService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Worked query and add missing value', () => {
        const job: IJob = { id: 456 };
        const workeds: IWorked[] = [{ id: 69892 }];
        job.workeds = workeds;

        const workedCollection: IWorked[] = [{ id: 66511 }];
        jest.spyOn(workedService, 'query').mockReturnValue(of(new HttpResponse({ body: workedCollection })));
        const additionalWorkeds = [...workeds];
        const expectedCollection: IWorked[] = [...additionalWorkeds, ...workedCollection];
        jest.spyOn(workedService, 'addWorkedToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ job });
        comp.ngOnInit();

        expect(workedService.query).toHaveBeenCalled();
        expect(workedService.addWorkedToCollectionIfMissing).toHaveBeenCalledWith(workedCollection, ...additionalWorkeds);
        expect(comp.workedsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Machine query and add missing value', () => {
        const job: IJob = { id: 456 };
        const machine: IMachine = { id: 77813 };
        job.machine = machine;

        const machineCollection: IMachine[] = [{ id: 75938 }];
        jest.spyOn(machineService, 'query').mockReturnValue(of(new HttpResponse({ body: machineCollection })));
        const additionalMachines = [machine];
        const expectedCollection: IMachine[] = [...additionalMachines, ...machineCollection];
        jest.spyOn(machineService, 'addMachineToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ job });
        comp.ngOnInit();

        expect(machineService.query).toHaveBeenCalled();
        expect(machineService.addMachineToCollectionIfMissing).toHaveBeenCalledWith(machineCollection, ...additionalMachines);
        expect(comp.machinesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const job: IJob = { id: 456 };
        const workeds: IWorked = { id: 22193 };
        job.workeds = [workeds];
        const machine: IMachine = { id: 30800 };
        job.machine = machine;

        activatedRoute.data = of({ job });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(job));
        expect(comp.workedsSharedCollection).toContain(workeds);
        expect(comp.machinesSharedCollection).toContain(machine);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Job>>();
        const job = { id: 123 };
        jest.spyOn(jobService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ job });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: job }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(jobService.update).toHaveBeenCalledWith(job);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Job>>();
        const job = new Job();
        jest.spyOn(jobService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ job });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: job }));
        saveSubject.complete();

        // THEN
        expect(jobService.create).toHaveBeenCalledWith(job);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Job>>();
        const job = { id: 123 };
        jest.spyOn(jobService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ job });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(jobService.update).toHaveBeenCalledWith(job);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackWorkedById', () => {
        it('Should return tracked Worked primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWorkedById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMachineById', () => {
        it('Should return tracked Machine primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMachineById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedWorked', () => {
        it('Should return option if no Worked is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedWorked(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Worked for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedWorked(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Worked is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedWorked(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
