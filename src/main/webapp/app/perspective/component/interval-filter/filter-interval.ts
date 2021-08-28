import * as dayjs from 'dayjs';

export interface FilterInterval {
  startDate: dayjs.Dayjs;
  endDate: dayjs.Dayjs;
}

export const defaultInterval = {
  startDate: dayjs().subtract(1, 'week'),
  endDate: dayjs().add(2, 'week'),
};
