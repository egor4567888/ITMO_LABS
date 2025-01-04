package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "POINTS")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double x;
    private double y;
    private double r;
    private boolean hit;
    private String owner;

    public Point() {}

    public Point(double x, double y, double r, boolean hit, String owner) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean isHit() {
        return hit;
    }

    public String getOwner() {
        return owner;
    }
}