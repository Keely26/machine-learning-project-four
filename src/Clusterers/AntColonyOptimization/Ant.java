package Clusterers.AntColonyOptimization;

/**
 * Ants need to maintain their current location and a pointer to the location of the data point
 * they are carrying
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
