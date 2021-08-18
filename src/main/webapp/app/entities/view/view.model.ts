import { IMachine } from 'app/entities/machine/machine.model';

export interface IView {
  id?: number;
  name?: string | null;
  machines?: IMachine[] | null;
}

export class View implements IView {
  constructor(public id?: number, public name?: string | null, public machines?: IMachine[] | null) {}
}

export function getViewIdentifier(view: IView): number | undefined {
  return view.id;
}
