import * as dayjs from 'dayjs';
import { IJob } from 'app/entities/job/job.model';

export interface IMachine {
  id?: number;
  name?: string;
  description?: string;
  createDateTime?: dayjs.Dayjs | null;
  updateDateTime?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  jobs?: IJob[] | null;
}

export class Machine implements IMachine {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public createDateTime?: dayjs.Dayjs | null,
    public updateDateTime?: dayjs.Dayjs | null,
    public deleted?: boolean | null,
    public jobs?: IJob[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getMachineIdentifier(machine: IMachine): number | undefined {
  return machine.id;
}
