package numMethods;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BisectionMethod {

    public static class IterationData {
        public int iteration;
        public double a;
        public double b;
        public double c;
        public double fa;
        public double fb;
        public double fc;

        public IterationData(int iteration, double a, double b, double c, double fa, double fb, double fc) {
            this.iteration = iteration;
            this.a = a;
            this.b = b;
            this.c = c;
            this.fa = fa;
            this.fb = fb;
            this.fc = fc;
        }
    }

    // Accept variable name as parameter
    public static List<IterationData> bisectionMethod(String funcStr, String variable, double a, double b, double tolerance) throws Exception {
        final int MAX_ITERATIONS = 1000;
        Expression function = new ExpressionBuilder(funcStr).variable(variable).build();
        List<IterationData> iterations = new ArrayList<>();

        double fa = evaluate(function, variable, a);
        double fb = evaluate(function, variable, b);

        if (fa * fb >= 0) {
            throw new IllegalArgumentException("Function must have opposite signs at endpoints a and b.");
        }

        for (int i = 1; i <= MAX_ITERATIONS; i++) {
            double c = (a + b) / 2;
            double fc = evaluate(function, variable, c);

            iterations.add(new IterationData(i, a, b, c, fa, fb, fc));

            if (Math.abs(fc) < tolerance || (b - a) / 2 < tolerance) {
                break;
            }

            if (fc * fa < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }

        return iterations;
    }

    // Use the variable name
    private static double evaluate(Expression f, String variable, double x) {
        f.setVariable(variable, x);
        return f.evaluate();
    }

    // ... (printIterationTable and printSolution can be left unchanged or adapted to show the variable name)
}