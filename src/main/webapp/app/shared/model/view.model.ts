import { IMachine } from 'app/shared/model/machine.model';

export interface IView {
  id?: number;
  name?: string;
  machines?: IMachine[];
}

export class View implements IView {
  constructor(public id?: number, public name?: string, public machines?: IMachine[]) {}
}
