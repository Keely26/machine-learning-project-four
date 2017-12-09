package Clusterers.AntColonyOptimization;

/**
 * Ant class acts as a container to hold a set of antID numbers, their positions, and their paths
 */
public class Ant {

    private GridLocation location;
    private GridLocation pickUpLocation;

    public boolean isCarrying() {
        return pickUpLocation != null;
    }

    public void setLocation(GridLocation location) {
        this.location = location;
    }

    public GridLocation getLocation() {
        return this.location;
    }

    public GridLocation getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation() {
        this.pickUpLocation = this.location;
    }

    public void drop() {
        this.pickUpLocation = null;
    }
}
