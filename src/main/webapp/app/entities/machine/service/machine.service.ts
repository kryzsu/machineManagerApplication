import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMachine, getMachineIdentifier } from '../machine.model';

export type EntityResponseType = HttpResponse<IMachine>;
export type EntityArrayResponseType = HttpResponse<IMachine[]>;

@Injectable({ providedIn: 'root' })
export class MachineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/machines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(machine: IMachine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(machine);
    return this.http
      .post<IMachine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(machine: IMachine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(machine);
    return this.http
      .put<IMachine>(`${this.resourceUrl}/${getMachineIdentifier(machine) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(machine: IMachine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(machine);
    return this.http
      .patch<IMachine>(`${this.resourceUrl}/${getMachineIdentifier(machine) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMachine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMachine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMachineToCollectionIfMissing(machineCollection: IMachine[], ...machinesToCheck: (IMachine | null | undefined)[]): IMachine[] {
    const machines: IMachine[] = machinesToCheck.filter(isPresent);
    if (machines.length > 0) {
      const machineCollectionIdentifiers = machineCollection.map(machineItem => getMachineIdentifier(machineItem)!);
      const machinesToAdd = machines.filter(machineItem => {
        const machineIdentifier = getMachineIdentifier(machineItem);
        if (machineIdentifier == null || machineCollectionIdentifiers.includes(machineIdentifier)) {
          return false;
        }
        machineCollectionIdentifiers.push(machineIdentifier);
        return true;
      });
      return [...machinesToAdd, ...machineCollection];
    }
    return machineCollection;
  }

  protected convertDateFromClient(machine: IMachine): IMachine {
    return Object.assign({}, machine, {
      createDateTime: machine.createDateTime?.isValid() ? machine.createDateTime.toJSON() : undefined,
      updateDateTime: machine.updateDateTime?.isValid() ? machine.updateDateTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDateTime = res.body.createDateTime ? dayjs(res.body.createDateTime) : undefined;
      res.body.updateDateTime = res.body.updateDateTime ? dayjs(res.body.updateDateTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((machine: IMachine) => {
        machine.createDateTime = machine.createDateTime ? dayjs(machine.createDateTime) : undefined;
        machine.updateDateTime = machine.updateDateTime ? dayjs(machine.updateDateTime) : undefined;
      });
    }
    return res;
  }
}
