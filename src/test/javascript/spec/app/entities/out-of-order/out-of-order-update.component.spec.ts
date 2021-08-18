import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { OutOfOrderUpdateComponent } from 'app/entities/out-of-order/out-of-order-update.component';
import { OutOfOrderService } from 'app/entities/out-of-order/out-of-order.service';
import { OutOfOrder } from 'app/shared/model/out-of-order.model';

describe('Component Tests', () => {
  describe('OutOfOrder Management Update Component', () => {
    let comp: OutOfOrderUpdateComponent;
    let fixture: ComponentFixture<OutOfOrderUpdateComponent>;
    let service: OutOfOrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [OutOfOrderUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(OutOfOrderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OutOfOrderUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OutOfOrderService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new OutOfOrder(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new OutOfOrder();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
