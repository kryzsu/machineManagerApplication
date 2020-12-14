import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMachine, Machine } from 'app/shared/model/machine.model';
import { MachineService } from './machine.service';
import { MachineComponent } from './machine.component';
import { MachineDetailComponent } from './machine-detail.component';
import { MachineUpdateComponent } from './machine-update.component';

@Injectable({ providedIn: 'root' })
export class MachineResolve implements Resolve<IMachine> {
  constructor(private service: MachineService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMachine> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((machine: HttpResponse<Machine>) => {
          if (machine.body) {
            return of(machine.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Machine());
  }
}

export const machineRoute: Routes = [
  {
    path: '',
    component: MachineComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.machine.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MachineDetailComponent,
    resolve: {
      machine: MachineResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.machine.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MachineUpdateComponent,
    resolve: {
      machine: MachineResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.machine.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MachineUpdateComponent,
    resolve: {
      machine: MachineResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'machineManagerApplicationApp.machine.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
