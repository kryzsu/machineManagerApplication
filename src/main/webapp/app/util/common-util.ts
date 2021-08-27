import * as R from 'ramda';

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
