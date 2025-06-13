package numMethods;

import java.util.Stack;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Secant {

    // New: Allow variable name
    public static double evaluateFunction(String function, String variable, double value) {
        Expression expr = new ExpressionBuilder(function)
                .variable(variable)
                .build()
                .setVariable(variable, value);
        return expr.evaluate();
    }

    // Keep the old version for compatibility (defaults to "x")
    public static double evaluateFunction(String function, double x) {
        return evaluateFunction(function, "x", x);
    }

    // Overload runSecant for variable name support
    public static void runSecant(Stack<Double> stack, String function, String variable, double tol, StringBuilder solvingSteps) {
        if (isConverged(stack, tol)) {
            double x0 = stack.get(stack.size() - 2);
            double x1 = stack.get(stack.size() - 1);
            double f0 = evaluateFunction(function, variable, x0);
            double f1 = evaluateFunction(function, variable, x1);
            double denominator = f1 - f0;

            if (denominator == 0) {
                throw new ArithmeticException("Division by zero in secant method.");
            }

            double next = x1 - f1 * (x1 - x0) / denominator;
            double slope = denominator / (x1 - x0);

            solvingSteps.append(String.format("%s%d = %s - f(%s)/f'(%s) = %.4f - (%.4f) / (%.4f) = %.4f\n",
                    variable, stack.size(), x1, variable, variable, x1, f1, slope, next));
            return;
        }

        double x0 = stack.get(stack.size() - 2);
        double x1 = stack.get(stack.size() - 1);
        double f0 = evaluateFunction(function, variable, x0);
        double f1 = evaluateFunction(function, variable, x1);
        double denominator = f1 - f0;

        if (denominator == 0) {
            throw new ArithmeticException("Division by zero in secant method.");
        }

        double next = x1 - f1 * (x1 - x0) / denominator;
        double slope = denominator / (x1 - x0);

        solvingSteps.append(String.format("%s%d = %s - f(%s)/f'(%s) = %.4f - (%.4f) / (%.4f) = %.4f\n",
                variable, stack.size(), x1, variable, variable, x1, f1, slope, next));

        stack.push(next);
        runSecant(stack, function, variable, tol, solvingSteps);
    }

    // Keep the original for compatibility (assumes "x")
    public static void runSecant(Stack<Double> stack, String function, double tol, StringBuilder solvingSteps) {
        runSecant(stack, function, "x", tol, solvingSteps);
    }

    // The rest of your class remains unchanged
    public static boolean isConverged(Stack<Double> stack, double tol) {
        int size = stack.size();
        if (size < 2) return false;
        return Math.abs(stack.peek() - stack.get(size - 2)) < tol;
    }

    public static double nextApprox(Stack<Double> stack, String function) {
        int size = stack.size();
        double x0 = stack.get(size - 2);
        double x1 = stack.get(size - 1);
        double f0 = evaluateFunction(function, x0);
        double f1 = evaluateFunction(function, x1);

        if (f1 - f0 == 0) {
            throw new ArithmeticException("Division by zero in secant method.");
        }

        return x1 - f1 * (x1 - x0) / (f1 - f0);
    }

    public static int countIterations(Stack<Double> stack) {
        if (stack.isEmpty()) return 0;
        Stack<Double> temp = new Stack<>();
        temp.addAll(stack);
        return countHelper(temp, 0);
    }

    public static int countHelper(Stack<Double> stack, int count) {
        if (stack.isEmpty()) return count;
        stack.pop();
        return countHelper(stack, count + 1);
    }
}