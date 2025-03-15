package org.example;

import java.math.BigDecimal;

public class MethodResult {
    private final BigDecimal root;
    private final BigDecimal functionValue;
    private final int iterations;

    public MethodResult(BigDecimal root, BigDecimal functionValue, int iterations) {
        this.root = root;
        this.functionValue = functionValue;
        this.iterations = iterations;
    }

    public BigDecimal getRoot() {
        return root;
    }

    public BigDecimal getFunctionValue() {
        return functionValue;
    }

    public int getIterations() {
        return iterations;
    }
}