// auth.component.ts
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [NgIf, FormsModule],
  template: `
    <h2>ФИО: Иванов Иван Иванович, Группа: 12345, Вариант: 1</h2>
    <form (ngSubmit)="login()">
      <input [(ngModel)]="username" name="username" placeholder="Логин" required>
      <input [(ngModel)]="password" name="password" type="password" placeholder="Пароль" required>
      <button type="submit">Войти</button>
      <button type="button" (click)="register()">Зарегистрироваться</button>
    </form>
    <p>{{ errorMsg }}</p>
  `,
})
export class AuthComponent {
  username = '';
  password = '';
  errorMsg = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.http.post('/auth/login',
      { username: this.username, password: this.password },
      { responseType: 'text', withCredentials: true }
    ).subscribe({
      next: () => this.router.navigate(['/main']),
      error: err => this.errorMsg = err.error
    });
  }

  register() {
    this.http.post('/auth/register',
      { username: this.username, password: this.password },
      { responseType: 'text', withCredentials: true }
    ).subscribe({
      next: () => this.login(),
      error: err => this.errorMsg = err.error
    });
  }
}