package com.example.controller;

import com.example.model.Point;
import com.example.repository.PointRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
public class PointController {
    private final PointRepository pointRepository;

    public PointController(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @GetMapping
    public List<Point> getAllPoints(HttpSession session) {
        String user = (String) session.getAttribute("username");
        if (user == null) {
            return List.of();
        }
        return pointRepository.findByOwner(user);
    }

    @PostMapping
    public Point addPoint(@RequestBody PointRequest req, HttpSession session) {
        String user = (String) session.getAttribute("username");
        if (user == null) {
            return null;
        }
        boolean hit = checkHit(req.getX(), req.getY(), req.getR());
        Point point = new Point(req.getX(), req.getY(), req.getR(), hit, user);
        return pointRepository.save(point);
    }

    private boolean checkHit(int x, double y, int r) {
        // Первая четверть: квадрат со стороной r (0<=x<=r, 0<=y<=r)
        if (x >= 0 && y >= 0 && x <= r && y <= r) return true;
        // Третья четверть: треугольник с катетами r/2
        if (x <= 0 && y <= 0) {
            double half = r / 2.0;
            if (x >= -half && y >= -half && (y >= -half - (2.0 / r) * x)) return true;
        }
        // Четвертая четверть: четверть окружности (x>=0,y<=0, x^2+y^2<=r^2)
        if (x >= 0 && y <= 0) {
            if (x * x + y * y <= r * r) return true;
        }
        return false;
    }

    public static class PointRequest {
        private int x;
        private double y;
        private int r;

        public PointRequest() {}

        public int getX() { return x; }

        public void setX(int x) { this.x = x; }

        public double getY() { return y; }

        public void setY(double y) { this.y = y; }

        public int getR() { return r; }

        public void setR(int r) { this.r = r; }
    }
}