package org.example;

import java.math.BigDecimal;

public class UserInputData {
    private final MathFunction function;
    private final MethodEnum method;
    // Поля для интервалов (используются для бисекции и хорд)
    private final BigDecimal a;
    private final BigDecimal b;
    // Поля для начальных приближений (используются для методов Ньютона, простой итерации и секущих)
    private final BigDecimal x0;
    // Используется только для метода секущих
    private final BigDecimal x1;
    private final BigDecimal epsilon;

    public UserInputData(MathFunction function, MethodEnum method,
                         BigDecimal a, BigDecimal b, BigDecimal x0, BigDecimal x1, BigDecimal epsilon) {
        this.function = function;
        this.method = method;
        this.a = a;
        this.b = b;
        this.x0 = x0;
        this.x1 = x1;
        this.epsilon = epsilon;
    }

    public MathFunction getFunction() {
        return function;
    }

    public MethodEnum getMethod() {
        return method;
    }

    public BigDecimal getA() {
        return a;
    }

    public BigDecimal getB() {
        return b;
    }

    public BigDecimal getX0() {
        return x0;
    }

    public BigDecimal getX1() {
        return x1;
    }

    public BigDecimal getEpsilon() {
        return epsilon;
    }
}