import { Component, OnInit } from '@angular/core';
import {HttpService} from '../services/http.service';

@Component({
  selector: 'app-create-example',
  templateUrl: './create-example.component.html',
  styleUrls: ['./create-example.component.css']
})
export class CreateExampleComponent implements OnInit {

  form: HTMLFormElement;

  constructor(private http: HttpService) { }

  ngOnInit(): void {
  }

  upload(): void {
     this.form = document.forms.namedItem('createExample');
     console.log(this.form);
     if(this.form.checkValidity()) {
      this.http.createExample(this.form).subscribe(
        data => {
          console.log(data.valueOf());
        }
      );
    }
  }

}
