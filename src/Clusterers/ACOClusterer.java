package Clusterers;

import Data.Clustering;
import Data.Dataset;

import java.util.List;

public class ACOClusterer implements IDataClusterer {

    private final int numAnts;
    private final double k1, k2, radius;

    private List<Ant> ants;

    public ACOClusterer(int numAnts, double k1, double k2, double radius) {
        this.numAnts = numAnts;
        this.k1 = k1;
        this.k2 = k2;
        this.radius = radius;
    }

    private void initialize() {
        // Initialize grid
        // Initialize ants
        // Place ants and data points on grid
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        initialize();
        return null;
    }
}
