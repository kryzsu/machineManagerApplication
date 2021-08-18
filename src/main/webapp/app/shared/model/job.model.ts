import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';

export interface IJob {
  id?: number;
  estimation?: number;
  productCount?: number;
  startDate?: Moment;
  endDate?: Moment;
  fact?: number;
  orderNumber?: string;
  products?: IProduct[];
  machineName?: string;
  machineId?: number;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public estimation?: number,
    public productCount?: number,
    public startDate?: Moment,
    public endDate?: Moment,
    public fact?: number,
    public orderNumber?: string,
    public products?: IProduct[],
    public machineName?: string,
    public machineId?: number
  ) {}
}
