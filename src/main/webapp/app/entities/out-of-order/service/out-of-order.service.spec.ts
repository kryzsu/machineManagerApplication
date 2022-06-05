import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOutOfOrder, OutOfOrder } from '../out-of-order.model';

import { OutOfOrderService } from './out-of-order.service';

describe('Service Tests', () => {
  describe('OutOfOrder Service', () => {
    let service: OutOfOrderService;
    let httpMock: HttpTestingController;
    let elemDefault: IOutOfOrder;
    let expectedResult: IOutOfOrder | IOutOfOrder[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OutOfOrderService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        start: currentDate,
        end: currentDate,
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            start: currentDate.format(DATE_FORMAT),
            end: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a OutOfOrder', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            start: currentDate.format(DATE_FORMAT),
            end: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            start: currentDate,
            end: currentDate,
          },
          returnedFromService
        );

        service.create(new OutOfOrder()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OutOfOrder', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            start: currentDate.format(DATE_FORMAT),
            end: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            start: currentDate,
            end: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OutOfOrder', () => {
        const patchObject = Object.assign({}, new OutOfOrder());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            start: currentDate,
            end: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OutOfOrder', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            start: currentDate.format(DATE_FORMAT),
            end: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            start: currentDate,
            end: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a OutOfOrder', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOutOfOrderToCollectionIfMissing', () => {
        it('should add a OutOfOrder to an empty array', () => {
          const outOfOrder: IOutOfOrder = { id: 123 };
          expectedResult = service.addOutOfOrderToCollectionIfMissing([], outOfOrder);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(outOfOrder);
        });

        it('should not add a OutOfOrder to an array that contains it', () => {
          const outOfOrder: IOutOfOrder = { id: 123 };
          const outOfOrderCollection: IOutOfOrder[] = [
            {
              ...outOfOrder,
            },
            { id: 456 },
          ];
          expectedResult = service.addOutOfOrderToCollectionIfMissing(outOfOrderCollection, outOfOrder);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OutOfOrder to an array that doesn't contain it", () => {
          const outOfOrder: IOutOfOrder = { id: 123 };
          const outOfOrderCollection: IOutOfOrder[] = [{ id: 456 }];
          expectedResult = service.addOutOfOrderToCollectionIfMissing(outOfOrderCollection, outOfOrder);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(outOfOrder);
        });

        it('should add only unique OutOfOrder to an array', () => {
          const outOfOrderArray: IOutOfOrder[] = [{ id: 123 }, { id: 456 }, { id: 56156 }];
          const outOfOrderCollection: IOutOfOrder[] = [{ id: 123 }];
          expectedResult = service.addOutOfOrderToCollectionIfMissing(outOfOrderCollection, ...outOfOrderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const outOfOrder: IOutOfOrder = { id: 123 };
          const outOfOrder2: IOutOfOrder = { id: 456 };
          expectedResult = service.addOutOfOrderToCollectionIfMissing([], outOfOrder, outOfOrder2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(outOfOrder);
          expect(expectedResult).toContain(outOfOrder2);
        });

        it('should accept null and undefined values', () => {
          const outOfOrder: IOutOfOrder = { id: 123 };
          expectedResult = service.addOutOfOrderToCollectionIfMissing([], null, outOfOrder, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(outOfOrder);
        });

        it('should return initial array if no OutOfOrder is added', () => {
          const outOfOrderCollection: IOutOfOrder[] = [{ id: 123 }];
          expectedResult = service.addOutOfOrderToCollectionIfMissing(outOfOrderCollection, undefined, null);
          expect(expectedResult).toEqual(outOfOrderCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
