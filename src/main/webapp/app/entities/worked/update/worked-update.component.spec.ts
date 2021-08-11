jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkedService } from '../service/worked.service';
import { IWorked, Worked } from '../worked.model';

import { WorkedUpdateComponent } from './worked-update.component';

describe('Component Tests', () => {
  describe('Worked Management Update Component', () => {
    let comp: WorkedUpdateComponent;
    let fixture: ComponentFixture<WorkedUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workedService: WorkedService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkedUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkedUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkedUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workedService = TestBed.inject(WorkedService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const worked: IWorked = { id: 456 };

        activatedRoute.data = of({ worked });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(worked));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Worked>>();
        const worked = { id: 123 };
        jest.spyOn(workedService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ worked });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: worked }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workedService.update).toHaveBeenCalledWith(worked);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Worked>>();
        const worked = new Worked();
        jest.spyOn(workedService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ worked });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: worked }));
        saveSubject.complete();

        // THEN
        expect(workedService.create).toHaveBeenCalledWith(worked);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Worked>>();
        const worked = { id: 123 };
        jest.spyOn(workedService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ worked });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workedService.update).toHaveBeenCalledWith(worked);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
