import * as R from 'ramda';
import * as dayjs from 'dayjs';
import { DATE_FORMAT } from '../config/input.constants';

// eslint-disable-next-line @typescript-eslint/no-unsafe-return
export const sortByNameCaseInsensitive = R.sortBy(R.compose(R.toLower, (item: any) => item.name ?? ''));

export const toDate = (dateStr: string | null): Date | null => {
  if (dateStr == null) {
    return null;
  }

  const parts = dateStr.split('-');
  return new Date(Number(parts[0]), Number(parts[1]), Number(parts[2]));
};

export const wrongDate = new Date(1000, 1, 1);

export const dayjsToString = (date: dayjs.Dayjs): string | undefined => (date.isValid() ? date.format(DATE_FORMAT) : undefined);
