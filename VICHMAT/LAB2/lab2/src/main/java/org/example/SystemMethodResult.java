package org.example;

import java.math.BigDecimal;

public class SystemMethodResult {
    private final BigDecimal x;
    private final BigDecimal y;
    private final int iterations;
    private final BigDecimal errorX;
    private final BigDecimal errorY;

    public SystemMethodResult(BigDecimal x, BigDecimal y, int iterations, BigDecimal errorX, BigDecimal errorY) {
        this.x = x;
        this.y = y;
        this.iterations = iterations;
        this.errorX = errorX;
        this.errorY = errorY;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public int getIterations() {
        return iterations;
    }

    public BigDecimal getErrorX() {
        return errorX;
    }

    public BigDecimal getErrorY() {
        return errorY;
    }
}