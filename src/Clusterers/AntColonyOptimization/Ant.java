package Clusterers.AntColonyOptimization;


import Data.Datum;

/**
 * Ant class acts as a container to hold a set of antID numbers, their positions, and their paths
 */
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

    public Datum getFood() {
        return this.food;
    }

    public void addFood(Datum food) {
        this.food = food;
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
