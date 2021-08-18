import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { ViewUpdateComponent } from 'app/entities/view/view-update.component';
import { ViewService } from 'app/entities/view/view.service';
import { View } from 'app/shared/model/view.model';

describe('Component Tests', () => {
  describe('View Management Update Component', () => {
    let comp: ViewUpdateComponent;
    let fixture: ComponentFixture<ViewUpdateComponent>;
    let service: ViewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [ViewUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ViewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ViewUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ViewService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new View(123);
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
        const entity = new View();
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
