package uni.projects.gameoflife.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class Grid {
    private int rows;
    private int cols;
    private Cell[][] cells;
    private int iteration = 0;

    public Grid() {}

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        cells = new Cell[rows][cols];
        // Initialize cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(false);  // All cells dead initially
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCellAlive(int row, int col, boolean alive) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cells[row][col].setAlive(alive);
        }
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public void printGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].isAlive() ? "X " : ". ");
            }
            System.out.println();
        }
    }

    public static Grid fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        int rows = rootNode.path("rows").asInt();
        int cols = rootNode.path("cols").asInt();;
        return getGrid(rootNode, rows, cols);
    }

    public static Grid getGrid(JsonNode rootNode, int rows, int cols) {

        Grid grid = new Grid(rows, cols);

        JsonNode cellsNode = rootNode.path("cells");
        for (JsonNode cellNode : cellsNode) {
            int row = cellNode.path("row").asInt();
            int col = cellNode.path("col").asInt();
            boolean isAlive = cellNode.path("alive").asBoolean();

            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                grid.setCellAlive(row, col, isAlive);
            }
        }

        int iteration = rootNode.path("iterations").asInt();
        grid.setIteration(iteration);

        return grid;
    }

    public String toJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("rows", this.rows);
        rootNode.put("cols", this.cols);
        rootNode.put("iterations", this.iteration);

        ArrayNode cellsNode = rootNode.putArray("cells");
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                ObjectNode cellNode = objectMapper.createObjectNode();
                cellNode.put("row", i);
                cellNode.put("col", j);
                cellNode.put("alive", this.cells[i][j].isAlive());
                cellsNode.add(cellNode);
            }
        }

        return objectMapper.writeValueAsString(rootNode);
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }
}
