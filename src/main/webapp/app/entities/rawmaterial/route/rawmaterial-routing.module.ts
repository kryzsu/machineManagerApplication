import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RawmaterialComponent } from '../list/rawmaterial.component';
import { RawmaterialDetailComponent } from '../detail/rawmaterial-detail.component';
import { RawmaterialUpdateComponent } from '../update/rawmaterial-update.component';
import { RawmaterialRoutingResolveService } from './rawmaterial-routing-resolve.service';

const rawmaterialRoute: Routes = [
  {
    path: '',
    component: RawmaterialComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RawmaterialDetailComponent,
    resolve: {
      rawmaterial: RawmaterialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RawmaterialUpdateComponent,
    resolve: {
      rawmaterial: RawmaterialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RawmaterialUpdateComponent,
    resolve: {
      rawmaterial: RawmaterialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rawmaterialRoute)],
  exports: [RouterModule],
})
export class RawmaterialRoutingModule {}
