import { Component, OnInit } from '@angular/core';
import {HttpClientService} from '../submission/http-client.service';

@Component({
  selector: 'app-create-example',
  templateUrl: './create-example.component.html',
  styleUrls: ['./create-example.component.css']
})
export class CreateExampleComponent implements OnInit {

  form: HTMLFormElement;

  constructor(
    public httpClient: HttpClientService
  ) { }

  ngOnInit(): void {
  }

  upload(): void {
    this.form = document.forms.namedItem('createExample');
    if (this.form.checkValidity()) {
      this.httpClient.uploadEndpoint(this.form).subscribe(
        data => {
          console.log(data.valueOf());
        }
      );
    }
  }
}
