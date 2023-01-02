import { job2Event, job2Treenode, machine2Treenode, machineDaysLists2Events } from 'app/perspective/converter-utils';
import { IMachine } from '../entities/machine/machine.model';
import { IJob } from '../entities/job/job.model';
import * as dayjs from 'dayjs';
import { IMachineDay } from '../entities/machineday';
import { CalendarEventAction } from 'angular-calendar';

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
        product: { id: 11, name: 'product' },
      };

      // GIVEN
      const rv = job2Treenode(job);
      // THEN
      expect(rv.label).toEqual('product 2 nap');
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
        product: { id: 11, name: 'product' },
      };

      // GIVEN
      const rv = job2Treenode(job);
      // THEN
      expect(rv.label).toEqual('product 2 nap');
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

  describe('machineDaysLists2Events', () => {
    it('empty', () => {
      // GIVEN
      const machineDayList: IMachineDay[] = [];
      const actions: CalendarEventAction[] = [
        {
          // eslint-disable-next-line @typescript-eslint/no-empty-function
          label: '',
          onClick() {},
        },
      ];

      const rv = machineDaysLists2Events(machineDayList, actions);

      // THEN
      expect(rv).toEqual([]);
    });

    it('3 day two jobs', () => {
      // GIVEN
      const machineDayList: IMachineDay[] = [
        { date: '2023-01-02', occupied: true, comment: 'wn1', jobId: 48, dayOfWeek: 1 },
        { date: '2023-01-03', occupied: true, comment: 'wn7', jobId: 54, dayOfWeek: 2 },
        { date: '2023-01-04', occupied: true, comment: 'wn7', jobId: 54, dayOfWeek: 3 },
      ];

      const actions: CalendarEventAction[] = [
        {
          // eslint-disable-next-line @typescript-eslint/no-empty-function
          label: '',
          onClick() {},
        },
      ];

      const rv = machineDaysLists2Events(machineDayList, actions);

      // THEN
      expect(rv[0].id).toEqual(machineDayList[0].jobId);
      expect(rv[0].title).toEqual(machineDayList[0].comment);
      expect(rv[0].start.toISOString()).toEqual('2023-01-01T23:00:00.000Z');
      expect(rv[0]?.end?.toISOString()).toEqual('2023-01-01T23:00:00.000Z');

      expect(rv[1].id).toEqual(machineDayList[1].jobId);
      expect(rv[1].title).toEqual(machineDayList[1].comment);
      expect(rv[1].start.toISOString()).toEqual('2023-01-02T23:00:00.000Z');
      expect(rv[1]?.end?.toISOString()).toEqual('2023-01-03T23:00:00.000Z');
    });

    it('just weekend', () => {
      // GIVEN
      const machineDayList: IMachineDay[] = [
        { date: '2023-01-02', occupied: true, dayOfWeek: 6 },
        { date: '2023-01-03', occupied: true, dayOfWeek: 7 },
      ];

      const actions: CalendarEventAction[] = [
        {
          // eslint-disable-next-line @typescript-eslint/no-empty-function
          label: '',
          onClick() {},
        },
      ];

      const rv = machineDaysLists2Events(machineDayList, actions);

      // THEN
      expect(rv).toEqual([]);
    });

    it('1 weekend on job', () => {
      // GIVEN
      const machineDayList: IMachineDay[] = [
        { date: '2023-01-02', occupied: true, comment: 'wn1', jobId: 48, dayOfWeek: 1 },
        { date: '2023-01-03', occupied: true, dayOfWeek: 7 },
      ];

      const actions: CalendarEventAction[] = [
        {
          // eslint-disable-next-line @typescript-eslint/no-empty-function
          label: '',
          onClick() {},
        },
      ];

      const rv = machineDaysLists2Events(machineDayList, actions);

      // THEN
      expect(rv.length).toEqual(1);
      expect(rv[0].id).toEqual(machineDayList[0].jobId);
      expect(rv[0].title).toEqual(machineDayList[0].comment);
      expect(rv[0].start.toISOString()).toEqual('2023-01-02T23:00:00.000Z');
      expect(rv[0]?.end?.toISOString()).toEqual('2023-01-02T23:00:00.000Z');
    });
  });
});
