import { Moment } from 'moment';

export interface IHoliday {
  id?: number;
  day?: Moment;
  comment?: string;
}

export class Holiday implements IHoliday {
  constructor(public id?: number, public day?: Moment, public comment?: string) {}
}
