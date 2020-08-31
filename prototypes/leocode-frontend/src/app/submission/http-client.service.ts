import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {
  url = 'http://localhost:9090';

  constructor(private http: HttpClient) { }

  uploadEndpoint(form: HTMLFormElement): Observable<any> {
    const formData: FormData = new FormData(form);

    return this.http.post(`${this.url}/upload`, formData)
      .pipe(map(data => {
        const result = {...data};
        return result;
      }));
  }

  listAllExamples(): Observable<any> {
    return this.http.get(`${this.url}/example/getAll`)
      .pipe(map(data => {
        const result = [];
        for (const key in data) {
          if (data.hasOwnProperty(key)){
            result.push({...data[key], id: key});
          }
        }
        return result;
      }));
  }

}
