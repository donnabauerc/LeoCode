import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-detail-example',
  templateUrl: './detail-example.component.html',
  styleUrls: ['./detail-example.component.css']
})
export class DetailExampleComponent implements OnInit {

  id: number;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id');
  }

}
