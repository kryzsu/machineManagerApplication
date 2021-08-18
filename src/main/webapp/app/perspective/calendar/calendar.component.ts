import { Component, OnInit } from '@angular/core';
import { CalendarEvent, CalendarEventAction, CalendarView } from 'angular-calendar';
import { Subject } from 'rxjs';
import { startOfDay, subDays, addDays, endOfDay, isSameDay, isSameMonth } from 'date-fns';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import * as d3 from 'd3';

import { IMachine } from 'app/shared/model/machine.model';
import { IJob } from 'app/shared/model/job.model';
import * as moment from 'moment';
import { machineArray2Events } from '../converter-utils';

@Component({
  selector: 'jhi-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  CalendarView = CalendarView;
  view: CalendarView = CalendarView.Month;

  machineList: IMachine[] = [];
  todo = ['Get to work', 'Pick up groceries', 'Go home', 'Fall asleep'];

  done = ['Get up', 'Brush teeth', 'Take a shower', 'Check e-mail', 'Walk dog'];

  refresh: Subject<any> = new Subject();

  activeDayIsOpen = true;

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
        this.events2 = this.events2.filter(iEvent => iEvent !== event);
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

  viewDate: Date = new Date();

  events2: CalendarEvent[] = [];

  constructor() {
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

      this.machineList.push({
        id: machineId,
        name: 'gep' + i,
        description: 'gep leiras ' + i,
        jobs,
      });
    }

    this.events2 = machineArray2Events(this.machineList, this.actions);
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
    this.events2 = this.events2.filter(event => event !== eventToDelete);
  }

  addEvent(): void {
    this.events2 = [
      ...this.events2,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
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

  setView(view: CalendarView): void {
    this.view = view;
  }

  closeOpenMonthViewDay(): void {
    this.activeDayIsOpen = false;
  }
}
