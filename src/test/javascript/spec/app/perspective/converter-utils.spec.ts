import { machine2Treenode } from 'app/perspective/converter-utils';
import { IMachine } from 'app/shared/model/machine.model';

fdescribe('converter utils', () => {
  describe('machine2Treenode', () => {
    it('no child machine not a problem', () => {
      const machine: IMachine = {
        id: 1,
        name: 'name',
        description: 'desc',
      };

      // GIVEN
      const rv = machine2Treenode(machine);
      // THEN
      expect(rv).toBeNull();
    });
  });
});
