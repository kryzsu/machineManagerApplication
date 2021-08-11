import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorked, getWorkedIdentifier } from '../worked.model';

export type EntityResponseType = HttpResponse<IWorked>;
export type EntityArrayResponseType = HttpResponse<IWorked[]>;

@Injectable({ providedIn: 'root' })
export class WorkedService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workeds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(worked: IWorked): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(worked);
    return this.http
      .post<IWorked>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(worked: IWorked): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(worked);
    return this.http
      .put<IWorked>(`${this.resourceUrl}/${getWorkedIdentifier(worked) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(worked: IWorked): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(worked);
    return this.http
      .patch<IWorked>(`${this.resourceUrl}/${getWorkedIdentifier(worked) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorked>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorked[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkedToCollectionIfMissing(workedCollection: IWorked[], ...workedsToCheck: (IWorked | null | undefined)[]): IWorked[] {
    const workeds: IWorked[] = workedsToCheck.filter(isPresent);
    if (workeds.length > 0) {
      const workedCollectionIdentifiers = workedCollection.map(workedItem => getWorkedIdentifier(workedItem)!);
      const workedsToAdd = workeds.filter(workedItem => {
        const workedIdentifier = getWorkedIdentifier(workedItem);
        if (workedIdentifier == null || workedCollectionIdentifiers.includes(workedIdentifier)) {
          return false;
        }
        workedCollectionIdentifiers.push(workedIdentifier);
        return true;
      });
      return [...workedsToAdd, ...workedCollection];
    }
    return workedCollection;
  }

  protected convertDateFromClient(worked: IWorked): IWorked {
    return Object.assign({}, worked, {
      day: worked.day?.isValid() ? worked.day.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.day = res.body.day ? dayjs(res.body.day) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((worked: IWorked) => {
        worked.day = worked.day ? dayjs(worked.day) : undefined;
      });
    }
    return res;
  }
}
