package org.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Function;

public enum MathFunction {
    FIRST(
        "f(x) = -2.7*x^3 - 1.48*x^2 + 19.23*x + 6.35",
        x -> {
            // MC для операций с BigDecimal
            MathContext mc = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
            return new BigDecimal("-2.7", mc).multiply(x.pow(3, mc), mc)
                    .add(new BigDecimal("-1.48", mc).multiply(x.pow(2, mc), mc), mc)
                    .add(new BigDecimal("19.23", mc).multiply(x, mc), mc)
                    .add(new BigDecimal("6.35", mc), mc);
        },
        x -> {
            MathContext mc = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
            return new BigDecimal("-8.1", mc).multiply(x.pow(2, mc), mc)
                    .add(new BigDecimal("-2.96", mc).multiply(x, mc), mc)
                    .add(new BigDecimal("19.23", mc), mc);
        }
    ),
    SECOND(
        "f(x) = e^x + x^2 - 10*x",
        x -> {
            MathContext mc = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
            
            BigDecimal expX = new BigDecimal(Math.exp(x.doubleValue()), mc);
            return expX.add(x.pow(2, mc), mc)
                       .subtract(new BigDecimal("10", mc).multiply(x, mc), mc);
        },
        x -> {
            MathContext mc = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
            BigDecimal expX = new BigDecimal(Math.exp(x.doubleValue()), mc);
            return expX.add(new BigDecimal("2", mc).multiply(x, mc), mc)
                       .subtract(new BigDecimal("10", mc), mc);
        }
    ),
    THIRD(
        "f(x) = x^3 - x",
        x -> {
            MathContext mc = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
            return x.pow(3, mc).subtract(x, mc);
        },
        x -> {
            MathContext mc = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
            return new BigDecimal("3", mc).multiply(x.pow(2, mc), mc)
                    .subtract(BigDecimal.ONE, mc);
        }
    );

    private static final MathContext MC = new MathContext(MathFunction.num, RoundingMode.HALF_UP);
    private final String description;
    private final Function<BigDecimal, BigDecimal> function;
    private final Function<BigDecimal, BigDecimal> derivative;
    private  final static int num = 30;

    MathFunction(String description,
                 Function<BigDecimal, BigDecimal> function,
                 Function<BigDecimal, BigDecimal> derivative) {
        this.description = description;
        this.function = function;
        this.derivative = derivative;
    }

    public String getDescription() {
        return description;
    }

    public Function<BigDecimal, BigDecimal> getFunction() {
        return function;
    }

    public Function<BigDecimal, BigDecimal> getDerivative() {
        return derivative;
    }

    public static MathFunction fromChoice(int choice) {
        switch (choice) {
            case 1:
                return FIRST;
            case 2:
                return SECOND;
            case 3:
                return THIRD;
            default:
                throw new IllegalArgumentException("Некорректный выбор функции.");
        }
    }
}