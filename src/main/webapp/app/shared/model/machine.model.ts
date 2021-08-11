import { IOutOfOrder } from 'app/shared/model/out-of-order.model';
import { IJob } from 'app/shared/model/job.model';

export interface IMachine {
  id?: number;
  name?: string;
  description?: string;
  outOfOrders?: IOutOfOrder[];
  jobs?: IJob[];
}

export class Machine implements IMachine {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public outOfOrders?: IOutOfOrder[],
    public jobs?: IJob[]
  ) {}
}
