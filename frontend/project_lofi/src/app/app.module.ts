import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatSidenavModule } from  '@angular/material/sidenav';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatDividerModule } from '@angular/material/divider'
import { MatListModule} from '@angular/material/list'
import { MatAutocompleteModule } from '@angular/material/autocomplete'
import { MatButtonToggleModule } from '@angular/material/button-toggle'
import { MatCardModule } from '@angular/material/card'
import { MatCheckboxModule } from '@angular/material/checkbox'
import { MatChipsModule } from '@angular/material/chips'
import { MatMenuModule } from '@angular/material/menu'
import { MatInputModule } from '@angular/material/input'
import { MatSelectModule } from '@angular/material/select'
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field'

// used to create fake backend
// import { fakeBackendProvider } from './_helpers/fake-backend';
import { AppRoutingModule } from './app-routing.module';
// import { JwtInterceptor } from './_helpers/jwt.interceptor';
import { ErrorInterceptor } from './_helpers/error.interceptor';
import { AppComponent } from './app.component';
import { AlertComponent } from './_components/alert.component';
import { HomeComponent } from './home/home.component';
import { PlaylistDialogComponent } from './home/playlist-dialog/playlist-dialog.component';
import { PullDialogComponent } from './home/pull-dialog/pull-dialog.component';
import { SearchComponent } from './search/search.component';
import { LofiComponent } from './lofi/lofi.component';
import { RemoveLofiComponenet } from './lofi/remove-dialog/remove.component';
import { AssignLofiComponent } from './lofi/assign-dialog/assign.component';
import { ValidationComponent } from './_components/validation/validation.component';

@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        AppRoutingModule,
        MatSidenavModule,
        BrowserAnimationsModule,
        FormsModule,

        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatDividerModule,
        MatAutocompleteModule,
        MatButtonToggleModule,
        MatCardModule,
        MatCheckboxModule,
        MatChipsModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatSelectModule,
        MatDialogModule,
        MatFormFieldModule
    ],
    declarations: [
        AppComponent,
        AlertComponent,
        HomeComponent,
        PlaylistDialogComponent,
        PullDialogComponent,
        SearchComponent,
        LofiComponent,
        RemoveLofiComponenet,
        AssignLofiComponent,
        ValidationComponent,
    ],
    providers: [
        // { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },

        // provider used to create fake backend
        // fakeBackendProvider
    ],
    bootstrap: [AppComponent]
})
export class AppModule { };
