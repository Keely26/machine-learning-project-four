package Clusterers.AntColonyOptimization;

/**
 * Stores a pair of x, y coordinates for use in the ant grid world
 */
public class GridLocation {
    int x;
    int y;

    GridLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
