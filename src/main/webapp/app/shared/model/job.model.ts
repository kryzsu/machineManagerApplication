import { IMachine } from 'app/shared/model/machine.model';

export interface IJob {
  id?: number;
  name?: string;
  machine?: IMachine;
}

export class Job implements IJob {
  constructor(public id?: number, public name?: string, public machine?: IMachine) {}
}
