import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IMachine } from 'app/entities/machine/machine.model';
import { dayjsToString } from '../util/common-util';
import { FilterInterval } from './component/interval-filter/filter-interval';
import { createRequestOption } from '../core/request/request-util';

export type EntityArrayResponseType = HttpResponse<IMachine[]>;

@Injectable({ providedIn: 'root' })
export class PerspectiveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/perspective');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDetailedMachineList(interval: FilterInterval): Observable<EntityArrayResponseType> {
    const options = createRequestOption({
      startDate: dayjsToString(interval.startDate),
      endDate: dayjsToString(interval.endDate),
    });

    return this.http.get<IMachine[]>(`${this.resourceUrl}/get-detailed-machine-list`, { params: options, observe: 'response' });
  }
}
