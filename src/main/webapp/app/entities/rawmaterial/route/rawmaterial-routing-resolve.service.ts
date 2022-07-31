import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRawmaterial, Rawmaterial } from '../rawmaterial.model';
import { RawmaterialService } from '../service/rawmaterial.service';

@Injectable({ providedIn: 'root' })
export class RawmaterialRoutingResolveService implements Resolve<IRawmaterial> {
  constructor(protected service: RawmaterialService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRawmaterial> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rawmaterial: HttpResponse<Rawmaterial>) => {
          if (rawmaterial.body) {
            return of(rawmaterial.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rawmaterial());
  }
}
