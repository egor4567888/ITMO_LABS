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
  templateUrl: './main.component.html',
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
    this.http.get<Point[]>('/points', { withCredentials: true })
      .subscribe(res => this.points = res);
  }
  get filteredPoints(): Point[] {
    return this.points.filter(p => p.r === this.r);
  }


  sendPoint() {
    if (this.r <= 0 || this.y < -3 || this.y > 3) return;
    this.http.post<Point>('/points', { x: this.x, y: this.y, r: this.r }, { withCredentials: true })
      .subscribe({
        next: res => {
          if (res) {
            // Логирование для проверки значений
            console.log('Получена точка:', res);
            
            this.points.push({
              x: parseFloat(res.x.toFixed(1)),
              y: parseFloat(res.y.toFixed(1)),
              r: res.r,
              hit: res.hit
            });
            this.drawShapes();
          }
        },
        error: err => {
          console.error('Ошибка при отправке точки:', err);
        }
      });
  }
  
  handleClickOnCanvas(event: MouseEvent) {
    const svg = (event.target as SVGElement).ownerSVGElement;
    if (!svg) return;

    const point = svg.createSVGPoint();
    point.x = event.clientX;
    point.y = event.clientY;

    const ctm = svg.getScreenCTM();
    if (!ctm) return;

    const svgPoint = point.matrixTransform(ctm.inverse());


    const scale = 33.3;

    const x = parseFloat(((svgPoint.x - 50) / scale).toFixed(5));
    const y = parseFloat(((50 - svgPoint.y) / scale).toFixed(5));

    if (isNaN(x) || x < -3 || x > 3) {
        alert('Значение X должно быть в диапазоне от -3 до 3.');
        return;
    }
    if (isNaN(y) || y < -5 || y > 5) {
        alert('Значение Y должно быть в диапазоне от -5 до 5.');
        return;
    }
    if (this.r < 1 || this.r > 4) {
        alert('Значение R должно быть в диапазоне от 1 до 4.');
        return;
    }

    this.x = x;
    this.y = y;
    this.sendPoint();
}

getSvgX(x: number): number {

    const scale = 33.3;
    return 50 + x * scale;
}

getSvgY(y: number): number {
    const scale = 33.3;
    return 50 - y * scale;
}

  quarterCirclePath(): string {
    return `M50,50 L50,0 A50,50 0 0,1 100,50 Z`;
  }
  drawShapes() {
    // Дополнительная логика при необходимости
  }

  logout() {
    this.http.get('http://localhost:8080/auth/logout', { withCredentials: true, responseType: 'text' })
      .subscribe(() => {
        window.location.href = '/';
      });
  }
}