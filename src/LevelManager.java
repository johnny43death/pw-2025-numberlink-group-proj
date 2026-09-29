import java.io.*;
import java.util.*;

public class LevelManager {
    private List<Level> levels = new ArrayList<>();

    public LevelManager(String filePath) {
        loadLevels(filePath);
    }

    private void loadLevels(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentName = null;
            List<int[]> tempBoard = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("\"") && line.endsWith("\"")) {
                    if (currentName != null && !tempBoard.isEmpty()) {
                        levels.add(new Level(currentName, tempBoard.toArray(new int[0][])));
                        tempBoard.clear();
                    }
                    currentName = line.substring(1, line.length() - 1);
                } else {
                    String[] tokens = line.replace(";", "").split(",");
                    int[] row = new int[tokens.length];
                    for (int i = 0; i < tokens.length; i++) {
                        row[i] = Integer.parseInt(tokens[i].trim());
                    }
                    tempBoard.add(row);
                }
            }

            // Dodaj ostatni poziom
            if (currentName != null && !tempBoard.isEmpty()) {
                levels.add(new Level(currentName, tempBoard.toArray(new int[0][])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Level> getLevels() {
        return levels;
    }
}