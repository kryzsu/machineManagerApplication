import { Routes } from '@angular/router';

import { calendarRoute } from './calendar/calendar.route';

const PERSPECTIVE_ROUTES = [calendarRoute];

export const perspectiveState: Routes = [
  {
    path: '',
    children: PERSPECTIVE_ROUTES,
  },
];
