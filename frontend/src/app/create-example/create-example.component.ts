import { Component, OnInit } from '@angular/core';
import {HttpService} from '../services/http.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-create-example',
  templateUrl: './create-example.component.html',
  styleUrls: ['./create-example.component.css']
})
export class CreateExampleComponent implements OnInit {

  form: HTMLFormElement;

  constructor(private http: HttpService,
              public router: Router) { }

  ngOnInit(): void {
  }

  upload(): void {
    this.form = document.forms.namedItem('createExampleForm');
    if (this.form.checkValidity()) {
      this.http.createExample(this.form).subscribe(
        data => {
          this.router.navigate(['example', data.id]);
          },
        error => {
          alert('Sorry there has been an error!');
          console.log(error);
        }
        );
    }
  }

}
