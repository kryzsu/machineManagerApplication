import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IView, View } from '../view.model';
import { ViewService } from '../service/view.service';

@Injectable({ providedIn: 'root' })
export class ViewRoutingResolveService implements Resolve<IView> {
  constructor(protected service: ViewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IView> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((view: HttpResponse<View>) => {
          if (view.body) {
            return of(view.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new View());
  }
}
