package org.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Methods {

    static final MathContext MC = new MathContext(30, RoundingMode.HALF_UP);

    public static MethodResult bisectionMethod(Function<BigDecimal, BigDecimal> f, BigDecimal a, BigDecimal b, BigDecimal epsilon) {
        BigDecimal c = BigDecimal.ZERO;
        int iterations = 0;
        while (b.subtract(a, MC).abs().compareTo(epsilon) >= 0) {
            iterations++;
            c = a.add(b, MC).divide(new BigDecimal("2"), MC);
            if (f.apply(c).compareTo(BigDecimal.ZERO) == 0) {
                break;
            } else if (f.apply(c).multiply(f.apply(a), MC).compareTo(BigDecimal.ZERO) < 0) {
                b = c;
            } else {
                a = c;
            }
        }
        return new MethodResult(c, f.apply(c), iterations);
    }

    public static MethodResult chordMethod(Function<BigDecimal, BigDecimal> f, BigDecimal a, BigDecimal b, BigDecimal epsilon) {
        BigDecimal x = BigDecimal.ZERO;
        int iterations = 0;
        while (true) {
            iterations++;
            BigDecimal numerator = f.apply(a).multiply(b.subtract(a, MC), MC);
            BigDecimal denominator = f.apply(b).subtract(f.apply(a), MC);
            x = a.subtract(numerator.divide(denominator, MC), MC);
            if (f.apply(x).abs().compareTo(epsilon) < 0) {
                break;
            }
            if (f.apply(a).multiply(f.apply(x), MC).compareTo(BigDecimal.ZERO) < 0) {
                b = x;
            } else {
                a = x;
            }
        }
        return new MethodResult(x, f.apply(x), iterations);
    }

    public static MethodResult simpleIteration(Function<BigDecimal, BigDecimal> phi, Function<BigDecimal, BigDecimal> f, BigDecimal x0, BigDecimal epsilon) {
        int iterations = 0;
        BigDecimal x1;
        while (true) {
            iterations++;
            x1 = phi.apply(x0);
            if (x1.subtract(x0, MC).abs().compareTo(epsilon) < 0) {
                break;
            }
            x0 = x1;
        }
        return new MethodResult(x1, f.apply(x1), iterations);
    }

    public static MethodResult secantMethod(Function<BigDecimal, BigDecimal> f, BigDecimal x0, BigDecimal x1, BigDecimal epsilon) {
        int iterations = 0;
        BigDecimal x2;
        while (true) {
            iterations++;
            BigDecimal numerator = f.apply(x1).multiply(x1.subtract(x0, MC), MC);
            BigDecimal denominator = f.apply(x1).subtract(f.apply(x0), MC);
            x2 = x1.subtract(numerator.divide(denominator, MC), MC);
            if (x2.subtract(x1, MC).abs().compareTo(epsilon) < 0) {
                break;
            }
            x0 = x1;
            x1 = x2;
        }
        return new MethodResult(x2, f.apply(x2), iterations);
    }

    public static MethodResult newtonMethod(Function<BigDecimal, BigDecimal> f, Function<BigDecimal, BigDecimal> df, BigDecimal x0, BigDecimal epsilon) {
        int iterations = 0;
        BigDecimal x1;
        while (true) {
            iterations++;
            BigDecimal fx = f.apply(x0);
            BigDecimal dfx = df.apply(x0);
            x1 = x0.subtract(fx.divide(dfx, MC), MC);
            if (x1.subtract(x0, MC).abs().compareTo(epsilon) < 0) {
                break;
            }
            x0 = x1;
        }
        return new MethodResult(x1, f.apply(x1), iterations);
    }

    // Методы, принимающие объект UserInputData:

    public static MethodResult bisectionMethod(UserInputData input) {
        return bisectionMethod(
                input.getFunction().getFunction(),
                input.getA(),
                input.getB(),
                input.getEpsilon()
        );
    }

    public static MethodResult chordMethod(UserInputData input) {
        return chordMethod(
                input.getFunction().getFunction(),
                input.getA(),
                input.getB(),
                input.getEpsilon()
        );
    }

    public static MethodResult simpleIteration(UserInputData input) {
        BigDecimal a = input.getA();
        BigDecimal b = input.getB();
        // Вычисляем производные в концах интервала
        BigDecimal fpa = input.getFunction().getDerivative().apply(a);
        BigDecimal fpb = input.getFunction().getDerivative().apply(b);
        BigDecimal lambda;
        int signA = fpa.signum();
        int signB = fpb.signum();
        if (signA > 0 && signB > 0) {
            lambda = BigDecimal.ONE.negate().divide(fpa.abs().max(fpb.abs()), MC);
        } else if (signA < 0 && signB < 0) {
            lambda = BigDecimal.ONE.divide(fpa.abs().max(fpb.abs()), MC);
        } else {
            throw new IllegalArgumentException("Не выполнено условие для выбора lambda у метода простой итерации.");
        }
        // Проверка условия сходимости: max(|1 + lambda*f'(a)|, |1 + lambda*f'(b)|) < 1 - epsilon
        BigDecimal phiPrimeA = (BigDecimal.ONE.add(lambda.multiply(fpa, MC), MC)).abs();
        BigDecimal phiPrimeB = (BigDecimal.ONE.add(lambda.multiply(fpb, MC), MC)).abs();
        BigDecimal maxPhiPrime = phiPrimeA.max(phiPrimeB);
        BigDecimal convergenceBound = BigDecimal.ONE.subtract(input.getEpsilon(), MC);
        if (maxPhiPrime.compareTo(convergenceBound) >= 0) {
            throw new IllegalArgumentException("Условие сходимости метода простой итерации не выполнено: " +
                    "max(|phi'(a)|,|phi'(b)|) = " + maxPhiPrime.toPlainString() +
                    " >= " + convergenceBound.toPlainString());
        }
        Function<BigDecimal, BigDecimal> phi = x -> x.add(lambda.multiply(input.getFunction().getFunction().apply(x), MC), MC);
        return simpleIteration(
                phi,
                input.getFunction().getFunction(),
                input.getX0(),
                input.getEpsilon()
        );
    }

    public static MethodResult secantMethod(UserInputData input) {
        return secantMethod(
                input.getFunction().getFunction(),
                input.getX0(),
                input.getX1(),
                input.getEpsilon()
        );
    }

    public static MethodResult newtonMethod(UserInputData input) {
        return newtonMethod(
                input.getFunction().getFunction(),
                input.getFunction().getDerivative(),
                input.getX0(),
                input.getEpsilon()
        );
    }
      public static SystemMethodResult simpleIterationSystem(SystemInputData inputData) {
        BiFunction<BigDecimal, BigDecimal, BigDecimal> phi1 = inputData.getPhi1();
        BiFunction<BigDecimal, BigDecimal, BigDecimal> phi2 = inputData.getPhi2();
        BigDecimal x0 = inputData.getX0();
        BigDecimal y0 = inputData.getY0();
        BigDecimal epsilon = inputData.getEpsilon();

        int iterations = 0;
        BigDecimal x_new, y_new;

        while (true) {
            iterations++;
            if (iterations > 100) {
                throw new IllegalArgumentException("Превышено максимальное число итераций (1000)");
            }
            
            x_new = phi1.apply(x0, y0);
            y_new = phi2.apply(x0, y0);
            
            if (x_new.subtract(x0, MC).abs().compareTo(epsilon) < 0 &&
                y_new.subtract(y0, MC).abs().compareTo(epsilon) < 0) {
                break;
            }
            x0 = x_new;
            y0 = y_new;
        }

        BigDecimal errorX = x_new.subtract(x0, MC).abs();
        BigDecimal errorY = y_new.subtract(y0, MC).abs();

        return new SystemMethodResult(x_new, y_new, iterations, errorX, errorY);
    }
}