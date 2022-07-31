jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RawmaterialService } from '../service/rawmaterial.service';
import { IRawmaterial, Rawmaterial } from '../rawmaterial.model';

import { RawmaterialUpdateComponent } from './rawmaterial-update.component';

describe('Component Tests', () => {
  describe('Rawmaterial Management Update Component', () => {
    let comp: RawmaterialUpdateComponent;
    let fixture: ComponentFixture<RawmaterialUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let rawmaterialService: RawmaterialService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RawmaterialUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RawmaterialUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RawmaterialUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      rawmaterialService = TestBed.inject(RawmaterialService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const rawmaterial: IRawmaterial = { id: 456 };

        activatedRoute.data = of({ rawmaterial });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(rawmaterial));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Rawmaterial>>();
        const rawmaterial = { id: 123 };
        jest.spyOn(rawmaterialService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rawmaterial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rawmaterial }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(rawmaterialService.update).toHaveBeenCalledWith(rawmaterial);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Rawmaterial>>();
        const rawmaterial = new Rawmaterial();
        jest.spyOn(rawmaterialService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rawmaterial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rawmaterial }));
        saveSubject.complete();

        // THEN
        expect(rawmaterialService.create).toHaveBeenCalledWith(rawmaterial);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Rawmaterial>>();
        const rawmaterial = { id: 123 };
        jest.spyOn(rawmaterialService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rawmaterial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(rawmaterialService.update).toHaveBeenCalledWith(rawmaterial);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
