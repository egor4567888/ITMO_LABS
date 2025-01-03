import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Point {
  x: number;
  y: number;
  r: number;
  hit: boolean;
}

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div>
      <button *ngFor="let val of xValues" (click)="x=val">{{val}}</button>
      <input type="number" [(ngModel)]="y" placeholder="Y (-3..3)" />
      <button *ngFor="let val of rValues" (click)="r=val; drawShapes()">{{val}}</button>
      <button (click)="sendPoint()">Проверить</button>
      <br><br>
      <canvas #canvas width="300" height="300"
        style="border:1px solid #000"
        (click)="canvasClick($event)">
      </canvas>
      <br>
      <table border="1">
        <tr><th>X</th><th>Y</th><th>R</th><th>Попадание</th></tr>
        <tr *ngFor="let p of points">
          <td>{{p.x}}</td>
          <td>{{p.y}}</td>
          <td>{{p.r}}</td>
          <td [style.color]="p.hit?'green':'red'">{{p.hit}}</td>
        </tr>
      </table>
      <br>
      <a href="#" (click)="logout()">Выйти</a>
    </div>
  `,
})
export class MainComponent implements OnInit {
  xValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
  rValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
  x = 0; y = 0; r = 1;
  points: Point[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.getPoints();
    this.drawShapes();
  }

  getPoints() {
    this.http.get<Point[]>('http://localhost:8080/points', { withCredentials: true })
      .subscribe(res => this.points = res);
  }

  sendPoint() {
    if (this.r <= 0 || this.y < -3 || this.y > 3) return;
    this.http.post<Point>('http://localhost:8080/points',
      { x: this.x, y: this.y, r: this.r }, { withCredentials: true }
    ).subscribe(res => {
      if (res) {
        this.points.push(res);
        this.drawShapes();
      }
    });
  }

  canvasClick(e: MouseEvent) {
    const canvas = e.target as HTMLCanvasElement;
    const rect = canvas.getBoundingClientRect();
    const cx = e.clientX - rect.left - 150;
    const cy = 150 - (e.clientY - rect.top);
    const scale = 100 / this.r;
    this.x = Math.round(cx / scale);
    this.y = Math.round(cy / scale);
    this.sendPoint();
  }

  drawShapes() {
    const canvas = document.querySelector('canvas');
    if (!canvas) return;
    const ctx = (canvas as HTMLCanvasElement).getContext('2d');
    if (!ctx) return;
    ctx.clearRect(0, 0, 300, 300);

    // Оси
    ctx.beginPath();
    ctx.moveTo(0, 150); ctx.lineTo(300, 150);
    ctx.moveTo(150, 0); ctx.lineTo(150, 300);
    ctx.stroke();

    // r пикселей = 100
    const scale = 100 / (this.r || 1);

    // Первый квадрант: квадрат
    ctx.fillStyle = 'rgba(0,150,0,0.3)';
    ctx.fillRect(150, 150 - 100, 100, 100);

    // Третий квадрант: треугольник
    ctx.beginPath();
    ctx.moveTo(150, 150);
    ctx.lineTo(100, 150);
    ctx.lineTo(150, 200);
    ctx.closePath();
    ctx.fillStyle = 'rgba(0,0,150,0.3)';
    ctx.fill();

    // Четвертый квадрант: четверть окружности
    ctx.beginPath();
    ctx.arc(150, 150, 100, 0, Math.PI / 2, true);
    ctx.fillStyle = 'rgba(150,0,0,0.3)';
    ctx.fill();

    // Точки
    this.points.forEach(p => {
      const px = 150 + p.x * scale;
      const py = 150 - p.y * scale;
      ctx.beginPath();
      ctx.arc(px, py, 3, 0, 2 * Math.PI);
      ctx.fillStyle = p.hit ? 'green' : 'red';
      ctx.fill();
    });
  }

  logout() {
    this.http.get('http://localhost:8080/auth/logout', { withCredentials: true, responseType: 'text' })
      .subscribe(() => {
        window.location.href = '/';
      });
  }
}