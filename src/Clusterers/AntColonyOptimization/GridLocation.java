package Clusterers.AntColonyOptimization;

public class GridLocation {
    int x;
    int y;

    public GridLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
