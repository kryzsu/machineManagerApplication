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
      const rv = toDate('2021-08-27');
      // THEN
      expect(rv).not.toBeNull();
      expect(rv?.getDate()).toEqual(2021);
      expect(rv?.getMonth()).toEqual(8);
      expect(rv?.getDay()).toEqual(27);
    });
  });
});
