jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MachineService } from '../service/machine.service';
import { IMachine, Machine } from '../machine.model';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';

import { MachineUpdateComponent } from './machine-update.component';

describe('Component Tests', () => {
  describe('Machine Management Update Component', () => {
    let comp: MachineUpdateComponent;
    let fixture: ComponentFixture<MachineUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let machineService: MachineService;
    let jobService: JobService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MachineUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MachineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MachineUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      machineService = TestBed.inject(MachineService);
      jobService = TestBed.inject(JobService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call runningJob query and add missing value', () => {
        const machine: IMachine = { id: 456 };
        const runningJob: IJob = { id: 2897 };
        machine.runningJob = runningJob;

        const runningJobCollection: IJob[] = [{ id: 66558 }];
        jest.spyOn(jobService, 'query').mockReturnValue(of(new HttpResponse({ body: runningJobCollection })));
        const expectedCollection: IJob[] = [runningJob, ...runningJobCollection];
        jest.spyOn(jobService, 'addJobToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        expect(jobService.query).toHaveBeenCalled();
        expect(jobService.addJobToCollectionIfMissing).toHaveBeenCalledWith(runningJobCollection, runningJob);
        expect(comp.runningJobsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const machine: IMachine = { id: 456 };
        const runningJob: IJob = { id: 16412 };
        machine.runningJob = runningJob;

        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(machine));
        expect(comp.runningJobsCollection).toContain(runningJob);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Machine>>();
        const machine = { id: 123 };
        jest.spyOn(machineService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: machine }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(machineService.update).toHaveBeenCalledWith(machine);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Machine>>();
        const machine = new Machine();
        jest.spyOn(machineService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: machine }));
        saveSubject.complete();

        // THEN
        expect(machineService.create).toHaveBeenCalledWith(machine);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Machine>>();
        const machine = { id: 123 };
        jest.spyOn(machineService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(machineService.update).toHaveBeenCalledWith(machine);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackJobById', () => {
        it('Should return tracked Job primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackJobById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
