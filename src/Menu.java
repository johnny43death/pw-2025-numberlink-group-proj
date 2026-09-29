import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class Menu extends JPanel {
    public Menu(List<Level> levels, GameFrame frame) {
        setLayout(new BorderLayout());

        // Główny panel ze wszystkimi przyciskami
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // marginesy

        JLabel title = new JLabel("Wybierz poziom:");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        buttonPanel.add(title);

        for (Level level : levels) {
            JButton button = new JButton(level.getName());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 40));
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener((ActionEvent e) -> {
                frame.startGame(level);
            });
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null); // bez ramki
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // szybkość scrolla

        add(scrollPane, BorderLayout.CENTER);
    }
}