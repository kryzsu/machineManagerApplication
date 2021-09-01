import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { newJob, newJobEnd } from './actions';
import { Router } from '@angular/router';
import { EMPTY, from } from 'rxjs';

@Injectable()
export class RouteToJobNewEffect {
  routeToJobNew$ = createEffect(() =>
    this.actions$.pipe(
      ofType(newJob),
      mergeMap(action =>
        from(this.router.navigate(['/job', 'new'], { state: { machineId: action.machineId } })).pipe(
          map(() => newJobEnd()),
          catchError(() => EMPTY)
        )
      )
    )
  );

  constructor(private actions$: Actions, private router: Router) {}
}
