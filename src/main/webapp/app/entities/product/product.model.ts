import { IJob } from 'app/entities/job/job.model';
import { IRawmaterial } from 'app/entities/rawmaterial/rawmaterial.model';

export interface IProduct {
  id?: number;
  name?: string;
  comment?: string | null;
  weight?: number;
  jobs?: IJob[] | null;
  rawmaterial?: IRawmaterial | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public comment?: string | null,
    public weight?: number,
    public jobs?: IJob[] | null,
    public rawmaterial?: IRawmaterial | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
