jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JobService } from '../service/job.service';
import { IJob, Job } from '../job.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';

import { JobUpdateComponent } from './job-update.component';

describe('Component Tests', () => {
  describe('Job Management Update Component', () => {
    let comp: JobUpdateComponent;
    let fixture: ComponentFixture<JobUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let jobService: JobService;
    let productService: ProductService;
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
      productService = TestBed.inject(ProductService);
      machineService = TestBed.inject(MachineService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Product query and add missing value', () => {
        const job: IJob = { id: 456 };
        const products: IProduct[] = [{ id: 69718 }];
        job.products = products;

        const productCollection: IProduct[] = [{ id: 79610 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [...products];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ job });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
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
        const products: IProduct = { id: 63338 };
        job.products = [products];
        const machine: IMachine = { id: 30800 };
        job.machine = machine;

        activatedRoute.data = of({ job });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(job));
        expect(comp.productsSharedCollection).toContain(products);
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
      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
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
      describe('getSelectedProduct', () => {
        it('Should return option if no Product is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedProduct(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Product for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedProduct(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Product is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedProduct(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
