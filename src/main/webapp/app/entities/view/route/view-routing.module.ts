import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ViewComponent } from '../list/view.component';
import { ViewDetailComponent } from '../detail/view-detail.component';
import { ViewUpdateComponent } from '../update/view-update.component';
import { ViewRoutingResolveService } from './view-routing-resolve.service';

const viewRoute: Routes = [
  {
    path: '',
    component: ViewComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ViewDetailComponent,
    resolve: {
      view: ViewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ViewUpdateComponent,
    resolve: {
      view: ViewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ViewUpdateComponent,
    resolve: {
      view: ViewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(viewRoute)],
  exports: [RouterModule],
})
export class ViewRoutingModule {}
