import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MenuComponent } from './menu/menu.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import {RouterModule, Routes} from '@angular/router';
import { CreateExampleComponent } from './create-example/create-example.component';
import { TestExampleComponent } from './test-example/test-example.component';
import { PortfolioComponent } from './portfolio/portfolio.component';
import { ListExampleComponent } from './list-example/list-example.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';

const appRoutes: Routes = [
  {path: 'list-example', component: ListExampleComponent},
  {path: 'create-example', component: CreateExampleComponent},
  {path: 'test-example', component: TestExampleComponent},
  {path: 'portfolio', component: PortfolioComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    CreateExampleComponent,
    TestExampleComponent,
    PortfolioComponent,
    ListExampleComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    RouterModule.forRoot(appRoutes),
    MatTableModule,
    MatPaginatorModule,
    MatSortModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
