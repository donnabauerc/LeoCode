import { Component, OnInit } from '@angular/core';
import {HttpService} from '../services/http.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-submission-status',
  templateUrl: './submission-status.component.html',
  styleUrls: ['./submission-status.component.css']
})
export class SubmissionStatusComponent implements OnInit {

  submissionId: number;
  submissionStatus: string;

  constructor(private http: HttpService,
              private route: ActivatedRoute,
              public router: Router) { }

  ngOnInit(): void {
    this.submissionId = +this.route.snapshot.paramMap.get('id');
    this.getSubmissionStatus(this.submissionId);
  }

  getSubmissionStatus(id: number): void {
    this.http.getSubmissionStatusSse(id).subscribe(messageEvent => {
      this.submissionStatus = messageEvent.data;
    }, error => {
      console.log(error);
    });
  }

}
