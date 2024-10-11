package uni.projects.gameoflife.model;

public class Cell {
    private boolean isAlive;

    public Cell() {}

    public Cell(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }
}
