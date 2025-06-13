package numMethods;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class FalsePosition {

    public static class FalsePositionIteration {
        public int iteration;
        public double xL;
        public double xR;
        public double fXL;
        public double fXR;
        public double xNew;
        public double fXNew;

        public FalsePositionIteration(int iteration, double xL, double xR, double fXL, double fXR, double xNew, double fXNew) {
            this.iteration = iteration;
            this.xL = xL;
            this.xR = xR;
            this.fXL = fXL;
            this.fXR = fXR;
            this.xNew = xNew;
            this.fXNew = fXNew;
        }
    }

    // Accept variable name as argument
    public static List<FalsePositionIteration> falsePosition(String expressionString, String variable, double xL, double xR, double epsilon) {
        List<FalsePositionIteration> iterationData = new ArrayList<>();
        Expression expression = new ExpressionBuilder(expressionString).variable(variable).build();

        double fXL = expression.setVariable(variable, xL).evaluate();
        double fXR = expression.setVariable(variable, xR).evaluate();

        if (fXL * fXR > 0) {
            // No root in [xL, xR]
            return iterationData;
        }

        int iteration = 0;
        while (true) {
            iteration++;
            double xNew = xL - fXL * (xR - xL) / (fXR - fXL);
            double fXNew = expression.setVariable(variable, xNew).evaluate();

            iterationData.add(new FalsePositionIteration(iteration, xL, xR, fXL, fXR, xNew, fXNew));

            if (Math.abs(fXNew) < epsilon) {
                break;
            }

            if (fXL * fXNew < 0) {
                xR = xNew;
                fXR = fXNew;
            } else {
                xL = xNew;
                fXL = fXNew;
            }
        }

        return iterationData;
    }
}