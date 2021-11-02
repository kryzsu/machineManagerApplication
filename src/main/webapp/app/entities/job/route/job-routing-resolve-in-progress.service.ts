import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJob } from '../job.model';
import { JobService } from '../service/job.service';

@Injectable({ providedIn: 'root' })
export class JobRoutingResolveInProgressService implements Resolve<IJob[]> {
  constructor(protected service: JobService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJob[]> | Observable<never> {
    const machineId = route.params['machine-id'];

    if (machineId) {
      return this.service.inProgress({ machineId }).pipe(
        mergeMap((job: HttpResponse<IJob[]>) => {
          if (job.body) {
            return of(job.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of([]);
  }
}
