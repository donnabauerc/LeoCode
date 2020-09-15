import { Component, OnInit } from '@angular/core';
import {HttpClientService} from '../submission/http-client.service';

@Component({
  selector: 'app-example-list',
  templateUrl: './example-list.component.html',
  styleUrls: ['./example-list.component.css']
})
export class ExampleListComponent implements OnInit {

  examples = [];
  isLoading = false;

  constructor(
    public httpClient: HttpClientService
  ) { }

  ngOnInit(): void {
    this.fetchExamples();
  }

  fetchExamples(): void {
    this.isLoading = true;
    this.httpClient.listAllExamples().subscribe(
      data => {
        this.examples = data;
        console.log(data);
        this.isLoading = false;
      }
    );
    const e = this.examples[0];
    console.log(e.id);
  }
}
