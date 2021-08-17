import { Component, OnInit } from '@angular/core';
import { CalendarEvent, CalendarEventAction } from 'angular-calendar';
import { Subject } from 'rxjs';
import { startOfDay, subDays, addDays, endOfDay, isSameDay, isSameMonth } from 'date-fns';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import * as d3 from 'd3';
import { EventColor } from 'calendar-utils';

import { IMachine } from 'app/shared/model/machine.model';
import { IJob } from 'app/shared/model/job.model';
import * as moment from 'moment';

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

@Component({
  selector: 'jhi-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  colorList: EventColor[];

  private demoData: IMachine[] = [];
  private todo = ['Get to work', 'Pick up groceries', 'Go home', 'Fall asleep'];

  private done = ['Get up', 'Brush teeth', 'Take a shower', 'Check e-mail', 'Walk dog'];

  private refresh: Subject<any> = new Subject();

  private activeDayIsOpen = true;

  private actions: CalendarEventAction[] = [
    {
      label: '&nbsp; edit &nbsp;',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '&nbsp; delete &nbsp;',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter(iEvent => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];
  private data = [
    { Framework: 'Vue', Stars: '166443', Released: '2014' },
    { Framework: 'React', Stars: '150793', Released: '2013' },
    { Framework: 'Angular', Stars: '62342', Released: '2016' },
    { Framework: 'Backbone', Stars: '27647', Released: '2010' },
    { Framework: 'Ember', Stars: '21471', Released: '2011' },
  ];
  private svg: any;
  private margin = 50;
  private width = 750 - this.margin * 2;
  private height = 400 - this.margin * 2;

  private viewDate: Date = new Date();

  private events2: CalendarEvent[] = [];

  constructor() {
    this.colorList = this.createColors();
    const startDate = subDays(startOfDay(new Date()), 1);

    for (let i = 0; i < 12; i++) {
      const machineId = i * 100;
      const jobs: IJob[] = [];
      let estimationSum = 0;

      for (let j = 0; j < 6; j++) {
        const estimation = Math.floor(Math.random() * 100) % 3;
        jobs.push({
          id: machineId + 10 * j + 1,
          startDate: moment(addDays(startDate, estimationSum)),
          estimation,
          orderNumber: 'order ' + machineId + j + 1,
          products: [{ id: machineId + 10 * j + 1, name: 'termek ' + machineId + j + 1 }],
          machineName: 'gep' + i,
          machineId,
        });

        estimationSum += estimation;
      }

      this.demoData.push({
        id: machineId,
        name: 'gep' + i,
        description: 'gep leiras ' + i,
        jobs,
      });
    }

    this.events2 = this.machineArray2Events(this.demoData);
  }

  machineArray2Events(machineList: IMachine[]): CalendarEvent[] {
    const rv: CalendarEvent[] = [];

    for (const machine of machineList) {
      const color = this.colorList[Math.floor(Math.random() * 100) % 7];
      if (machine.jobs != null) {
        for (const job of machine.jobs) {
          const startDate = job?.startDate?.toDate() || new Date();
          const estimation = job.estimation || 0;
          let title = machine.name || '';
          if (job.products != null) {
            title += ': ' + job.products[0].name;
          }

          rv.push({
            start: startDate,
            end: addDays(startDate, estimation),
            title,
            color,
            actions: this.actions,
            allDay: true,
            resizable: {
              beforeStart: true,
              afterEnd: true,
            },
            draggable: true,
          });
        }
      }
    }

    return rv;
  }

  private createColors(): EventColor[] {
    const colorList = [];
    colorList.push(colors.cyan);
    colorList.push(colors.olive);
    colorList.push(colors.purple);
    colorList.push(colors.chocolate);
    colorList.push(colors.teal);
    colorList.push(colors.yellow);
    colorList.push(colors.blue);
    colorList.push(colors.red);

    return colorList;
  }

  handleEvent(arg0: string, event: CalendarEvent<any>): void {
    alert('handleEvent' + arg0 + ' ' + event);
  }

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if ((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) || events.length === 0) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  deleteEvent(eventToDelete: CalendarEvent): void {
    this.events = this.events.filter(event => event !== eventToDelete);
  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.red,
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];
  }

  drop(event: CdkDragDrop<string[]>): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
    }
  }

  ngOnInit(): void {
    this.createSvg();
    this.drawBars(this.data);

    // Parse data from a CSV
    // d3.csv("/assets/frameworks.csv").then(data => this.drawBars(data));

    // Fetch JSON from an external endpoint
    // d3.json('https://api.jsonbin.io/b/5eee6a5397cb753b4d149343').then(data => this.drawBars(data));
  }

  private createSvg(): void {
    this.svg = d3
      .select('figure#bar')
      .append('svg')
      .attr('width', this.width + this.margin * 2)
      .attr('height', this.height + this.margin * 2)
      .append('g')
      .attr('transform', 'translate(' + this.margin + ',' + this.margin + ')');
  }

  private drawBars(data: any[]): void {
    // Add X axis
    const x = d3
      .scaleBand()
      .range([0, this.width])
      .domain(data.map(d => d.Framework))
      .padding(0.2);

    this.svg
      .append('g')
      .attr('transform', 'translate(0,' + this.height + ')')
      .call(d3.axisBottom(x))
      .selectAll('text')
      .attr('transform', 'translate(-10,0)rotate(-45)')
      .style('text-anchor', 'end');

    // Add Y axis
    const y = d3.scaleLinear().domain([0, 200000]).range([this.height, 0]);

    this.svg.append('g').call(d3.axisLeft(y));

    // Create and fill the bars
    this.svg
      .selectAll('bars')
      .data(data)
      .enter()
      .append('rect')
      .attr('x', (d: any) => x(d.Framework))
      .attr('y', (d: any) => y(d.Stars))
      .attr('width', x.bandwidth())
      .attr('height', (d: any) => this.height - y(d.Stars))
      .attr('fill', '#d04a35');
  }
}
