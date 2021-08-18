import { IJob } from 'app/entities/job/job.model';

export interface IProduct {
  id?: number;
  name?: string;
  comment?: string | null;
  jobs?: IJob[] | null;
}

export class Product implements IProduct {
  constructor(public id?: number, public name?: string, public comment?: string | null, public jobs?: IJob[] | null) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
