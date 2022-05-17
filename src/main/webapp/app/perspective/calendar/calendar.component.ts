/* tslint:disable: no-unsafe-return */
import { Component, OnInit } from '@angular/core';
import { CalendarEvent, CalendarEventAction, CalendarView } from 'angular-calendar';
import { Observable, Subject } from 'rxjs';
import { startOfDay, endOfDay, isSameDay, isSameMonth } from 'date-fns';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import * as d3 from 'd3';

import { machine2BarData, machineArray2Events } from '../converter-utils';
import { IIdWithPriority, IJob } from 'app/entities/job/job.model';
import { Store } from '@ngrx/store';
import { selectMachineList } from '../../redux/selectors';
import * as Actions from '../../redux/actions';
import { AppState } from '../../redux/app.state';
import { filter, map } from 'rxjs/operators';
import { BarData } from '../../shared/bar-chart/bar-chart.component';
import { IMachine } from '../../entities/machine/machine.model';
import { EntityArrayResponseType, PerspectiveService } from '../perspective.service';
import { sortByNameCaseInsensitive } from '../../util/common-util';
import { defaultInterval, FilterInterval } from 'app/perspective/component/interval-filter/filter-interval';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  isLoading = false;
  CalendarView = CalendarView;
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();

  events2: CalendarEvent[] = [];

  refresh: Subject<any> = new Subject();
  activeDayIsOpen = true;
  machineList$: Observable<AppState> | undefined;
  barData: BarData = { labels: [], datasets: [] };

  private perspectiveService: PerspectiveService;
  private store: Store;
  private actions: CalendarEventAction[] = [
    {
      label: '&nbsp; edit &nbsp;',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        let jobId;
        if (event.id === undefined) {
          jobId = -1;
        } else if (typeof event.id === 'string') {
          jobId = Number.parseInt(event.id, 10);
        } else {
          jobId = event.id;
        }
        this.router.navigate(['/job', jobId, 'edit']);
        // this.store.dispatch(Actions.editJob({ jobId }));
      },
    },
    {
      label: '&nbsp; delete &nbsp;',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        const job: IJob = event.meta;
        this.store.dispatch(Actions.deleteJob({ jobId: job.id ?? -1 }));
        this.events2 = this.events2.filter(iEvent => iEvent !== event);
      },
    },
  ];

  private svg: any;
  private margin = 50;
  private width = 750 - this.margin * 2;
  private height = 400 - this.margin * 2;

  constructor(store: Store, perspectiveService: PerspectiveService, protected router: Router) {
    this.store = store;
    this.perspectiveService = perspectiveService;
    this.machineList$ = this.store.select(selectMachineList);

    this.store
      .select(selectMachineList)
      .pipe(
        filter(val => val !== undefined), // eslint-disable-line @typescript-eslint/no-unnecessary-condition
        map(machine2BarData)
      )
      .subscribe(data => {
        this.barData = data;
      });

    this.store
      .select(selectMachineList)
      .pipe(
        filter(val => val !== undefined), // eslint-disable-line @typescript-eslint/no-unnecessary-condition
        map(state => machineArray2Events(state.machineList, this.actions))
      )
      .subscribe(data => (this.events2 = data));
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
    this.doRefresh();
    this.createSvg();
  }

  doRefresh(): void {
    this.perspectiveService
      .getDetailedMachineList()
      .pipe(map((response: EntityArrayResponseType) => response.body ?? []))
      .subscribe((machineList: IMachine[]) => {
        machineList = sortByNameCaseInsensitive(machineList);
        this.store.dispatch(Actions.createMachineList({ machineList }));
      });
  }

  setView(view: CalendarView): void {
    this.view = view;
  }

  closeOpenMonthViewDay(): void {
    this.activeDayIsOpen = false;
  }

  onRefresh(): void {
    this.doRefresh();
  }

  savePriorities(idWithPrioritiesList: IIdWithPriority[]): void {
    this.perspectiveService.savePriorities(idWithPrioritiesList).subscribe(() => {
      console.warn('savePriorities');
    });
  }

  private createSvg(): void {
    this.svg = d3
      .select('figure#bar')
      .append('svg')
      .attr('width', this.width + this.margin * 2)
      .attr('height', this.height + this.margin * 2)
      .append('g')
      .attr('transform', `translate(${this.margin}, ${this.margin})`);
  }

  private drawBars(data: any[]): void {
    // Add X axis
    const x: any = d3
      .scaleBand()
      .range([0, this.width])
      .domain(data.map(d => d.Framework)) // eslint-disable-line @typescript-eslint/no-unsafe-return
      .padding(0.2);

    this.svg
      .append('g')
      .attr('transform', `translate(0,${this.height})`)
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
      .attr('x', (d: any) => x(d.Framework)) // eslint-disable-line @typescript-eslint/no-unsafe-return
      .attr('y', (d: any) => y(d.Stars))
      .attr('width', x.bandwidth())
      .attr('height', (d: any) => this.height - y(d.Stars))
      .attr('fill', '#d04a35');
  }
}
