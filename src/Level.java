public class Level {
    private String name;
    private int[][] board;

    public Level(String name, int[][] board) {
        this.name = name;
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public int[][] getBoard() {
        return board;
    }
}