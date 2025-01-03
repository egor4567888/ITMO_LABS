package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "POINTS")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private double y;
    private int r;
    private boolean hit;
    private String owner;

    public Point() {}

    public Point(int x, double y, int r, boolean hit, String owner) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public boolean isHit() {
        return hit;
    }

    public String getOwner() {
        return owner;
    }
}