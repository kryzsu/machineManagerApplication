import { IJob } from 'app/shared/model/job.model';

export interface IMachine {
  id?: number;
  name?: string;
  description?: string;
  jobs?: IJob[];
}

export class Machine implements IMachine {
  constructor(public id?: number, public name?: string, public description?: string, public jobs?: IJob[]) {}
}
