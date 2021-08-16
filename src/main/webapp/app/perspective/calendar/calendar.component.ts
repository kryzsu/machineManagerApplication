import { Component } from '@angular/core';
import { CalendarEvent, CalendarEventAction } from 'angular-calendar';
import { Subject } from 'rxjs';
import { startOfDay, subDays, addDays, endOfDay, isSameDay, isSameMonth } from 'date-fns';

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
export class CalendarComponent {
  refresh: Subject<any> = new Subject();

  activeDayIsOpen = true;

  actions: CalendarEventAction[] = [
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

  viewDate: Date = new Date();

  events: CalendarEvent[] = [
    {
      start: subDays(startOfDay(new Date()), 1),
      end: addDays(new Date(), 1),
      title: '3 napos a gep1-en',
      color: colors.red,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: addDays(new Date(), 2),
      end: addDays(new Date(), 2),
      title: '1 napos a gep1-en',
      color: colors.red,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 1),
      title: '1 napos a gep2-en',
      color: colors.blue,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: addDays(new Date(), 2),
      end: addDays(new Date(), 5),
      title: '2. feladat gep2-en',
      color: colors.blue,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 6),
      title: '1 napos a gep3-en',
      color: colors.yellow,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: addDays(new Date(), 7),
      end: addDays(new Date(), 10),
      title: '2. feladat gep3-en',
      color: colors.yellow,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 1),
      title: '1 napos a gep4-en',
      color: colors.teal,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 2),
      title: '1 napos a gep5-en',
      color: colors.olive,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: addDays(new Date(), 3),
      end: addDays(new Date(), 5),
      title: '2. feladat gep5-en',
      color: colors.olive,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 1),
      title: '1 napos a gep6-en',
      color: colors.chocolate,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 3),
      title: '1 napos a gep7-en',
      color: colors.purple,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: addDays(new Date(), 4),
      end: addDays(new Date(), 5),
      title: '2. feladat gep7-en',
      color: colors.purple,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: startOfDay(new Date()),
      end: addDays(new Date(), 4),
      title: '1 napos a gep8-en',
      color: colors.cyan,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
    {
      start: addDays(new Date(), 5),
      end: addDays(new Date(), 9),
      title: '2. feladat gep8-en',
      color: colors.cyan,
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,
    },
  ];

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

  deleteEvent(eventToDelete: CalendarEvent) {
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
}
