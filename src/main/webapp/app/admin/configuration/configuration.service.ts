import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ConfigurationService {
  constructor(private http: HttpClient) {}

  get(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'management/configprops', { observe: 'body' });
  }
}
