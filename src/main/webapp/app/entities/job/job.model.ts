import * as dayjs from 'dayjs';
import { IProduct } from 'app/entities/product/product.model';
import { IMachine } from 'app/entities/machine/machine.model';

export interface IJob {
  id?: number;
  estimation?: number | null;
  productCount?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  fact?: number | null;
  orderNumber?: string | null;
  products?: IProduct[] | null;
  machine?: IMachine | null;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public estimation?: number | null,
    public productCount?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public fact?: number | null,
    public orderNumber?: string | null,
    public products?: IProduct[] | null,
    public machine?: IMachine | null
  ) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
