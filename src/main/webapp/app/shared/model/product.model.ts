import { IJob } from 'app/shared/model/job.model';

export interface IProduct {
  id?: number;
  name?: string;
  comment?: string;
  jobs?: IJob[];
}

export class Product implements IProduct {
  constructor(public id?: number, public name?: string, public comment?: string, public jobs?: IJob[]) {}
}
