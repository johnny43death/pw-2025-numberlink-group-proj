import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class NumberLinkV4 extends JPanel {
    private final int cellSize = 50;
    private int[][] board;
    private int size;
    private int[][] filledColors;

    private List<Point> currentPath = new ArrayList<>();
    private Point lastPoint = null;
    private int selectedNumber = 0;
    private Runnable onLevelComplete;

    private Map<Integer, Color> colorMap = Map.of(
            1, Color.RED,
            2, Colors.seaColor,
            3, Color.GREEN,
            4, Color.ORANGE,
            5, Colors.lightYellow,
            6, Colors.lightPurple,
            7, Color.CYAN,
            8, Colors.babyPink,
            9, Colors.lightOrange,
            10, Colors.mint
    );

    public NumberLinkV4(int[][] board, Runnable onLevelComplete) {
        this.board = board;
        this.size = board.length;
        this.onLevelComplete = onLevelComplete;
        this.filledColors = new int[size][size];


        setPreferredSize(new Dimension(size * cellSize, size * cellSize));
        setBackground(Color.WHITE);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                if (x >= 0 && x < size && y >= 0 && y < size && board[y][x] != 0) {
                    int clickedNumber = board[y][x];
                    Point clicked = new Point(x, y);

                    // Cofnij, jeśli kliknięto liczbę i istnieje już ścieżka tego numeru
                    if (selectedNumber == clickedNumber && !currentPath.isEmpty()) {
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                if (filledColors[i][j] == clickedNumber) {
                                    filledColors[i][j] = 0;
                                }
                            }
                        }
                        currentPath.clear();
                        selectedNumber = 0;
                        lastPoint = null;
                        repaint();
                    } else {
                        // Rozpocznij nową ścieżkę
                        selectedNumber = clickedNumber;
                        lastPoint = clicked;
                        currentPath = new ArrayList<>();
                        currentPath.add(lastPoint);
                        repaint();
                    }
                }
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;
                Point p = new Point(x, y);
                if (lastPoint != null && x >= 0 && x < size && y >= 0 && y < size && isAdjacent(lastPoint, p)) {
                    if ((board[y][x] == 0 || board[y][x] == selectedNumber) && filledColors[y][x] == 0) {
                        filledColors[y][x] = selectedNumber;
                        currentPath.add(p);
                        lastPoint = p;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
                if (checkWin()) {
                    JOptionPane.showMessageDialog(null, "Poziom ukończony!");
                    onLevelComplete.run();
                }
            }
        };

        addMouseListener(mouseAdapter);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitToMenu");
        getActionMap().put("exitToMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Tak", "Nie"};
                int result = JOptionPane.showOptionDialog(
                        NumberLinkV4.this,
                        "Czy na pewno chcesz wrócić do menu?",
                        "Potwierdzenie",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (result == JOptionPane.YES_OPTION) {
                    onLevelComplete.run();
                }
            }
        });
        addMouseMotionListener(mouseAdapter);
    }

    private boolean isAdjacent(Point a, Point b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    private boolean checkWin() {
        // 1. Sprawdź, czy cała plansza jest wypełniona
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[y][x] == 0 && filledColors[y][x] == 0) return false;
            }
        }

        // 2. Sprawdź, czy każda para liczb jest połączona jedną spójną ścieżką
        Set<Integer> numbers = new HashSet<>();
        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) numbers.add(cell);
            }
        }

        for (int number : numbers) {
            List<Point> endpoints = new ArrayList<>();
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (board[y][x] == number) {
                        endpoints.add(new Point(x, y));
                    }
                }
            }

            if (endpoints.size() != 2) return false; // każda liczba musi wystąpić dokładnie 2 razy

            if (!isConnected(endpoints.get(0), endpoints.get(1), number)) {
                return false; // liczby nie są połączone poprawną ścieżką
            }
        }

        return true;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int value = board[y][x];
                if (value != 0) {
                    g.setColor(colorMap.getOrDefault(value, Color.LIGHT_GRAY));
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (filledColors[y][x] != 0) {
                    g.setColor(colorMap.getOrDefault(filledColors[y][x], Color.LIGHT_GRAY));
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }

                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);

                if (value != 0) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString(String.valueOf(value), x * cellSize + 18, y * cellSize + 32);
                }
            }
        }
    }

    private boolean isConnected(Point start, Point end, int number) {
        boolean[][] visited = new boolean[size][size];
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.y][start.x] = true;

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p.equals(end)) return true;

            for (int d = 0; d < 4; d++) {
                int nx = p.x + dx[d];
                int ny = p.y + dy[d];

                if (nx >= 0 && ny >= 0 && nx < size && ny < size && !visited[ny][nx]) {
                    if (board[ny][nx] == number || filledColors[ny][nx] == number) {
                        visited[ny][nx] = true;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }

        return false;
    }
}