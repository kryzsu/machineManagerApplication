import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IView, View } from 'app/shared/model/view.model';
import { ViewService } from './view.service';
import { ViewComponent } from './view.component';
import { ViewDetailComponent } from './view-detail.component';
import { ViewUpdateComponent } from './view-update.component';

@Injectable({ providedIn: 'root' })
export class ViewResolve implements Resolve<IView> {
  constructor(private service: ViewService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IView> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((view: HttpResponse<View>) => {
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

export const viewRoute: Routes = [
  {
    path: '',
    component: ViewComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'machineManagerApplicationApp.view.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ViewDetailComponent,
    resolve: {
      view: ViewResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.view.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ViewUpdateComponent,
    resolve: {
      view: ViewResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.view.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ViewUpdateComponent,
    resolve: {
      view: ViewResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.view.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
