import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorked, Worked } from '../worked.model';
import { WorkedService } from '../service/worked.service';

@Injectable({ providedIn: 'root' })
export class WorkedRoutingResolveService implements Resolve<IWorked> {
  constructor(protected service: WorkedService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorked> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((worked: HttpResponse<Worked>) => {
          if (worked.body) {
            return of(worked.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Worked());
  }
}
