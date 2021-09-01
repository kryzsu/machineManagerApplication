import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { editJob, editJobEnd } from './actions';
import { Router } from '@angular/router';
import { EMPTY, from } from 'rxjs';

@Injectable()
export class RouteToJobEditEffect {
  routeToJobEdit$ = createEffect(() =>
    this.actions$.pipe(
      ofType(editJob),
      mergeMap(action =>
        from(this.router.navigate(['/job', action.jobId, 'edit'])).pipe(
          map(() => editJobEnd({ jobId: 1 })),
          catchError(() => EMPTY)
        )
      )
    )
  );

  constructor(private actions$: Actions, private router: Router) {}
}
