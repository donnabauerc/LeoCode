import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpService} from '../services/http.service';
import {Example} from '../model/example.model';

@Component({
  selector: 'app-detail-example',
  templateUrl: './detail-example.component.html',
  styleUrls: ['./detail-example.component.css']
})
export class DetailExampleComponent implements OnInit {

  example: Example;

  constructor(private route: ActivatedRoute,
              private http: HttpService) { }

  ngOnInit(): void {
    this.getData(+this.route.snapshot.paramMap.get('id'));
  }

  getData(id: number): void {
    this.http.getExampleById(id).subscribe(value => this.example = value);
  }

}
