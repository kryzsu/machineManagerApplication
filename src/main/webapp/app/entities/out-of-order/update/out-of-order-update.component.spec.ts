jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OutOfOrderService } from '../service/out-of-order.service';
import { IOutOfOrder, OutOfOrder } from '../out-of-order.model';
import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';

import { OutOfOrderUpdateComponent } from './out-of-order-update.component';

describe('Component Tests', () => {
  describe('OutOfOrder Management Update Component', () => {
    let comp: OutOfOrderUpdateComponent;
    let fixture: ComponentFixture<OutOfOrderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let outOfOrderService: OutOfOrderService;
    let machineService: MachineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OutOfOrderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OutOfOrderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OutOfOrderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      outOfOrderService = TestBed.inject(OutOfOrderService);
      machineService = TestBed.inject(MachineService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Machine query and add missing value', () => {
        const outOfOrder: IOutOfOrder = { id: 456 };
        const machines: IMachine[] = [{ id: 23302 }];
        outOfOrder.machines = machines;

        const machineCollection: IMachine[] = [{ id: 39083 }];
        jest.spyOn(machineService, 'query').mockReturnValue(of(new HttpResponse({ body: machineCollection })));
        const additionalMachines = [...machines];
        const expectedCollection: IMachine[] = [...additionalMachines, ...machineCollection];
        jest.spyOn(machineService, 'addMachineToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ outOfOrder });
        comp.ngOnInit();

        expect(machineService.query).toHaveBeenCalled();
        expect(machineService.addMachineToCollectionIfMissing).toHaveBeenCalledWith(machineCollection, ...additionalMachines);
        expect(comp.machinesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const outOfOrder: IOutOfOrder = { id: 456 };
        const machines: IMachine = { id: 63355 };
        outOfOrder.machines = [machines];

        activatedRoute.data = of({ outOfOrder });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(outOfOrder));
        expect(comp.machinesSharedCollection).toContain(machines);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OutOfOrder>>();
        const outOfOrder = { id: 123 };
        jest.spyOn(outOfOrderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ outOfOrder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: outOfOrder }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(outOfOrderService.update).toHaveBeenCalledWith(outOfOrder);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OutOfOrder>>();
        const outOfOrder = new OutOfOrder();
        jest.spyOn(outOfOrderService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ outOfOrder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: outOfOrder }));
        saveSubject.complete();

        // THEN
        expect(outOfOrderService.create).toHaveBeenCalledWith(outOfOrder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OutOfOrder>>();
        const outOfOrder = { id: 123 };
        jest.spyOn(outOfOrderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ outOfOrder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(outOfOrderService.update).toHaveBeenCalledWith(outOfOrder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMachineById', () => {
        it('Should return tracked Machine primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMachineById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedMachine', () => {
        it('Should return option if no Machine is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedMachine(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Machine for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedMachine(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Machine is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedMachine(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
