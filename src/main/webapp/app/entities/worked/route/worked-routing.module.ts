import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkedComponent } from '../list/worked.component';
import { WorkedDetailComponent } from '../detail/worked-detail.component';
import { WorkedUpdateComponent } from '../update/worked-update.component';
import { WorkedRoutingResolveService } from './worked-routing-resolve.service';

const workedRoute: Routes = [
  {
    path: '',
    component: WorkedComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkedDetailComponent,
    resolve: {
      worked: WorkedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkedUpdateComponent,
    resolve: {
      worked: WorkedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkedUpdateComponent,
    resolve: {
      worked: WorkedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workedRoute)],
  exports: [RouterModule],
})
export class WorkedRoutingModule {}
