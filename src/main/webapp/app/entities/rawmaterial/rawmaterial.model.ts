export interface IRawmaterial {
  id?: number;
  name?: string;
  comment?: string | null;
  grade?: string;
  dimension?: string;
  coating?: string;
  supplier?: string;
}

export class Rawmaterial implements IRawmaterial {
  constructor(
    public id?: number,
    public name?: string,
    public comment?: string | null,
    public grade?: string,
    public dimension?: string,
    public coating?: string,
    public supplier?: string
  ) {}
}

export function getRawmaterialIdentifier(rawmaterial: IRawmaterial): number | undefined {
  return rawmaterial.id;
}
