package com.example.controller;

import com.example.model.Point;
import com.example.repository.PointRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/points")
public class PointController {
    private final PointRepository pointRepository;
    private static final Logger logger = LoggerFactory.getLogger(PointController.class);

    public PointController(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @GetMapping
    public List<Point> getAllPoints() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user == null) {
            return List.of();
        }
        return pointRepository.findByOwner(user);
    }

    @PostMapping
    public Point addPoint(@RequestBody PointRequest req) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user == null) {
            return null;
        }
        boolean hit = checkHit(req.getX(), req.getY(), req.getR());
        Point point = new Point(req.getX(), req.getY(), req.getR(), hit, user);
        return pointRepository.save(point);
    }

    private boolean checkHit(double x, double y, double r) {
        if (x >= 0 && y >= 0 && x <= r && y <= r) return true;

        if (x >= 0 && y <= 0) {
            double half = r / 2.0;
            if (x - y <= half) return true;
        }

        if (x <= 0 && y <= 0) {
            double half = r / 2.0;
            if (x * x + y * y <= half * half) return true;
        }
        return false;
    }

    public static class PointRequest {
        private double x;
        private double y;
        private double r;

        public PointRequest() {}

        public double getX() { return x; }

        public void setX(double x) { this.x = x; }

        public double getY() { return y; }

        public void setY(double y) { this.y = y; }

        public double getR() { return r; }

        public void setR(double r) { this.r = r; }
    }
}