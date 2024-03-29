jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductService } from '../service/product.service';
import { IProduct, Product } from '../product.model';
import { IRawmaterial } from 'app/entities/rawmaterial/rawmaterial.model';
import { RawmaterialService } from 'app/entities/rawmaterial/service/rawmaterial.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Component Tests', () => {
  describe('Product Management Update Component', () => {
    let comp: ProductUpdateComponent;
    let fixture: ComponentFixture<ProductUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productService: ProductService;
    let rawmaterialService: RawmaterialService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productService = TestBed.inject(ProductService);
      rawmaterialService = TestBed.inject(RawmaterialService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Rawmaterial query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const rawmaterial: IRawmaterial = { id: 78260 };
        product.rawmaterial = rawmaterial;

        const rawmaterialCollection: IRawmaterial[] = [{ id: 55800 }];
        jest.spyOn(rawmaterialService, 'query').mockReturnValue(of(new HttpResponse({ body: rawmaterialCollection })));
        const additionalRawmaterials = [rawmaterial];
        const expectedCollection: IRawmaterial[] = [...additionalRawmaterials, ...rawmaterialCollection];
        jest.spyOn(rawmaterialService, 'addRawmaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(rawmaterialService.query).toHaveBeenCalled();
        expect(rawmaterialService.addRawmaterialToCollectionIfMissing).toHaveBeenCalledWith(
          rawmaterialCollection,
          ...additionalRawmaterials
        );
        expect(comp.rawmaterialsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const product: IProduct = { id: 456 };
        const rawmaterial: IRawmaterial = { id: 20147 };
        product.rawmaterial = rawmaterial;

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(product));
        expect(comp.rawmaterialsSharedCollection).toContain(rawmaterial);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = { id: 123 };
        jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = new Product();
        jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(productService.create).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = { id: 123 };
        jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRawmaterialById', () => {
        it('Should return tracked Rawmaterial primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRawmaterialById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
