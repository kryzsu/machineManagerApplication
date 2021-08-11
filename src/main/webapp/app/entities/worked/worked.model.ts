import * as dayjs from 'dayjs';
import { IJob } from 'app/entities/job/job.model';

export interface IWorked {
  id?: number;
  day?: dayjs.Dayjs | null;
  comment?: string | null;
  jobs?: IJob[] | null;
}

export class Worked implements IWorked {
  constructor(public id?: number, public day?: dayjs.Dayjs | null, public comment?: string | null, public jobs?: IJob[] | null) {}
}

export function getWorkedIdentifier(worked: IWorked): number | undefined {
  return worked.id;
}
