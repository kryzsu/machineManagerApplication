import * as dayjs from 'dayjs';
import { IWorked } from 'app/entities/worked/worked.model';
import { IMachine } from 'app/entities/machine/machine.model';

export interface IJob {
  id?: number;
  customerName?: string;
  days?: number;
  productName?: string;
  count?: number;
  productType?: string | null;
  comment?: string | null;
  createDateTime?: dayjs.Dayjs | null;
  updateDateTime?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  inProgress?: boolean | null;
  daysDone?: number | null;
  workeds?: IWorked[] | null;
  machine?: IMachine | null;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public customerName?: string,
    public days?: number,
    public productName?: string,
    public count?: number,
    public productType?: string | null,
    public comment?: string | null,
    public createDateTime?: dayjs.Dayjs | null,
    public updateDateTime?: dayjs.Dayjs | null,
    public deleted?: boolean | null,
    public inProgress?: boolean | null,
    public daysDone?: number | null,
    public workeds?: IWorked[] | null,
    public machine?: IMachine | null
  ) {
    this.deleted = this.deleted ?? false;
    this.inProgress = this.inProgress ?? false;
  }
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
