package com.example.dto;

import java.time.LocalDateTime;

public class PointDTO {
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private LocalDateTime createdAt;

    public PointDTO() {}

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getR() { return r; }
    public void setR(double r) { this.r = r; }
    public boolean isHit() { return hit; }
    public void setHit(boolean hit) { this.hit = hit; }

    public LocalDateTime getCreatedAt() { 
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt;
    }
}