import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOutOfOrder, OutOfOrder } from 'app/shared/model/out-of-order.model';
import { OutOfOrderService } from './out-of-order.service';
import { OutOfOrderComponent } from './out-of-order.component';
import { OutOfOrderDetailComponent } from './out-of-order-detail.component';
import { OutOfOrderUpdateComponent } from './out-of-order-update.component';

@Injectable({ providedIn: 'root' })
export class OutOfOrderResolve implements Resolve<IOutOfOrder> {
  constructor(private service: OutOfOrderService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOutOfOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((outOfOrder: HttpResponse<OutOfOrder>) => {
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

export const outOfOrderRoute: Routes = [
  {
    path: '',
    component: OutOfOrderComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'machineManagerApplicationApp.outOfOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OutOfOrderDetailComponent,
    resolve: {
      outOfOrder: OutOfOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.outOfOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OutOfOrderUpdateComponent,
    resolve: {
      outOfOrder: OutOfOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.outOfOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OutOfOrderUpdateComponent,
    resolve: {
      outOfOrder: OutOfOrderResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.outOfOrder.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
