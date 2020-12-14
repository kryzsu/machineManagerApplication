import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { MachineUpdateComponent } from 'app/entities/machine/machine-update.component';
import { MachineService } from 'app/entities/machine/machine.service';
import { Machine } from 'app/shared/model/machine.model';

describe('Component Tests', () => {
  describe('Machine Management Update Component', () => {
    let comp: MachineUpdateComponent;
    let fixture: ComponentFixture<MachineUpdateComponent>;
    let service: MachineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [MachineUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MachineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MachineUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MachineService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Machine(123);
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
        const entity = new Machine();
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
