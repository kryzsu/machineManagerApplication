import * as dayjs from 'dayjs';
import { IMachine } from 'app/entities/machine/machine.model';

export interface IOutOfOrder {
  id?: number;
  start?: dayjs.Dayjs;
  end?: dayjs.Dayjs;
  description?: string;
  machines?: IMachine[] | null;
}

export class OutOfOrder implements IOutOfOrder {
  constructor(
    public id?: number,
    public start?: dayjs.Dayjs,
    public end?: dayjs.Dayjs,
    public description?: string,
    public machines?: IMachine[] | null
  ) {}
}

export function getOutOfOrderIdentifier(outOfOrder: IOutOfOrder): number | undefined {
  return outOfOrder.id;
}
