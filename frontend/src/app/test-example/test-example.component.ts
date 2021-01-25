import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpService} from '../services/http.service';

@Component({
  selector: 'app-test-example',
  templateUrl: './test-example.component.html',
  styleUrls: ['./test-example.component.css']
})
export class TestExampleComponent implements OnInit {

  exampleId: number;
  form: FormData;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpService) { }

  ngOnInit(): void {
    this.checkPathParam();
  }

  checkPathParam(): boolean {
    this.exampleId = +this.route.snapshot.paramMap.get('id');
    if (0 !== this.exampleId) {
      return true;
    } else {
      return false;
    }
  }

  sendSubmission(): void {
    this.form = new FormData(document.forms.namedItem('testExampleForm'));
    if (this.checkPathParam()) {
      this.form.set('example', String(this.exampleId));
    }
    this.http.testExample(this.form).subscribe(value => {
      this.router.navigate(['submission-status', value]);
    });
  }

}
