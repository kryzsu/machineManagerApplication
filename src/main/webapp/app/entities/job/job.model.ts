import * as dayjs from 'dayjs';
import { IProduct } from 'app/entities/product/product.model';
import { IMachine } from 'app/entities/machine/machine.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IJob {
  id?: number;
  estimation?: number | null;
  productCount?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  fact?: number | null;
  orderNumber?: string | null;
  createDateTime?: dayjs.Dayjs | null;
  drawingContentType?: string | null;
  drawing?: string | null;
  worknumber?: string;
  priority?: number | null;
  product?: IProduct | null;
  machine?: IMachine | null;
  customer?: ICustomer | null;
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
    public createDateTime?: dayjs.Dayjs | null,
    public drawingContentType?: string | null,
    public drawing?: string | null,
    public worknumber?: string,
    public priority?: number | null,
    public product?: IProduct | null,
    public machine?: IMachine | null,
    public customer?: ICustomer | null
  ) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
