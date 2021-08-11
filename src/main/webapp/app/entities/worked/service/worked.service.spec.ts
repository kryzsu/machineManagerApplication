import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWorked, Worked } from '../worked.model';

import { WorkedService } from './worked.service';

describe('Service Tests', () => {
  describe('Worked Service', () => {
    let service: WorkedService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorked;
    let expectedResult: IWorked | IWorked[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkedService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        day: currentDate,
        comment: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            day: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Worked', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            day: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            day: currentDate,
          },
          returnedFromService
        );

        service.create(new Worked()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Worked', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            day: currentDate.format(DATE_FORMAT),
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            day: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Worked', () => {
        const patchObject = Object.assign(
          {
            day: currentDate.format(DATE_FORMAT),
            comment: 'BBBBBB',
          },
          new Worked()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            day: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Worked', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            day: currentDate.format(DATE_FORMAT),
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            day: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Worked', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkedToCollectionIfMissing', () => {
        it('should add a Worked to an empty array', () => {
          const worked: IWorked = { id: 123 };
          expectedResult = service.addWorkedToCollectionIfMissing([], worked);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(worked);
        });

        it('should not add a Worked to an array that contains it', () => {
          const worked: IWorked = { id: 123 };
          const workedCollection: IWorked[] = [
            {
              ...worked,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkedToCollectionIfMissing(workedCollection, worked);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Worked to an array that doesn't contain it", () => {
          const worked: IWorked = { id: 123 };
          const workedCollection: IWorked[] = [{ id: 456 }];
          expectedResult = service.addWorkedToCollectionIfMissing(workedCollection, worked);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(worked);
        });

        it('should add only unique Worked to an array', () => {
          const workedArray: IWorked[] = [{ id: 123 }, { id: 456 }, { id: 52723 }];
          const workedCollection: IWorked[] = [{ id: 123 }];
          expectedResult = service.addWorkedToCollectionIfMissing(workedCollection, ...workedArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const worked: IWorked = { id: 123 };
          const worked2: IWorked = { id: 456 };
          expectedResult = service.addWorkedToCollectionIfMissing([], worked, worked2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(worked);
          expect(expectedResult).toContain(worked2);
        });

        it('should accept null and undefined values', () => {
          const worked: IWorked = { id: 123 };
          expectedResult = service.addWorkedToCollectionIfMissing([], null, worked, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(worked);
        });

        it('should return initial array if no Worked is added', () => {
          const workedCollection: IWorked[] = [{ id: 123 }];
          expectedResult = service.addWorkedToCollectionIfMissing(workedCollection, undefined, null);
          expect(expectedResult).toEqual(workedCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
