import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {MenuComponent} from './menu/menu.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {PortfolioComponent} from './portfolio/portfolio.component';
import {TestExampleComponent} from './test-example/test-example.component';
import {RouterModule, Routes} from '@angular/router';
import {ListExampleComponent} from './list-example/list-example.component';
import {DetailExampleComponent} from './detail-example/detail-example.component';
import {CreateExampleComponent} from './create-example/create-example.component';
import {FileNotFoundComponent} from './file-not-found/file-not-found.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {LayoutModule} from '@angular/cdk/layout';
import {MatSortModule} from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {HttpClientModule} from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { ReactiveFormsModule } from '@angular/forms';

const appRoutes: Routes = [
  {path: 'create-example', component: CreateExampleComponent},
  {path: 'test-example', component: TestExampleComponent},
  {path: 'portfolio', component: PortfolioComponent},
  {path: 'list-example', component: ListExampleComponent},
  {path: 'example/:id', component: DetailExampleComponent},
  {path: 'test-example/:id', component: TestExampleComponent},
  {path: '',  redirectTo: '/list-example', pathMatch: 'full' },
  {path: '**', component: FileNotFoundComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    CreateExampleComponent,
    TestExampleComponent,
    PortfolioComponent,
    ListExampleComponent,
    DetailExampleComponent
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
    MatSortModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
