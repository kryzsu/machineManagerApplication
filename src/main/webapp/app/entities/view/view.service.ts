import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IView } from 'app/shared/model/view.model';

type EntityResponseType = HttpResponse<IView>;
type EntityArrayResponseType = HttpResponse<IView[]>;

@Injectable({ providedIn: 'root' })
export class ViewService {
  public resourceUrl = SERVER_API_URL + 'api/views';

  constructor(protected http: HttpClient) {}

  create(view: IView): Observable<EntityResponseType> {
    return this.http.post<IView>(this.resourceUrl, view, { observe: 'response' });
  }

  update(view: IView): Observable<EntityResponseType> {
    return this.http.put<IView>(this.resourceUrl, view, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IView>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IView[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
