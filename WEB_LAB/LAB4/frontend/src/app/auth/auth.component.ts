import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { ResponseDTO } from '../models/response.dto';

interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  username = '';
  password = '';
  errorMsg = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.http.post<ResponseDTO<AuthTokens>>(
      '/auth/login',
      { username: this.username, password: this.password },
      { withCredentials: true, headers: { 'Content-Type': 'application/json' } }
    ).subscribe({
      next: res => {
        if (res.data) {
          localStorage.setItem('accessToken', res.data.accessToken);
          localStorage.setItem('refreshToken', res.data.refreshToken);
          this.router.navigate(['/main']);
        } else {
          this.errorMsg = res.message || 'Login failed';
        }
      },
      error: err => {
        if (err.error && err.error.message) {
          this.errorMsg = err.error.message;
        } else {
          this.errorMsg = 'An unexpected error occurred';
        }
      }
    });
  }

  register() {
    this.http.post<ResponseDTO<string>>(
      '/auth/register',
      { username: this.username, password: this.password },
      { responseType: 'json', withCredentials: true }
    ).subscribe({
      next: () => this.login(),
      error: err => {
        if (err.error && err.error.message) {
          this.errorMsg = err.error.message;
        } else {
          this.errorMsg = 'Registration failed';
        }
      }
    });
  }
}