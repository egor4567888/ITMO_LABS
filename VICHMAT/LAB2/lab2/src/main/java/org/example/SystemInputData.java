package org.example;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class SystemInputData {
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> phi1;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> phi2;
    private final BigDecimal x0;
    private final BigDecimal y0;
    private final BigDecimal epsilon;

    public SystemInputData(BiFunction<BigDecimal, BigDecimal, BigDecimal> phi1,
                           BiFunction<BigDecimal, BigDecimal, BigDecimal> phi2,
                           BigDecimal x0, BigDecimal y0, BigDecimal epsilon) {
        this.phi1 = phi1;
        this.phi2 = phi2;
        this.x0 = x0;
        this.y0 = y0;
        this.epsilon = epsilon;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getPhi1() {
        return phi1;
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getPhi2() {
        return phi2;
    }

    public BigDecimal getX0() {
        return x0;
    }

    public BigDecimal getY0() {
        return y0;
    }

    public BigDecimal getEpsilon() {
        return epsilon;
    }
}