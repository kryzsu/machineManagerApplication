import { toDate } from 'app/util/common-util';

describe('common utils', () => {
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
