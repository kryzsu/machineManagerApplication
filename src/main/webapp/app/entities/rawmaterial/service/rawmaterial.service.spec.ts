import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRawmaterial, Rawmaterial } from '../rawmaterial.model';

import { RawmaterialService } from './rawmaterial.service';

describe('Service Tests', () => {
  describe('Rawmaterial Service', () => {
    let service: RawmaterialService;
    let httpMock: HttpTestingController;
    let elemDefault: IRawmaterial;
    let expectedResult: IRawmaterial | IRawmaterial[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RawmaterialService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        comment: 'AAAAAAA',
        grade: 'AAAAAAA',
        dimension: 'AAAAAAA',
        coating: 'AAAAAAA',
        supplier: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Rawmaterial', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Rawmaterial()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Rawmaterial', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            comment: 'BBBBBB',
            grade: 'BBBBBB',
            dimension: 'BBBBBB',
            coating: 'BBBBBB',
            supplier: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Rawmaterial', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Rawmaterial()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Rawmaterial', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            comment: 'BBBBBB',
            grade: 'BBBBBB',
            dimension: 'BBBBBB',
            coating: 'BBBBBB',
            supplier: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Rawmaterial', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRawmaterialToCollectionIfMissing', () => {
        it('should add a Rawmaterial to an empty array', () => {
          const rawmaterial: IRawmaterial = { id: 123 };
          expectedResult = service.addRawmaterialToCollectionIfMissing([], rawmaterial);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(rawmaterial);
        });

        it('should not add a Rawmaterial to an array that contains it', () => {
          const rawmaterial: IRawmaterial = { id: 123 };
          const rawmaterialCollection: IRawmaterial[] = [
            {
              ...rawmaterial,
            },
            { id: 456 },
          ];
          expectedResult = service.addRawmaterialToCollectionIfMissing(rawmaterialCollection, rawmaterial);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Rawmaterial to an array that doesn't contain it", () => {
          const rawmaterial: IRawmaterial = { id: 123 };
          const rawmaterialCollection: IRawmaterial[] = [{ id: 456 }];
          expectedResult = service.addRawmaterialToCollectionIfMissing(rawmaterialCollection, rawmaterial);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(rawmaterial);
        });

        it('should add only unique Rawmaterial to an array', () => {
          const rawmaterialArray: IRawmaterial[] = [{ id: 123 }, { id: 456 }, { id: 39883 }];
          const rawmaterialCollection: IRawmaterial[] = [{ id: 123 }];
          expectedResult = service.addRawmaterialToCollectionIfMissing(rawmaterialCollection, ...rawmaterialArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const rawmaterial: IRawmaterial = { id: 123 };
          const rawmaterial2: IRawmaterial = { id: 456 };
          expectedResult = service.addRawmaterialToCollectionIfMissing([], rawmaterial, rawmaterial2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(rawmaterial);
          expect(expectedResult).toContain(rawmaterial2);
        });

        it('should accept null and undefined values', () => {
          const rawmaterial: IRawmaterial = { id: 123 };
          expectedResult = service.addRawmaterialToCollectionIfMissing([], null, rawmaterial, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(rawmaterial);
        });

        it('should return initial array if no Rawmaterial is added', () => {
          const rawmaterialCollection: IRawmaterial[] = [{ id: 123 }];
          expectedResult = service.addRawmaterialToCollectionIfMissing(rawmaterialCollection, undefined, null);
          expect(expectedResult).toEqual(rawmaterialCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
