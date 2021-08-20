/* eslint @typescript-eslint/no-var-requires: "off" */
import { Component, Input, OnInit } from '@angular/core';
import { CalendarEvent } from 'angular-calendar';
declare const require: any;
const milestones = require('d3-milestones');

interface Item {
  timestamp: string;
  text: string;
}

@Component({
  selector: 'jhi-perps-timeline',
  templateUrl: './perps-timeline.component.html',
  styleUrls: ['./perps-timeline.component.scss'],
})
export class PerpsTimelineComponent implements OnInit {
  @Input()
  set events(events: CalendarEvent[]) {
    this._events = events.map((eventObj: CalendarEvent) => ({
      timestamp: eventObj.start.toISOString().slice(0, 10),
      text: eventObj.title,
    }));
    this.refresh();
  }

  private _events: Item[] = [];

  ngOnInit(): void {
    this.refresh();
  }

  private refresh(): void {
    milestones.default('#timeline').parseTime('%Y-%m-%d').aggregateBy('day').render(this._events);
  }
}
