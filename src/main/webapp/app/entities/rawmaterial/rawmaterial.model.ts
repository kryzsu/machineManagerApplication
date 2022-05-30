export interface IRawmaterial {
  id?: number;
  name?: string;
  comment?: string | null;
}

export class Rawmaterial implements IRawmaterial {
  constructor(public id?: number, public name?: string, public comment?: string | null) {}
}

export function getRawmaterialIdentifier(rawmaterial: IRawmaterial): number | undefined {
  return rawmaterial.id;
}
