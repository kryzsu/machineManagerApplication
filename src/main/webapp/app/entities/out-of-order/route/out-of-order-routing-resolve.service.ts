import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOutOfOrder, OutOfOrder } from '../out-of-order.model';
import { OutOfOrderService } from '../service/out-of-order.service';

@Injectable({ providedIn: 'root' })
export class OutOfOrderRoutingResolveService implements Resolve<IOutOfOrder> {
  constructor(protected service: OutOfOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOutOfOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((outOfOrder: HttpResponse<OutOfOrder>) => {
          if (outOfOrder.body) {
            return of(outOfOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OutOfOrder());
  }
}
