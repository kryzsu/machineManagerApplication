import { Moment } from 'moment';

export interface IOutOfOrder {
  id?: number;
  date?: Moment;
  description?: string;
}

export class OutOfOrder implements IOutOfOrder {
  constructor(public id?: number, public date?: Moment, public description?: string) {}
}
