import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame {
    private LevelManager levelManager;

    public GameFrame() {
        setTitle("Numberlink - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        levelManager = new LevelManager("poziomy.txt");
        showMenu();
        setVisible(true);
    }

    public void showMenu() {
        setContentPane(new Menu(levelManager.getLevels(), this));
        revalidate();
        repaint();
    }

    public void startGame(Level level) {
        setTitle("Poziom: " + level.getName());
        NumberLinkV4 gamePanel = new NumberLinkV4(level.getBoard(), this::showMenu);

        JPanel panelWithExport = new JPanel(new BorderLayout());
        panelWithExport.add(gamePanel, BorderLayout.CENTER);

        JButton exportButton = new JButton("Pobierz planszę");
        exportButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exportButton.addActionListener(e -> {
            BufferedImage image = new BufferedImage(
                    gamePanel.getWidth(), gamePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            gamePanel.paint(g2);
            g2.dispose();
            try {
                File output = new File(level.getName().replaceAll("\\s+", "_") + ".png");
                ImageIO.write(image, "png", output);
                JOptionPane.showMessageDialog(this, "Zapisano do pliku: " + output.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Błąd zapisu obrazu.");
            }
        });

        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topBar.add(exportButton);
        panelWithExport.add(topBar, BorderLayout.NORTH);

        setContentPane(panelWithExport);
        pack();
        setLocationRelativeTo(null);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
