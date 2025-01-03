import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app/app.component';
import { AuthComponent } from './app/auth/auth.component';
import { MainComponent } from './app/main/main.component';

const routes: Routes = [
  { path: '', component: AuthComponent },
  { path: 'main', component: MainComponent }
];

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    FormsModule
  ]
}).catch(err => console.error(err));