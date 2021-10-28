import { Interval, dateFitInterval, toDate } from 'app/util/common-util';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct';

describe('common utils', () => {
  describe('dateFitInterval', () => {
    it('true', () => {
      // GIVEN
      const date: NgbDate = new NgbDate(2000, 1, 3);
      const interval: Interval = {
        start: new NgbDate(2000, 1, 1),
        end: new NgbDate(2000, 1, 5),
      };
      // WHEN
      const rv = dateFitInterval(interval, date);
      // THEN
      expect(rv).toBeTruthy();
    });

    it('false', () => {
      // GIVEN
      const date: NgbDate = new NgbDate(2000, 1, 8);
      const interval: Interval = {
        start: new NgbDate(2000, 1, 1),
        end: new NgbDate(2000, 1, 5),
      };
      // WHEN
      const rv = dateFitInterval(interval, date);
      // THEN
      expect(rv).toBeFalsy();
    });
  });

  describe('toDate', () => {
    it('null', () => {
      // GIVEN
      const rv = toDate(null);
      // THEN
      expect(rv).toBeNull();
    });

    it('not null', () => {
      // GIVEN
      const year = 2021;
      const month = 8;
      const day = 27;
      const rv = toDate(`${year}-${month}-${day}`);
      // THEN
      expect(rv).not.toBeNull();
      expect(rv?.getFullYear()).toEqual(year);
      expect(rv?.getMonth()).toEqual(month - 1);
      expect(rv?.getDate()).toEqual(day);
    });
  });
});
