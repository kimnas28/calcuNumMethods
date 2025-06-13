package numMethods;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Gui extends JFrame {

    private JButton bisectionButton;
    private JButton falsePositionButton;
    private JButton newtonRaphsonButton;
    private JButton secantButton;
    private JButton fixedPointButton;
    private JButton cramerButton;
    private JButton matrixMultiplicationButton;
    private JTextArea outputArea;
    private Image backgroundImage;
    private Font pixelFont; // Custom font

    public Gui() {
        setTitle("NumeriSolve");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- 1. Load the pixel font ---
        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT,
                new File(System.getProperty("user.dir") + "/src/upheavtt.ttf")).deriveFont(Font.BOLD, 56f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            pixelFont = pixelFont.deriveFont(Font.BOLD, 20f); // fallback
        }

        // At the top of your Gui constructor
        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/src/upheavtt.ttf")).deriveFont(Font.BOLD, 20f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            pixelFont = new Font("Arial", Font.BOLD, 20);
        }
        
        // Button settings: smaller, rounded, blue-white gradient, matrix multiplication on two lines
        Dimension buttonSize = new Dimension(240, 56); 
        Font buttonFont = new Font("Arial", Font.BOLD, 15);

bisectionButton = createGradientButton("BISECTION", buttonFont, buttonSize);
falsePositionButton = createGradientButton("FALSE POSITION", buttonFont, buttonSize);
        newtonRaphsonButton = createGradientButton("NEWTON RAPHSON", buttonFont, buttonSize);
        secantButton = createGradientButton("SECANT", buttonFont, buttonSize);
        fixedPointButton = createGradientButton("FIXED POINT", buttonFont, buttonSize);
        cramerButton = createGradientButton("CRAMER'S", buttonFont, buttonSize);
        matrixMultiplicationButton = createGradientButton(
            "<html><center>MATRIX<br>MULTIPLICATION</center></html>", buttonFont, buttonSize);

        // Load background image using resource loading
        backgroundImage = new ImageIcon(getClass().getResource("/nummethods/pixels_bg.png")).getImage();

        // Output area
        outputArea = new JTextArea(50, 50);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setOpaque(false);
        outputArea.setFont(new Font("Arial", Font.BOLD, 20));

        // Create a white rounded panel that holds the outputArea
        JPanel roundedWhitePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 180)); // translucent white
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2d.dispose();
            }
        };
        roundedWhitePanel.setOpaque(false);
        roundedWhitePanel.add(outputArea, BorderLayout.CENTER);

        // Place the rounded panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(roundedWhitePanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        scrollPane.setPreferredSize(new Dimension(1100, 400));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(scrollPane);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setOpaque(false);

        // --- MOVE BUTTONS LOWER: add vertical space above the buttons ---
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        centeringPanel.add(Box.createVerticalStrut(60), gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonGrid = new JPanel(new GridLayout(2, 4, 20, 18));
        buttonGrid.setOpaque(false);
        buttonGrid.add(bisectionButton);
        buttonGrid.add(falsePositionButton);
        buttonGrid.add(newtonRaphsonButton);
        buttonGrid.add(secantButton);
        buttonGrid.add(fixedPointButton);
        buttonGrid.add(cramerButton);
        buttonGrid.add(matrixMultiplicationButton);
        buttonGrid.add(new JLabel("")); // filler

        centeringPanel.add(buttonGrid, gbc);

        // --- 2. Use pixelFont in the titlePanel ---
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                String mainTitle = "NumeriSolve";
                String subtitle = "Interactive Numerical Methods Toolkit";
                Font mainFont = pixelFont.deriveFont(56f); // Use pixelFont
                Font subFont = pixelFont.deriveFont(Font.BOLD, 24f); // Use pixelFont smaller for subtitle

                int panelWidth = getWidth();

                // Button color: top (light blue), bottom (white)
                Color top = new Color(235, 242, 250);
                Color bottom = new Color(255, 255, 255);
                GradientPaint titlePaint = new GradientPaint(0, 20, top, 0, 100, bottom);

                // Blue shadow
                Color blueShadow = new Color(20, 60, 110, 110);
                // Yellow for subtitle
                Color yellow = new Color(255, 206, 50);

                // Draw shadow (blue)
                g2d.setFont(mainFont);
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(mainTitle);
                int xTitle = (panelWidth - titleWidth) / 2;
                int yTitle = 72;
                g2d.setColor(blueShadow);
                g2d.drawString(mainTitle, xTitle + 3, yTitle + 3);

                // Draw main title with gradient paint
                g2d.setPaint(titlePaint);
                g2d.drawString(mainTitle, xTitle, yTitle);

                // Draw a line below the title (blue accent)
                g2d.setColor(new Color(60, 120, 180, 130));
                g2d.setStroke(new BasicStroke(3));
                int lineY = yTitle + 22;
                g2d.drawLine(panelWidth / 2 - 170, lineY, panelWidth / 2 + 170, lineY);

                // Draw subtitle (yellow, pixelFont)
                g2d.setFont(subFont);
                FontMetrics subfm = g2d.getFontMetrics();
                int subWidth = subfm.stringWidth(subtitle);
                int xSub = (panelWidth - subWidth) / 2;
                int ySub = lineY + 32;
                g2d.setColor(yellow);
                g2d.drawString(subtitle, xSub, ySub);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 130);
            }
        };
        titlePanel.setOpaque(false);

        outerPanel.add(titlePanel, BorderLayout.NORTH);
        outerPanel.add(centeringPanel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.add(outerPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        contentPanel.setOpaque(false);

        setContentPane(contentPanel);

        bisectionButton.addActionListener(e -> runBisection());
        falsePositionButton.addActionListener(e -> runFalsePosition());
        newtonRaphsonButton.addActionListener(e -> runNewtonRaphson());
        secantButton.addActionListener(e -> runSecant());
        fixedPointButton.addActionListener(e -> runFixedPoint());
        cramerButton.addActionListener(e -> runCramersRule());
        matrixMultiplicationButton.addActionListener(e -> runMatrixMultiplication());
    }
        private JButton createGradientButton(String text, Font font, Dimension size) {
        JButton button = new JButton(text) {
            private boolean hover = false;

            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                setForeground(new Color(35, 35, 35));
                setFont(font);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20;
                int w = getWidth();
                int h = getHeight();

                // The gradient: light blue (top) to white (bottom)
                Color top = new Color(235, 242, 250);
                Color bottom = new Color(255, 255, 255);
                Color hoverTop = new Color(210, 222, 240);
                Color hoverBottom = new Color(240, 240, 255);

                GradientPaint paint = hover
                        ? new GradientPaint(0, 0, hoverTop, 0, h, hoverBottom)
                        : new GradientPaint(0, 0, top, 0, h, bottom);

                g2.setPaint(paint);
                g2.fillRoundRect(0, 0, w, h, arc, arc);

                // Border (subtle gray)
                g2.setColor(new Color(180, 180, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

                // Let super.paintComponent draw HTML text for multi-line centering
                g2.setColor(getForeground());
                g2.setFont(getFont());
                super.paintComponent(g);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Border painted in paintComponent for anti-alias edges
            }
        };
        button.setPreferredSize(size);
        setFont(font);
        return button;
    }

    // Remove HTML tags for width calculation (if needed)
    private String stripHtml(String html) {
        return html.replaceAll("<[^>]+>", "").replace("&nbsp;", " ");
    }

    private void runMatrixMultiplication() {
    try {
        // Prompt for columns and rows
        String columns = JOptionPane.showInputDialog(this, "Enter columns (space-separated):", "");
        if (columns == null) return;
        String rows = JOptionPane.showInputDialog(this, "Enter rows (space-separated):", "");
        if (rows == null) return;

        String[] listcol = columns.trim().split("\\s+");
        String[] listrow = rows.trim().split("\\s+");
        int[] arrcol = new int[listcol.length];
        int[] arrrow = new int[listrow.length];

        for (int i = 0; i < listcol.length; i++) arrcol[i] = Integer.parseInt(listcol[i]);
        for (int i = 0; i < listrow.length; i++) arrrow[i] = Integer.parseInt(listrow[i]);

        int[][] final_matrix = new int[arrrow.length][arrcol.length];
        for (int y = 0; y < arrrow.length; y++) {
            for (int x = 0; x < arrcol.length; x++) {
                final_matrix[y][x] = arrrow[y] * arrcol[x];
            }
        }

        // Build output for GUI
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix Multiplication Result:\n\n");
        for (int[] row : final_matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Invalid Input: " + ex.getMessage());
    }
}

    // Bisection Method
    // Only the runBisection() method is shown here

private void runBisection() {
    try {
        String func = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (func == null) return;

        // Extract variable name (first single letter found)
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(func);
        String variable = "x";
        if (matcher.find()) {
            variable = matcher.group(1);
        }

        String[] parts = func.split("=");
        if (parts.length == 2) {
            func = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        } else if (parts.length > 2) {
            JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols.");
            return;
        }

        double a = promptForDouble("Enter the x1 (" + variable + "1):");
        double b = promptForDouble("Enter the x2 (" + variable + "2):");
        int decimals = promptForInt("Enter the number of decimal places for tolerance (e.g., 4 for 1e-4):", 0, 10);
        double tolerance = Math.pow(10, -decimals);

        List<BisectionMethod.IterationData> iterations = BisectionMethod.bisectionMethod(func, variable, a, b, tolerance);

        // Use StringBuilder to create a well-aligned table using format specifiers
        StringBuilder sb = new StringBuilder();
        sb.append("Bisection Method\n\n");
        sb.append("Iteration Table:\n");

        // Use a fixed-width font for JTextArea for alignment
        outputArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        // Table Header
        String headerFormat = "%-5s %-12s %-12s %-12s %-12s\n";
        sb.append(String.format(headerFormat, "Iter", variable + "L", variable + "R", variable + "C", "f(" + variable + "C)"));
        sb.append(String.format(headerFormat, "-----", "------------", "------------", "------------", "------------"));

        // Table Rows
        String rowFormat = "%-5d %12." + decimals + "f %12." + decimals + "f %12." + decimals + "f %12." + decimals + "f\n";
        for (BisectionMethod.IterationData data : iterations) {
            sb.append(String.format(rowFormat, data.iteration, data.a, data.b, data.c, data.fc));
        }

        // Solving Steps
        sb.append("\nSolving Steps:\n");
        String stepFormat = "Iter %d: " + variable + "L = %." + decimals + "f, " + variable + "R = %." + decimals + "f, " + variable + "C = %." + decimals +
                "f, f(" + variable + "C) = %." + decimals + "f\n";
        for (BisectionMethod.IterationData data : iterations) {
            sb.append(String.format(stepFormat, data.iteration, data.a, data.b, data.c, data.fc));
        }
        if (!iterations.isEmpty()) {
            BisectionMethod.IterationData last = iterations.get(iterations.size() - 1);
            sb.append(String.format("%nFinal solution: Root ≈ %." + decimals + "f (after %d iterations)%n",
                    last.c, last.iteration));
        }
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
    // False Position Method
private void runFalsePosition() {
    try {
        String func = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (func == null) return;
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(func);
        String variable = "x";
        if (matcher.find()) variable = matcher.group(1);
        String expressionString;
        String[] parts = func.split("=");
        if (parts.length == 2)
            expressionString = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        else if (parts.length > 2)
            { JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols."); return; }
        else expressionString = func;
        double xL = promptForDouble("Enter initial guess " + variable + "0:");
        double xR = promptForDouble("Enter initial guess " + variable + "1:");
        int decimals = promptForInt("Enter number of decimal places for tolerance (e.g., 4 for 1e-4):", 1, 10);
        double epsilon = Math.pow(10, -decimals);

        List<FalsePosition.FalsePositionIteration> results = FalsePosition.falsePosition(expressionString, variable, xL, xR, epsilon);

        outputArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        if (results.isEmpty()) {
            outputArea.setText("No valid iterations. Make sure that f(" + variable + "0) and f(" + variable + "1) have opposite signs.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("False Position Method\n\n");
        sb.append("Iteration Table:\n");
        String headerFormat = "%-5s %-12s %-12s %-13s %-13s\n";
        sb.append(String.format(headerFormat, "Iter", variable + "L", variable + "R", variable + "New", "f(" + variable + "New)"));
        sb.append(String.format(headerFormat, "-----", "------------", "------------", "-------------", "-------------"));
        String rowFormat = "%-5d %12." + decimals + "f %12." + decimals + "f %13." + decimals + "f %13." + decimals + "f\n";
        for (FalsePosition.FalsePositionIteration it : results)
            sb.append(String.format(rowFormat, it.iteration, it.xL, it.xR, it.xNew, it.fXNew));

        sb.append("\nSolving Steps:\n");
        String formatStep = variable + "%d = " + variable + "L - f(" + variable + "L)(" + variable + "R - " + variable + "L)/(f(" + variable + "R) - f(" + variable + "L)) = %." + decimals + "f - (%." + decimals +
                "f)(%." + decimals + "f - %." + decimals + "f) / (%." + decimals + "f - %." + decimals +
                "f) = %." + decimals + "f\n";
        for (FalsePosition.FalsePositionIteration it : results)
            sb.append(String.format(formatStep, it.iteration, it.xL, it.fXL, it.xR, it.xL, it.fXR, it.fXL, it.xNew));
        if (!results.isEmpty()) {
            FalsePosition.FalsePositionIteration last = results.get(results.size() - 1);
            sb.append(String.format("%nFinal solution: Root = %." + decimals + "f%n", last.xNew));
        }
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

    // Newton-Raphson Method
    // Only the runNewtonRaphson() method is shown here
private void runNewtonRaphson() {
    try {
        String func = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (func == null) return;
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(func);
        String variable = "x";
        if (matcher.find()) variable = matcher.group(1);
        String function;
        String[] parts = func.split("=");
        if (parts.length == 2)
            function = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        else if (parts.length > 2)
            { JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols."); return; }
        else function = func;
        double x0 = promptForDouble("Enter the initial guess (" + variable + "0):");
        int decimals = promptForInt("Enter the number of decimal places for tolerance (e.g., 4 for 1e-4):", 0, 10);
        double tolerance = Math.pow(10, -decimals);

        List<NewtonRaphson.IterationData> iterations = NewtonRaphson.newtonRaphson(function, variable, x0, tolerance);

        outputArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        StringBuilder sb = new StringBuilder();
        sb.append("Newton-Raphson Method\n\n");
        sb.append("Iteration Table:\n");
        String headerFormat = "%-5s %-14s %-14s\n";
        sb.append(String.format(headerFormat, "Iter", variable, "f(" + variable + ")"));
        sb.append(String.format(headerFormat, "-----", "--------------", "--------------"));
        String rowFormat = "%-5d %14." + decimals + "f %14." + decimals + "f\n";
        for (NewtonRaphson.IterationData data : iterations)
            sb.append(String.format(rowFormat, data.iteration, data.x, data.fx));

        sb.append("\nSolving Steps:\n");
        String formatStep = variable + "%d = " + variable + " - f(" + variable + ")/f'(" + variable + ") = %." + decimals + "f - (%." + decimals + "f) / (%." + decimals + "f) = %." + decimals + "f\n";
        for (NewtonRaphson.IterationData data : iterations)
            sb.append(String.format(formatStep, data.iteration, data.x, data.fx, data.dfx, data.xNew));
        NewtonRaphson.IterationData last = iterations.get(iterations.size() - 1);
        sb.append(String.format("%nFinal solution: Root = %." + decimals + "f%n", last.xNew));
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
    // Secant Method
    private void runSecant() {
    try {
        String equation = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (equation == null) return;
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(equation);
        String variable = "x";
        if (matcher.find()) variable = matcher.group(1);
        String[] parts = equation.split("=");
        if (parts.length == 2)
            equation = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        else if (parts.length > 2)
            { JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols."); return; }
        double x0 = promptForDouble("Enter first initial guess (" + variable + "0):");
        double x1 = promptForDouble("Enter second initial guess (" + variable + "1):");
        int decimals = promptForInt("Enter number of decimal places for tolerance (e.g., 4 for 1e-4):", 1, 10);
        double tol = Math.pow(10, -decimals);

        Stack<Double> approximations = new Stack<>();
        approximations.push(x0);
        approximations.push(x1);

        StringBuilder solvingSteps = new StringBuilder();
        Secant.runSecant(approximations, equation, variable, tol, solvingSteps);

        outputArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        StringBuilder sb = new StringBuilder();
        sb.append("Secant Method\n\n");
        sb.append("Iteration Table:\n");
        String headerFormat = "%-5s %-14s %-14s\n";
        sb.append(String.format(headerFormat, "Iter", variable, "f(" + variable + ")"));
        sb.append(String.format(headerFormat, "-----", "--------------", "--------------"));
        String rowFormat = "%-5d %14." + decimals + "f %14." + decimals + "f\n";
        for (int i = 0; i < approximations.size(); i++) {
            double x = approximations.get(i);
            double fx = Secant.evaluateFunction(equation, variable, x);
            sb.append(String.format(rowFormat, i + 1, x, fx));
        }
        sb.append("\nSolving Steps:\n");
        sb.append(solvingSteps.toString());
        sb.append(String.format("\nFinal solution: Root = %." + decimals + "f\n", approximations.peek()));
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
    
    private void runFixedPoint() {
    try {
        String gx = JOptionPane.showInputDialog(this, "Enter the function g(x):", "");
        if (gx == null) return;
        gx = FixedPoint.class.getDeclaredMethod("convertExpNotation", String.class)
                .invoke(null, gx).toString();
        double x0 = promptForDouble("Enter initial guess x0:");
        int decimals = promptForInt("Enter number of decimal places for tolerance (e.g., 4 for 1e-4):", 1, 10);
        double epsilon = Math.pow(10, -decimals);
        int maxIter = 100;
        List<Double> results = new java.util.ArrayList<>();
        results.add(x0);
        int iteration = 0;
        double xPrev = x0;
        while (iteration < maxIter) {
            double xNext = FixedPoint.evaluateG(gx, xPrev);
            results.add(xNext);
            if (Math.abs(xNext - xPrev) < epsilon) break;
            xPrev = xNext;
            iteration++;
        }

        outputArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        StringBuilder sb = new StringBuilder();
        sb.append("Fixed Point Method\n\n");
        sb.append("Iteration Table:\n");
        String headerFormat = "%-5s %-14s %-14s\n";
        sb.append(String.format(headerFormat, "Iter", "x", "g(x)"));
        sb.append(String.format(headerFormat, "-----", "--------------", "--------------"));
        String rowFormat = "%-5d %14." + decimals + "f %14." + decimals + "f\n";
        for (int i = 0; i < results.size() - 1; i++) {
            double x = results.get(i);
            double gxVal = FixedPoint.evaluateG(gx, x);
            sb.append(String.format(rowFormat, i + 1, x, gxVal));
        }
        sb.append("\nSolving Steps:\n");
        for (int i = 1; i < results.size(); i++) {
            double prev = results.get(i - 1);
            double curr = results.get(i);
            sb.append(String.format("x%d = g(x%d-1) = g(%." + decimals + "f) = %." + decimals + "f\n", i, i, prev, curr));
        }
        sb.append("\nFinal solution: Root ≈ ")
          .append(String.format("%." + decimals + "f", results.get(results.size() - 1)));
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
    private void runCramersRule() {
    try {
        // Collect equations from user
        String[] equations = new String[3];
        equations[0] = JOptionPane.showInputDialog(this, "Enter equation 1 (ex., 2x + 3y - 1z = 5):", "");
        if (equations[0] == null) return;
        equations[1] = JOptionPane.showInputDialog(this, "Enter equation 2 (ex., 4x - 2y + 3z = 6):", "");
        if (equations[1] == null) return;
        equations[2] = JOptionPane.showInputDialog(this, "Enter equation 3 (ex., 3x + 5y + 2z = 8):", "");
        if (equations[2] == null) return;

        // Detect variable names
        java.util.Set<String> varSet = new java.util.LinkedHashSet<>();
        for (String eq : equations) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("([a-zA-Z]\\w*)").matcher(eq);
            while (m.find()) {
                varSet.add(m.group(1));
            }
        }
        java.util.List<String> variables = new java.util.ArrayList<>(varSet);

        if (variables.size() != 3) {
            outputArea.setText("Please enter equations with exactly 3 unique variables.");
            return;
        }

        double[][] matrix = new double[3][3];
        double[] constants = new double[3];

        for (int i = 0; i < 3; i++) {
            String eq = equations[i].replaceAll("-", "+-");
            String[] sides = eq.split("=");
            if (sides.length != 2) {
                outputArea.setText("Equation format error in: " + eq);
                return;
            }
            String lhs = sides[0];
            String rhs = sides[1];

            // Fill coefficients
            for (int v = 0; v < 3; v++) {
                String var = variables.get(v);
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("([+-]?\\d*\\.?\\d*)\\s*" + var).matcher(lhs);
                double coef = 0;
                while (m.find()) {
                    String num = m.group(1);
                    if (num == null || num.trim().isEmpty() || num.equals("+")) num = "1";
                    if (num.equals("-")) num = "-1";
                    coef += Double.parseDouble(num.trim());
                }
                matrix[i][v] = coef;
            }
            // Fill constant
            constants[i] = Double.parseDouble(rhs.trim());
        }

        // Calculate determinants and build output
        double detA = determinant3x3(matrix);

        // Prepare output
        StringBuilder sb = new StringBuilder();

        // Show system
        sb.append("System of equations:\n");
        for (String eq : equations) sb.append("  ").append(eq).append("\n");
        sb.append("\nVariables: ").append(variables).append("\n\n");

        // Show coefficient matrix
        sb.append("Coefficient matrix:\n");
        for (int i = 0; i < 3; i++) {
            sb.append("[ ");
            for (int j = 0; j < 3; j++) {
                sb.append(String.format("%6.2f ", matrix[i][j]));
            }
            sb.append("]\n");
        }

        sb.append(String.format("|A| = %.2f\n\n", detA));

        // Show matrices for each variable
        for (int v = 0; v < 3; v++) {
            double[][] temp = new double[3][3];
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    temp[i][j] = (j == v) ? constants[i] : matrix[i][j];

            sb.append("Matrix: " + variables.get(v) + "\n");
            for (int i = 0; i < 3; i++) {
                sb.append("[ ");
                for (int j = 0; j < 3; j++) {
                    sb.append(String.format("%6.2f ", temp[i][j]));
                }
                sb.append("]\n");
            }
            double detAi = determinant3x3(temp);
            sb.append(String.format("|A%s| = %.2f\n", variables.get(v), detAi));
            double val = detA != 0 ? detAi / detA : 0;
            if (Math.abs(val) < 1e-9) val = 0.0;
            sb.append(String.format("%s = |A%s| / |A| = %.2f / %.2f = %.2f\n\n",
                    variables.get(v), variables.get(v), detAi, detA, val));
        }

        // Final answers summary
        sb.append("Final answers:\n");
        for (int v = 0; v < 3; v++) {
            double[][] temp = new double[3][3];
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    temp[i][j] = (j == v) ? constants[i] : matrix[i][j];
            double val = detA != 0 ? determinant3x3(temp) / detA : 0;
            if (Math.abs(val) < 1e-9) val = 0.0;
            sb.append(variables.get(v)).append(" = ").append(String.format("%.2f", val)).append("\n");
        }

        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

// Helper method for 3x3 determinant
private double determinant3x3(double[][] m) {
    return m[0][0]*(m[1][1]*m[2][2] - m[1][2]*m[2][1])
         - m[0][1]*(m[1][0]*m[2][2] - m[1][2]*m[2][0])
         + m[0][2]*(m[1][0]*m[2][1] - m[1][1]*m[2][0]);
}

    // Utility methods for prompting user safely
    private double promptForDouble(String prompt) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, prompt, "");
            if (input == null) throw new RuntimeException("Operation cancelled.");
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number, please try again.");
            }
        }
    }

    private int promptForInt(String prompt, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, prompt, "");
            if (input == null) throw new RuntimeException("Operation cancelled.");
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    JOptionPane.showMessageDialog(this, "Please enter an integer between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input, please enter an integer.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui numeriSolve = new Gui();
            numeriSolve.setVisible(true);
        });
    }
}