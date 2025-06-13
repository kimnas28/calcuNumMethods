package numMethods;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.List;

public class NewtonRaphson {

    public static class IterationData {
        public int iteration;
        public double x;
        public double fx;
        public double dfx;
        public double xNew;

        public IterationData(int iteration, double x, double fx, double dfx, double xNew) {
            this.iteration = iteration;
            this.x = x;
            this.fx = fx;
            this.dfx = dfx;
            this.xNew = xNew;
        }
    }

    /**
     * Newton-Raphson method for any variable name. Returns all iterations.
     */
    public static List<IterationData> newtonRaphson(String funcStr, String variable, double x0, double tolerance) throws Exception {
        final double DEFAULT_H = 1e-5;
        final int MAX_ITERATIONS = 1000;
        Expression function = new ExpressionBuilder(funcStr).variable(variable).build();

        List<IterationData> iterations = new ArrayList<>();
        double x = x0;
        double h = DEFAULT_H;

        for (int i = 1; i <= MAX_ITERATIONS; i++) {
            double fx = evaluate(function, variable, x);
            double dfx = derivative(function, variable, x, h);

            if (Math.abs(dfx) < 1e-10) {
                throw new ArithmeticException("Derivative too small, division by zero risk at x = " + x);
            }

            double xNew = x - fx / dfx;

            iterations.add(new IterationData(i, x, fx, dfx, xNew));

            // Debug output for each iteration (optional, can be removed)
            // System.out.printf("Iter %d: x=%.8f f(x)=%.8f f'(x)=%.8f xNew=%.8f\n", i, x, fx, dfx, xNew);

            if (Math.abs(xNew - x) < tolerance) {
                break;
            }

            x = xNew;
            h = DEFAULT_H * (1 + Math.abs(x));
        }

        return iterations;
    }

    // Evaluate for any variable name
    private static double evaluate(Expression f, String variable, double x) {
        f.setVariable(variable, x);
        return f.evaluate();
    }

    // Numerical derivative for any variable name
    private static double derivative(Expression f, String variable, double x, double h) {
        return (evaluate(f, variable, x + h) - evaluate(f, variable, x - h)) / (2 * h);
    }
}