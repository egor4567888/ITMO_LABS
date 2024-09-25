package com.egor456788;

import java.awt.*;

public class AnimationObject {
    int id;
    int x, y, finalRadius;
    int n = 20;
    float currentRadius = 0;
    Color color;

    AnimationObject(int x, int y, int finalRadius, Color color,int id) {
        this.x = x;
        this.y = y;
        this.finalRadius = finalRadius;
        this.color = color;
        this.id = id;
    }

    void animate() {
        currentRadius += ((float)finalRadius)/n;
        if (currentRadius > finalRadius) {
            currentRadius = finalRadius;
        }
    }

    boolean isComplete() {
        return currentRadius >= finalRadius;
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, (int) currentRadius, (int) currentRadius);
    }
}

