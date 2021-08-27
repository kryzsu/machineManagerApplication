import { IJob } from 'app/entities/job/job.model';

export interface ICustomer {
  id?: number;
  name?: string;
  jobs?: IJob[] | null;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public name?: string, public jobs?: IJob[] | null) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
