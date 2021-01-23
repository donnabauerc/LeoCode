import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Example} from '../model/example.model';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  BASE_URL = 'http://localhost:9090/';

  constructor(private http: HttpClient) { }

  getExampleList(): Observable<Example[]> {
    return this.http.get<Example[]>(this.BASE_URL + 'example/list');
  }

  getExampleById(id: number): Observable<Example>{
    return this.http.get<Example>(this.BASE_URL  + 'example/' + id);
  }
}
