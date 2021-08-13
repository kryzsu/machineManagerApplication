import { IOutOfOrder } from 'app/shared/model/out-of-order.model';
import { IJob } from 'app/shared/model/job.model';
import { IView } from 'app/shared/model/view.model';

export interface IMachine {
  id?: number;
  name?: string;
  description?: string;
  outOfOrders?: IOutOfOrder[];
  jobs?: IJob[];
  views?: IView[];
}

export class Machine implements IMachine {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public outOfOrders?: IOutOfOrder[],
    public jobs?: IJob[],
    public views?: IView[]
  ) {}
}
