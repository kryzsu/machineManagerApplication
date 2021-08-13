import { Route } from '@angular/router';

import { CalendarComponent } from './calendar.component';

export const calendarRoute: Route = {
  path: 'calendar',
  component: CalendarComponent,
  data: {
    authorities: [],
    pageTitle: 'calendar.title',
  },
};
