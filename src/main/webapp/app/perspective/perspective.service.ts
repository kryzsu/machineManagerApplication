import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import {IMachine, IMachineDay} from 'app/entities/machine/machine.model';
import { dayjsToString } from '../util/common-util';
import { FilterInterval } from './component/interval-filter/filter-interval';
import { createRequestOption } from '../core/request/request-util';
import { OutOfOrder } from '../entities/out-of-order/out-of-order.model';
import { IIdWithPriority } from '../entities/job/job.model';

export type EntityArrayResponseType = HttpResponse<IMachine[]>;

@Injectable({ providedIn: 'root' })
export class PerspectiveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/perspective');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDetailedMachineList(): Observable<EntityArrayResponseType> {
    const options = createRequestOption({});

    return this.http.get<IMachine[]>(`${this.resourceUrl}/get-detailed-machine-list`, { params: options, observe: 'response' });
  }

  getNextDateForMachine(machineId: string, estimation: number): Observable<HttpResponse<string>> {
    const options = createRequestOption({
      machineId,
      estimation,
    });

    return this.http.get<string>(`${this.resourceUrl}/get-next-start-date-4-machine`, { params: options, observe: 'response' });
  }

  getRelatedOutOfOrder(machineId: string): Observable<HttpResponse<OutOfOrder[]>> {
    const options = createRequestOption({
      machineId,
    });

    return this.http.get<OutOfOrder[]>(`${this.resourceUrl}/get-related-out-of-order`, { params: options, observe: 'response' });
  }

  savePriorities(idWithPrioritiesList: IIdWithPriority[]): Observable<HttpResponse<any>> {
    return this.http.post<IIdWithPriority[]>(`${this.resourceUrl}/save-priorities`, idWithPrioritiesList, { observe: 'response' });
  }

  getNextDays(machineId: number): Observable<HttpResponse<IMachineDay[]>> {
    const options = createRequestOption({
      machineId,
      days: 14
    });

    return this.http.get<IMachineDay[]>(`${this.resourceUrl}/get-job-next-days`, { params: options, observe: 'response' });
  }
}
