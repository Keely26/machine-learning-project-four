package Clusterers.AntColonyOptimization;


import Data.Datum;

/**
 * Ant class acts as a container to hold a set of antID numbers, their positions, and their paths
 */
@SuppressWarnings("WeakerAccess")
public class Ant {

    private GridLocation location;
    private GridLocation pickUpLocation;
    private Datum food;

    public boolean isCarrying() {
        return food != null;
    }

    public void setLocation(GridLocation location) {
        this.location = location;
    }

    public GridLocation getLocation() {
        return this.location;
    }

    public double[] getFood() {
        return this.food.features;
    }

    public Datum removeFood() {
        Datum food = this.food;
        this.food = null;
        return food;
    }

    public GridLocation getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation() {
        this.pickUpLocation = this.location;
    }
}
