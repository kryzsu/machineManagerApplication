import { IJob } from 'app/entities/job/job.model';
import { IRawmaterial } from 'app/entities/rawmaterial/rawmaterial.model';

export interface IProduct {
  id?: number;
  name?: string;
  drawingNumber?: string;
  itemNumber?: string | null;
  weight?: number;
  comment?: string | null;
  drawingContentType?: string | null;
  drawing?: string | null;
  jobs?: IJob[] | null;
  rawmaterial?: IRawmaterial | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public drawingNumber?: string,
    public itemNumber?: string | null,
    public weight?: number,
    public comment?: string | null,
    public drawingContentType?: string | null,
    public drawing?: string | null,
    public jobs?: IJob[] | null,
    public rawmaterial?: IRawmaterial | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
