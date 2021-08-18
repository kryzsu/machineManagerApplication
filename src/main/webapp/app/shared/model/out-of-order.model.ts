import { Moment } from 'moment';
import { IMachine } from 'app/shared/model/machine.model';

export interface IOutOfOrder {
  id?: number;
  date?: Moment;
  description?: string;
  machines?: IMachine[];
}

export class OutOfOrder implements IOutOfOrder {
  constructor(public id?: number, public date?: Moment, public description?: string, public machines?: IMachine[]) {}
}
