package org.example;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum MathFunctionSystem {
    FIRST(
            "Система 1: f₁(x,y)= x² + x + y² - 1,  f₂(x,y)= x - y",
            // f1(x,y) = x^2 + x + y^2 - 1
            (x, y) -> x.multiply(x).add(x).add(y.multiply(y)).subtract(BigDecimal.ONE),
            // f2(x,y) = x - y
            (x, y) -> x.subtract(y),
            // xPhi derivative sum: 2y+2x
            (x, y) -> x.abs().multiply(new BigDecimal("2")).add(y.abs().multiply(new BigDecimal("2"))),
            // yPhi derivative sum: 1
            (x, y) -> BigDecimal.valueOf(0.9),
            // xPhi: x = 1 - y² - x²  (итерационная функция для x, например)
            (x, y) -> {
                BigDecimal one = BigDecimal.ONE;
                BigDecimal ySquared = y.multiply(y);
                BigDecimal xSquared = x.multiply(x);
                return one.subtract(ySquared).subtract(xSquared);
            },
            // yPhi: y = x
            (x, y) -> x
    ),
    SECOND(
            "Система 2: f₁(x,y)= y² - x + 1,  f₂(x,y)= x² - y - 1",
            // f1(x,y) = y^2 - x + 1
            (x, y) -> y.multiply(y).subtract(x).add(BigDecimal.ONE),
            // f2(x,y) = x^2 - y - 1
            (x, y) -> x.multiply(x).subtract(y).subtract(BigDecimal.ONE),
            // xPhi derivative sum: 2y
            (x, y) -> y.abs().multiply(new BigDecimal("2")),
            // yPhi derivative sum: 2x
            (x, y) -> x.abs().multiply(new BigDecimal("2")),
            // xPhi: x = y² + 1
            (x, y) -> y.multiply(y).add(BigDecimal.ONE),
            // yPhi: y = x² - 1
            (x, y) -> x.multiply(x).subtract(BigDecimal.ONE)
    ),
    THIRD(
            "Система 3: f₁(x,y)= eʸ + x - 2,  f₂(x,y)= x + y - 0.5",
            // f1(x,y) = e^y + x - 2
            (x, y) -> {
                BigDecimal eY = BigDecimal.valueOf(Math.exp(y.doubleValue()));
                return eY.add(x).subtract(BigDecimal.valueOf(2));
            },
            // f2(x,y) = x + y - 0.5
            (x, y) -> x.add(y).subtract(new BigDecimal("0.5")),
            // xPhi derivative sum:  e^y
            (x, y) -> {
                BigDecimal eY = BigDecimal.valueOf(Math.exp(y.doubleValue()));
                return eY;
            },
            // yPhi derivative sum: 1  (заданная константа)
            (x, y) -> BigDecimal.valueOf(0.9),
            // xPhi: x = 2 - e^y
            (x, y) -> {
                BigDecimal eY = BigDecimal.valueOf(Math.exp(y.doubleValue()));
                return BigDecimal.valueOf(2).subtract(eY);
            },
            // yPhi: y = 0.5 - x
            (x, y) -> BigDecimal.valueOf(0.5).subtract(x)
    );

    private final String description;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> f1;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> f2;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> f1DerivativeSum;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> f2DerivativeSum;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> xPhi;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> yPhi;

    MathFunctionSystem(
            String description,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f1,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f2,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f1DerivativeSum,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f2DerivativeSum,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> xPhi,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> yPhi
    ) {
        this.description = description;
        this.f1 = f1;
        this.f2 = f2;
        this.f1DerivativeSum = f1DerivativeSum;
        this.f2DerivativeSum = f2DerivativeSum;
        this.xPhi = xPhi;
        this.yPhi = yPhi;
    }

    public String getDescription() {
        return description;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getF1() {
        return f1;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getF2() {
        return f2;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getF1DerivativeSum() {
        return f1DerivativeSum;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getF2DerivativeSum() {
        return f2DerivativeSum;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getxPhi() {
        return xPhi;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getyPhi() {
        return yPhi;
    }

    public static MathFunctionSystem fromChoice(int choice) {
        switch (choice) {
            case 1: return FIRST;
            case 2: return SECOND;
            case 3: return THIRD;
            default:
                throw new IllegalArgumentException("Недопустимый выбор системы: " + choice);
        }
    }
}