import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMachine } from 'app/shared/model/machine.model';

type EntityResponseType = HttpResponse<IMachine>;
type EntityArrayResponseType = HttpResponse<IMachine[]>;

@Injectable({ providedIn: 'root' })
export class MachineService {
  public resourceUrl = SERVER_API_URL + 'api/machines';

  constructor(protected http: HttpClient) {}

  create(machine: IMachine): Observable<EntityResponseType> {
    return this.http.post<IMachine>(this.resourceUrl, machine, { observe: 'response' });
  }

  update(machine: IMachine): Observable<EntityResponseType> {
    return this.http.put<IMachine>(this.resourceUrl, machine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMachine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMachine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
