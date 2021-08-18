import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OutOfOrderComponent } from '../list/out-of-order.component';
import { OutOfOrderDetailComponent } from '../detail/out-of-order-detail.component';
import { OutOfOrderUpdateComponent } from '../update/out-of-order-update.component';
import { OutOfOrderRoutingResolveService } from './out-of-order-routing-resolve.service';

const outOfOrderRoute: Routes = [
  {
    path: '',
    component: OutOfOrderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OutOfOrderDetailComponent,
    resolve: {
      outOfOrder: OutOfOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OutOfOrderUpdateComponent,
    resolve: {
      outOfOrder: OutOfOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OutOfOrderUpdateComponent,
    resolve: {
      outOfOrder: OutOfOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(outOfOrderRoute)],
  exports: [RouterModule],
})
export class OutOfOrderRoutingModule {}
