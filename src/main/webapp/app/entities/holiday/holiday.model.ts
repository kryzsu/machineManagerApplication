import * as dayjs from 'dayjs';

export interface IHoliday {
  id?: number;
  day?: dayjs.Dayjs | null;
  comment?: string | null;
  deleted?: boolean | null;
}

export class Holiday implements IHoliday {
  constructor(public id?: number, public day?: dayjs.Dayjs | null, public comment?: string | null, public deleted?: boolean | null) {
    this.deleted = this.deleted ?? false;
  }
}

export function getHolidayIdentifier(holiday: IHoliday): number | undefined {
  return holiday.id;
}
