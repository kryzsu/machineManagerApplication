import * as dayjs from 'dayjs';
import { IProduct } from 'app/entities/product/product.model';
import { IMachine } from 'app/entities/machine/machine.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IJob {
  id?: number;
  estimation?: number | null;
  priority?: number;
  manualOrder?: number;
  productCount?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  fact?: number | null;
  orderNumber?: string | null;
  drawingNumber?: string | null;
  drawingContentType?: string | null;
  drawing?: string | null;
  worknumber?: string;
  products?: IProduct[] | null;
  machine?: IMachine | null;
  customer?: ICustomer | null;
  consumerName?: string | null;
  createDateTime?: dayjs.Dayjs | null;
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
    public drawingNumber?: string | null,
    public drawingContentType?: string | null,
    public drawing?: string | null,
    public worknumber?: string,
    public products?: IProduct[] | null,
    public machine?: IMachine | null,
    public customer?: ICustomer | null
  ) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
