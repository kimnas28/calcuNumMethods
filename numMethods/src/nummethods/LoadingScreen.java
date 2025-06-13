package numMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class LoadingScreen extends JFrame {
    private int progress = 0;
    private Timer timer;

    public LoadingScreen() {
        setTitle("Loading...");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        add(new LoadingPanel());
        setVisible(true);

        timer = new Timer(50, e -> {
            progress += 2;
            repaint();
            if (progress >= 100) {
                timer.stop();
                dispose();
                launchStartPage();
            }
        });
        timer.start();
    }

    private void launchStartPage() {
        SwingUtilities.invokeLater(() -> {
            StartPage startPage = new StartPage();
            startPage.setVisible(true);
        });
    }

    class LoadingPanel extends JPanel {
        private Font pixelFont;

        public LoadingPanel() {
            try {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/src/upheavtt.ttf")).deriveFont(Font.BOLD, 96f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);
            } catch (Exception e) {
                pixelFont = new Font("Upheaval TT (BRK)", Font.BOLD, 96);
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(10, 24, 48)); 
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

            g2.setFont(pixelFont);
            String loadingText = "NumeriSolve";
            FontMetrics fm = g2.getFontMetrics();

            int loadingTextWidth = fm.stringWidth(loadingText);
            int loadingTextHeight = fm.getHeight();
            int loadingTextAscent = fm.getAscent();

            int barWidth = 700;
            int barHeight = 50;
            int spacing = 40;

            int totalBlockHeight = loadingTextHeight + spacing + barHeight;
            int blockTopY = (getHeight() - totalBlockHeight) / 2;

            int loadingTextX = (getWidth() - loadingTextWidth) / 2;
            int loadingTextY = blockTopY + loadingTextAscent;
            g2.setColor(Color.ORANGE);
            g2.drawString(loadingText, loadingTextX, loadingTextY);

            int barX = (getWidth() - barWidth) / 2;
            int barY = loadingTextY + spacing;
            g2.setColor(Color.WHITE);
            g2.drawRect(barX, barY, barWidth, barHeight);
            g2.fillRect(barX + 1, barY + 1, (barWidth - 2) * progress / 100, barHeight - 2);

            String label = "LOADING...";
            g2.setFont(pixelFont.deriveFont(Font.BOLD, 28f));
            FontMetrics labelFm = g2.getFontMetrics();
            int labelX = barX + 15;
            int labelY = barY + (barHeight - labelFm.getHeight()) / 2 + labelFm.getAscent();

            g2.setColor(Color.BLACK);
            g2.drawString(label, labelX, labelY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoadingScreen::new);
    }
}

class StartPage extends JFrame {

    private Font pixelFont;

    public StartPage() {
        setTitle("NumeriSolve");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            InputStream fontStream = getClass().getResourceAsStream("/upheavtt.ttf");
            if (fontStream != null) {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(120f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
                System.out.println("Loaded font: " + pixelFont.getFontName());
            } else {
                throw new IOException("Font file not found.");
            }
        } catch (Exception e) {
            System.out.println("Font load failed: " + e.getMessage());
            pixelFont = new Font("Upheaval TT (BRK)", Font.BOLD, 120);
        }

        JLabel background;
        URL bgURL = getClass().getResource("/background2.jpg");
        if (bgURL != null) {
            ImageIcon bgIcon = new ImageIcon(bgURL);
            Image scaledImage = bgIcon.getImage().getScaledInstance(1100, 600, Image.SCALE_SMOOTH);
            background = new JLabel(new ImageIcon(scaledImage));
        } else {
            System.out.println("Background image not found. Using plain background.");
            background = new JLabel();
            background.setOpaque(true);
            background.setBackground(Color.BLACK);
        }
        background.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel pixelMathLabel = createColorfulText("NumeriSolve");

        JLabel subText1 = new JLabel("NUMERICAL METHODS");
        subText1.setForeground(Color.WHITE);
        subText1.setFont(pixelFont.deriveFont(24f));
        subText1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subText2 = new JLabel("AND");
        subText2.setForeground(Color.WHITE);
        subText2.setFont(pixelFont.deriveFont(24f));
        subText2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subText3 = new JLabel("DATA STRUCTURE AND ALGORITHM");
        subText3.setForeground(Color.WHITE);
        subText3.setFont(pixelFont.deriveFont(24f));
        subText3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel startLabel = new JLabel("START");
        startLabel.setForeground(Color.RED);
        startLabel.setFont(pixelFont.deriveFont(38f));
        startLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
startLabel.addMouseListener(new java.awt.event.MouseAdapter() {
    Color defaultColor = Color.RED;
    Color hoverColor = Color.GREEN;

    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        startLabel.setForeground(hoverColor);
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        startLabel.setForeground(defaultColor);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        // Hide StartPage and show NumeriSolve GUI
        StartPage.this.setVisible(false);

        SwingUtilities.invokeLater(() -> {
            Gui numeriSolve = new Gui();
            numeriSolve.setVisible(true);
        });
    }
});

        content.add(pixelMathLabel);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(subText1);
        content.add(subText2);
        content.add(subText3);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(startLabel);

        background.add(content);
        setContentPane(background);
    }

    private JPanel createColorfulText(String text) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        Color[] colors = {
            Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW
        };

        for (int i = 0; i < text.length(); i++) {
            JLabel label = new JLabel(String.valueOf(text.charAt(i)));
            label.setForeground(colors[i % colors.length]);
            label.setFont(pixelFont);
            panel.add(label);
        }

        return panel;
    }
}
