import { job2Event, job2Treenode, machine2Treenode } from 'app/perspective/converter-utils';
import { IMachine } from '../entities/machine/machine.model';
import { IJob } from '../entities/job/job.model';
import * as dayjs from 'dayjs';

describe('converter utils', () => {
  describe('job2Treenode', () => {
    const basicJob: IJob = {
      id: 1,
    };
    it('basic data', () => {
      // GIVEN
      const rv = job2Treenode(basicJob);
      // THEN
      expect(rv).not.toBeNull();
      expect(rv.key).toEqual(`${basicJob.id}`);
      expect(rv.children).toBeUndefined();
      expect(rv.leaf).toBeTruthy();
      expect(rv.draggable).toBeTruthy();
      expect(rv.selectable).toBeTruthy();
      expect(rv.droppable).toBeFalsy();
    });

    it('with product', () => {
      const job: IJob = {
        ...basicJob,
        estimation: 2,
        products: [{ id: 11, name: 'product' }],
      };

      // GIVEN
      const rv = job2Treenode(job);
      // THEN
      expect(rv.label).toEqual('product 2 nap');
    });

    it('with more product', () => {
      const job: IJob = {
        ...basicJob,
        estimation: 2,
        products: [
          { id: 11, name: 'product' },
          { id: 22, name: 'product2' },
        ],
      };

      // GIVEN
      const rv = job2Treenode(job);
      // THEN
      expect(rv.label).toEqual('product, product2 2 nap');
    });
  });

  describe('job2Event', () => {
    const basicJob: IJob = {
      id: 1,
      estimation: 2,
    };
    const machine: IMachine = {
      id: 1,
      name: 'MachineName',
      description: 'desc',
    };
    it('no end date', () => {
      const dateStr = '2021-08-02';
      const date = new Date(dateStr);
      const job: IJob = {
        ...basicJob,
        startDate: dateStr as unknown as dayjs.Dayjs,
      };

      // GIVEN
      const rv = job2Event(machine, job, []);
      // THEN
      expect(rv).not.toBeNull();
      expect(rv.start.getDate()).toEqual(date.getDate());
      expect(rv.start.getMonth()).toEqual(date.getMonth());
      expect(rv.start.getFullYear()).toEqual(date.getFullYear());
      expect(rv.title).toEqual('MachineName');
    });

    it('with product', () => {
      const job: IJob = {
        ...basicJob,
        estimation: 2,
        products: [{ id: 11, name: 'product' }],
      };

      // GIVEN
      const rv = job2Treenode(job);
      // THEN
      expect(rv.label).toEqual('product 2 nap');
    });

    it('with more product', () => {
      const job: IJob = {
        ...basicJob,
        estimation: 2,
        products: [
          { id: 11, name: 'product' },
          { id: 22, name: 'product2' },
        ],
      };

      // GIVEN
      const rv = job2Treenode(job);
      // THEN
      expect(rv.label).toEqual('product, product2 2 nap');
    });
  });

  describe('machine2Treenode', () => {
    const basicMachine: IMachine = {
      id: 1,
      name: 'name',
      description: 'desc',
    };
    it('no child machine not a problem', () => {
      // GIVEN
      const rv = machine2Treenode(basicMachine);
      // THEN
      expect(rv).not.toBeNull();
      expect(rv.children).toBeUndefined();
    });
  });
});
