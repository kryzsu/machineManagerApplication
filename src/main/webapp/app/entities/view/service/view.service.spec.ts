import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IView, View } from '../view.model';

import { ViewService } from './view.service';

describe('Service Tests', () => {
  describe('View Service', () => {
    let service: ViewService;
    let httpMock: HttpTestingController;
    let elemDefault: IView;
    let expectedResult: IView | IView[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ViewService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a View', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new View()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a View', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a View', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new View()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of View', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a View', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addViewToCollectionIfMissing', () => {
        it('should add a View to an empty array', () => {
          const view: IView = { id: 123 };
          expectedResult = service.addViewToCollectionIfMissing([], view);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(view);
        });

        it('should not add a View to an array that contains it', () => {
          const view: IView = { id: 123 };
          const viewCollection: IView[] = [
            {
              ...view,
            },
            { id: 456 },
          ];
          expectedResult = service.addViewToCollectionIfMissing(viewCollection, view);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a View to an array that doesn't contain it", () => {
          const view: IView = { id: 123 };
          const viewCollection: IView[] = [{ id: 456 }];
          expectedResult = service.addViewToCollectionIfMissing(viewCollection, view);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(view);
        });

        it('should add only unique View to an array', () => {
          const viewArray: IView[] = [{ id: 123 }, { id: 456 }, { id: 36646 }];
          const viewCollection: IView[] = [{ id: 123 }];
          expectedResult = service.addViewToCollectionIfMissing(viewCollection, ...viewArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const view: IView = { id: 123 };
          const view2: IView = { id: 456 };
          expectedResult = service.addViewToCollectionIfMissing([], view, view2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(view);
          expect(expectedResult).toContain(view2);
        });

        it('should accept null and undefined values', () => {
          const view: IView = { id: 123 };
          expectedResult = service.addViewToCollectionIfMissing([], null, view, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(view);
        });

        it('should return initial array if no View is added', () => {
          const viewCollection: IView[] = [{ id: 123 }];
          expectedResult = service.addViewToCollectionIfMissing(viewCollection, undefined, null);
          expect(expectedResult).toEqual(viewCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
