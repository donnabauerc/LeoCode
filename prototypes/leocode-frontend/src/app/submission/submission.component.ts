import {Component, Injectable, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {HttpClientService} from './http-client.service';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-submission',
  templateUrl: './submission.component.html',
  styleUrls: ['./submission.component.css']
})

export class SubmissionComponent implements OnInit {

  form: HTMLFormElement;
  temp: boolean;
  res: any;

  constructor(
    public router: Router,
    public httpClient: HttpClientService
  ) { }

  ngOnInit(): void {
    this.temp = false;
    this.upload();
  }

  upload(): void {
    let dat: object;
    this.form = document.forms.namedItem('submission');
    if (this.form.checkValidity()) {
      this.httpClient.uploadForm(this.form).subscribe(
        data => {
          dat = data;
          // console.log(JSON.stringify(data));
          console.log(data.valueOf());
        }
      );
    }

    // fill form feedback for user
  }

  // onSubmit(form: NgForm): void {
  //   console.log(form.value);
  //   this.httpClient.test();
  // }
}
