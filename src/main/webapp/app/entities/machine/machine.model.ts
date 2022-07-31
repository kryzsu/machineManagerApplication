import { IOutOfOrder } from 'app/entities/out-of-order/out-of-order.model';
import { IJob } from 'app/entities/job/job.model';
import { IView } from 'app/entities/view/view.model';

export interface IMachine {
  id?: number;
  name?: string;
  description?: string;
  outOfOrders?: IOutOfOrder[] | null;
  jobs?: IJob[] | null;
  views?: IView[] | null;
  runningJob?: IJob | null;
}

export class Machine implements IMachine {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public outOfOrders?: IOutOfOrder[] | null,
    public jobs?: IJob[] | null,
    public views?: IView[] | null,
    public runningJob?: IJob | null
  ) {}
}

export function getMachineIdentifier(machine: IMachine): number | undefined {
  return machine.id;
}
