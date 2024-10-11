package uni.projects.gameoflife.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uni.projects.gameoflife.model.Cell;
import uni.projects.gameoflife.model.Grid;

import java.io.IOException;

import static uni.projects.gameoflife.model.Grid.getGrid;

@Service
public class GameService {


    public Grid processJsonFile(MultipartFile file) throws IOException {
        return Grid.fromJson(file.getInputStream().toString());
    }

    // Method to calculate the next generation
    public Grid computeNextGeneration(Grid currentGrid) {
        int rows = currentGrid.getRows();
        int cols = currentGrid.getCols();
        Grid nextGrid = new Grid(rows, cols);

        // Apply the Game of Life rules
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell currentCell = currentGrid.getCells()[row][col];
                int aliveNeighbors = countAliveNeighbors(currentGrid, row, col);

                // Apply the rules of the Game of Life
                if (currentCell.isAlive()) {
                    // Cell dies due to underpopulation or overpopulation
                    if (aliveNeighbors < 2 || aliveNeighbors > 3) {
                        nextGrid.getCells()[row][col].setAlive(false);  // Dies
                    } else {
                        nextGrid.getCells()[row][col].setAlive(true);   // Lives
                    }
                } else {
                    // Cell becomes alive through reproduction
                    if (aliveNeighbors == 3) {
                        nextGrid.getCells()[row][col].setAlive(true);   // Becomes alive
                    }
                }
            }
        }

        nextGrid.setIteration(currentGrid.getIteration() + 1);
        return nextGrid;
    }

    // Helper method to count alive neighbors for a cell with wrapping
    private int countAliveNeighbors(Grid grid, int row, int col) {
        int[] directions = {-1, 0, 1}; // To check all 8 neighbors
        int aliveCount = 0;
        int rows = grid.getRows();
        int cols = grid.getCols();

        for (int i : directions) {
            for (int j : directions) {
                if (i == 0 && j == 0) {
                    continue;  // Skip the current cell
                }
                // Calculate neighbor's row and column with wrapping
                int neighborRow = (row + i) % rows;
                int neighborCol = (col + j) % cols;

                if (neighborRow < 0) {
                    neighborRow += rows;  // Wrap around
                }
                if (neighborCol < 0) {
                    neighborCol += cols;  // Wrap around
                }

                if (grid.getCells()[neighborRow][neighborCol].isAlive()) {
                    aliveCount++;
                }
            }
        }

        return aliveCount;
    }
}
