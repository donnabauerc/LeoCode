import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import { PortfolioDataSource, PortfolioItem } from './portfolio-datasource';
import {Submission} from '../model/submission.model';
import {HttpService} from '../services/http.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<Submission>;
  dataSource: MatTableDataSource<Submission>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'status', 'result', 'lastTimeChanged'];

  constructor(private http: HttpService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<Submission>();
    this.refreshData(this.route.snapshot.paramMap.get('username'));
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  refreshData(username: string): void {
    this.http.getFinishedSubmissions(username).subscribe(value => this.dataSource.data = value, error => console.log(error));
  }
}
