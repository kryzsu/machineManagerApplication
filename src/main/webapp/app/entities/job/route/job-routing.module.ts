import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JobComponent } from '../list/job.component';
import { JobDetailComponent } from '../detail/job-detail.component';
import { JobUpdateComponent } from '../update/job-update.component';
import { JobRoutingResolveService } from './job-routing-resolve.service';
import { JobInProgressComponent } from '../in-progress-list/job-in-progress.component';
import { JobRoutingResolveInProgressService } from './job-routing-resolve-in-progress.service';

const jobRoute: Routes = [
  {
    path: '',
    component: JobComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobDetailComponent,
    resolve: {
      job: JobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'in-progress/:machine-id',
    component: JobInProgressComponent,
    resolve: {
      jobList: JobRoutingResolveInProgressService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobUpdateComponent,
    resolve: {
      job: JobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobUpdateComponent,
    resolve: {
      job: JobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jobRoute)],
  exports: [RouterModule],
})
export class JobRoutingModule {}
