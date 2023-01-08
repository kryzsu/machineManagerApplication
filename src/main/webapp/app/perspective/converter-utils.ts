import { CalendarEvent, CalendarEventAction } from 'angular-calendar';
import { EventColor } from 'calendar-utils';
import { addDays } from 'date-fns';
import { TreeNode } from 'primeng/api';

import { IMachine } from 'app/entities/machine/machine.model';
import { IJob } from 'app/entities/job/job.model';
import { AppState } from '../redux/app.state';
import { BarData } from '../shared/bar-chart/bar-chart.component';
import { dayjsToString, toDate, wrongDate } from '../util/common-util';
import * as dayjs from 'dayjs';
import { IMachineDay } from '../entities/machineday';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA',
  },
  teal: {
    primary: '#008080',
    secondary: '#FDF1BA',
  },
  olive: {
    primary: '#808000',
    secondary: '#FDF1BA',
  },
  chocolate: {
    primary: '#D2691E',
    secondary: '#FDF1BA',
  },
  cyan: {
    primary: '#00FFFF',
    secondary: '#FDF1BA',
  },
  purple: {
    primary: '#800080',
    secondary: '#FDF1BA',
  },
};
export const colorList: EventColor[] = [];
export const createColors = (): void => {
  colorList.push(colors.cyan);
  colorList.push(colors.olive);
  colorList.push(colors.purple);
  colorList.push(colors.chocolate);
  colorList.push(colors.teal);
  colorList.push(colors.yellow);
  colorList.push(colors.blue);
  colorList.push(colors.red);
};

createColors();

export const getEstimatedEndDate = (job: IJob): string =>
  dayjsToString(
    (job.endDate ? job.endDate : dayjs(job.startDate as any as string).add(job.estimation ?? 0, 'day')) as any as dayjs.Dayjs
  ) as string;

export const getJobLabel = (job: IJob): string =>
  `${job.worknumber ?? 0} - ${job.productCount ?? 0} - ${job.orderNumber ?? ''} - ${getEstimatedEndDate(job)}`;

export const machine2BarData = (appState: AppState): BarData => {
  const labels: string[] = [];
  const data: number[] = [];
  appState.machineList.forEach((machine: IMachine) => {
    labels.push(machine.name ?? '');
    let sum = 0;
    machine.jobs?.forEach((job: IJob): number => (sum += job.estimation ?? 0));
    data.push(sum);
  });

  return { labels, datasets: [{ label: 'becsült idők', data }] };
};

export const machineArray2Events = (machineList: IMachine[], actions: CalendarEventAction[]): CalendarEvent[] => {
  const events: CalendarEvent[] = [];
  machineList.forEach(machine => machine.jobs?.forEach(job => events.push(job2Event(machine, job, actions))));
  return events;
};

export const isWeekendOrFreeDay = (machineDay: IMachineDay): boolean => [6, 7].includes(machineDay.dayOfWeek ?? 10) || !machineDay.occupied;

export const machineDaysLists2Events = (machineDayList: IMachineDay[], actions: CalendarEventAction[]): CalendarEvent[] => {
  const color: EventColor = colorList[Math.floor(Math.random() * 100) % 7];
  const events: CalendarEvent[] = [];
  let jobStartMachineDay = null;
  let jobLatestMachineDay = null;
  let i = 0;

  // find first occupied job day
  for (; i < machineDayList.length; i++) {
    if (isWeekendOrFreeDay(machineDayList[i])) {
      continue;
    }

    jobStartMachineDay = machineDayList[i];
    jobLatestMachineDay = jobStartMachineDay;
    break;
  }

  i++;
  for (; i < machineDayList.length; i++) {
    if (isWeekendOrFreeDay(machineDayList[i])) {
      continue;
    }

    if (machineDayList[i].jobId !== jobStartMachineDay?.jobId) {
      const start: Date = toDate(jobStartMachineDay?.date ?? '') ?? wrongDate;
      const title = jobStartMachineDay?.comment ?? '';
      events.push(
        createEvent(
          jobStartMachineDay?.jobId ?? 0,
          start,
          toDate(machineDayList[i - 1]?.date ?? '') ?? wrongDate,
          title,
          actions,
          colorList[Math.floor(Math.random() * 100) % 7]
        )
      );
      jobStartMachineDay = machineDayList[i];
    }
    jobLatestMachineDay = machineDayList[i];
  }

  if (jobLatestMachineDay !== null) {
    const start: Date = toDate(jobStartMachineDay?.date ?? '') ?? wrongDate;
    const title = jobStartMachineDay?.comment ?? '';
    events.push(
      createEvent(
        jobStartMachineDay?.jobId ?? 0,
        start,
        toDate(jobLatestMachineDay.date ?? '') ?? wrongDate,
        title,
        actions,
        colorList[Math.floor(Math.random() * 100) % 7]
      )
    );
  }
  return events;
};

export const createEvent = (
  id: number,
  startDate: Date,
  endDate: Date,
  title: string,
  actions: CalendarEventAction[],
  color: EventColor
): CalendarEvent => ({
  id,
  start: startDate,
  end: endDate,
  title,
  color,
  actions,
  allDay: true,
  resizable: {
    beforeStart: true,
    afterEnd: true,
  },
  draggable: true,
});

export const machineDay2Event = (machineDay: IMachineDay, actions: CalendarEventAction[], color: EventColor): CalendarEvent => {
  const startDate: Date = machineDay.date ? toDate(machineDay.date) ?? wrongDate : wrongDate;
  const title = machineDay.comment ?? '';
  const endDate: Date | undefined = startDate;

  return {
    id: machineDay.jobId,
    start: startDate,
    end: endDate,
    title,
    color,
    actions,
    allDay: true,
    resizable: {
      beforeStart: true,
      afterEnd: true,
    },
    draggable: true,
    meta: machineDay,
  };
};
export const job2Event = (machine: IMachine, job: IJob, actions: CalendarEventAction[]): CalendarEvent => {
  const color = colorList[Math.floor(Math.random() * 100) % 7];
  const startDate: Date =
    job.startDate === undefined || job.startDate === null ? wrongDate : toDate(job.startDate as unknown as string) ?? wrongDate;
  const estimation: number = job.estimation ?? 0;
  let title = machine.name ?? '';
  title += `: ${getJobLabel(job)}`;
  const endDate: Date | undefined = job.endDate instanceof Date ? job.endDate : job.endDate?.toDate();
  const end: Date = endDate ?? addDays(startDate, estimation);

  return {
    id: job.id,
    start: startDate,
    end,
    title,
    color,
    actions,
    allDay: true,
    resizable: {
      beforeStart: true,
      afterEnd: true,
    },
    draggable: true,
    meta: job,
  };
};

export const job2Treenode = (job: IJob): TreeNode => ({
  label: getJobLabel(job),
  data: job,
  leaf: true,
  draggable: true,
  droppable: true,
  key: `job ${job.id ?? 0}`,
});

export const machine2Treenode = (machine: IMachine): TreeNode => ({
  label: `${machine.name ?? 'machine'} (${machine.jobs?.length ?? '0'})`,
  data: machine,
  children: machine.jobs?.map(job2Treenode),
  leaf: false,
  draggable: false,
  droppable: true,
  selectable: false,
  key: `machine ${machine.id ?? 0}`,
});
