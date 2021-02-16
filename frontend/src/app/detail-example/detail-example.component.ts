import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import { DetailExampleDataSource, DetailExampleItem } from './detail-example-datasource';
import {Example} from '../model/example.model';
import {HttpService} from '../services/http.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-detail-example',
  templateUrl: './detail-example.component.html',
  styleUrls: ['./detail-example.component.css']
})
export class DetailExampleComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<Example>;
  dataSource: MatTableDataSource<Example>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'type', 'files', 'test'];

  constructor(private route: ActivatedRoute,
              public router: Router,
              private http: HttpService) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<Example>();
    this.refreshData(+this.route.snapshot.paramMap.get('id'));
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  refreshData(id: number): void {
    // because just one value is returned but material requires array
    this.http.getExampleById(id).subscribe(value => {
      if (value === null) {
        this.router.navigate(['NotFound']);
      } else {
        this.dataSource.data = [value];
      }
    }, error => {
      console.log(error);
    });
  }
}
