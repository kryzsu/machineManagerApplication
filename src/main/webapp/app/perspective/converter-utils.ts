import { CalendarEvent, CalendarEventAction } from 'angular-calendar';
import { EventColor } from 'calendar-utils';
import { addDays } from 'date-fns';
import { TreeNode } from 'primeng/api';

import { IMachine } from 'app/entities/machine/machine.model';
import { IJob } from 'app/entities/job/job.model';
import { IProduct } from 'app/entities/product/product.model';
import { AppState } from '../redux/app.state';
import { BarData } from '../shared/bar-chart/bar-chart.component';

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
  const rv: CalendarEvent[] = [];

  for (const machine of machineList) {
    const color = colorList[Math.floor(Math.random() * 100) % 7];
    if (machine.jobs != null) {
      for (const job of machine.jobs) {
        const startDate: Date = job.startDate?.toDate() ?? new Date();
        const estimation: number = job.estimation ?? 0;
        let title = machine.name ?? '';
        if (job.products != null) {
          title += `: ${job.products[0].name ?? ''}`;
        }

        rv.push({
          start: startDate,
          end: addDays(startDate, estimation),
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
        });
      }
    }
  }

  return rv;
};

export const job2Treenode = (job: IJob): TreeNode => ({
  label: `${job.products?.map((product: IProduct): string => product.name ?? '').join(',') ?? ''} ${job.estimation ?? 0} nap`,
  data: job,
  leaf: true,
  draggable: true,
  droppable: false,
  selectable: true,
  key: `${job.id ?? 0}`,
});

export const machine2Treenode = (machine: IMachine): TreeNode => ({
  label: machine.name,
  data: machine,
  children: machine.jobs?.map(job2Treenode),
  leaf: false,
  draggable: false,
  droppable: true,
  selectable: true,
  key: `${machine.id ?? 0}`,
});
