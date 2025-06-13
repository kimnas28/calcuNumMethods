package numMethods;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FixedPoint {
    public static double evaluateG(String gx, double x) {
        Expression expression = new ExpressionBuilder(gx)
                .variable("x")
                .build()
                .setVariable("x", x);
        return expression.evaluate();
    }

    public static void printIterations(List<Double> results, String gx) {
        System.out.println("\nIteration Table:");
        System.out.printf("%-6s %-12s %-12s\n", "Iter", "x", "g(x)");
        System.out.println("------ ------------ ------------");

        for (int i = 0; i < results.size() - 1; i++) {
            double x = results.get(i);
            double gxVal = evaluateG(gx, x);
            System.out.printf("%-6d %-12.6f %-12.6f\n", i + 1, x, gxVal);
        }
    }

    public static void printSolvingSteps(List<Double> results, String gx) {
        System.out.println("\nSolving Steps:");
        for (int i = 1; i < results.size(); i++) {
            double prev = results.get(i - 1);
            double curr = results.get(i);
            System.out.printf("x%d = g(x%d-1) = g(%.6f) = %.6f\n", i, i, prev, curr);
        }
    }

    // Improved e^x style to exp(x) even with/without parenthesis and spaces
    public static String convertExpNotation(String gx) {
        // Handles 'e^x', 'e^3x', 'e^(x+1)', etc.
        // This regex matches 'e^' followed by an optional '(' then a valid expression for exp4j
        return gx.replaceAll("e\\^\\s*([a-zA-Z0-9_+\\-*/^().]+)", "exp($1)");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input g(x)
        System.out.print("Enter the function g(x): ");
        String gx = scanner.nextLine();

        // Auto-convert e^x style to exp(x)
        gx = convertExpNotation(gx);

        // Input initial guess
        System.out.print("Enter initial guess x0: ");
        double x0 = scanner.nextDouble();
        scanner.nextLine(); // clear newline

        // Input decimal places for tolerance
        int decimalPlaces;
        while (true) {
            System.out.print("Enter number of decimal places for tolerance (e.g., 4 for 1e-4): ");
            String line = scanner.nextLine();
            try {
                decimalPlaces = Integer.parseInt(line);
                if (decimalPlaces < 1) {
                    System.out.println("Please enter a positive integer.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter an integer.");
            }
        }

        double epsilon = Math.pow(10, -decimalPlaces);
        int maxIter = 100;

        List<Double> results = new ArrayList<>();
        results.add(x0);

        try {
            int iteration = 0;
            double xPrev = x0;

            while (iteration < maxIter) {
                double xNext = evaluateG(gx, xPrev);
                results.add(xNext);

                if (Math.abs(xNext - xPrev) < epsilon) {
                    break;
                }

                xPrev = xNext;
                iteration++;
            }

            printIterations(results, gx);
            printSolvingSteps(results, gx);

            System.out.println("\nFinal solution: Root ≈ " +
                    String.format("%." + decimalPlaces + "f", results.get(results.size() - 1)));

        } catch (Exception e) {
            System.out.println("Invalid expression or error occurred: " + e.getMessage());
        }

        scanner.close();
    }
}