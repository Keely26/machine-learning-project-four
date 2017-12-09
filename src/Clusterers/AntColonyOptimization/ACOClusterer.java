package Clusterers.AntColonyOptimization;

import Clusterers.IDataClusterer;
import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.*;

/**
 * Implementation of Ant Colony Optimization to cluster data
 * Ants translate the problem from the N dimensional solution space to a two dimensional space by using a 2D grid
 * Ants move data points around the grid and use kMeans to cluster in the 2D space, then transform the results back into
 * the original solution space.
 *
 * @author Keely Weisbeck
 */
public class ACOClusterer implements IDataClusterer {

    private final int numAnts, gridSize, numClusters;
    private final double k1, k2, radius;

    public ACOClusterer(int numAnts, int gridSize, double k1, double k2, double radius, int numClusters) {
        this.numAnts = numAnts;
        this.gridSize = gridSize;
        this.k1 = k1;
        this.k2 = k2;
        this.radius = radius;
        this.numClusters = numClusters;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        // Initialize
        List<Ant> ants = initializeAnts();
        Grid grid = new Grid(gridSize, dataset, ants);
        double avgFitness = 0.0;

        int iteration = 0;
        do {
            for (Ant currentAnt : ants) {
                GridLocation currentLocation = currentAnt.getLocation();
                if (currentAnt.isCarrying()) {
                    // Ant is carrying
                    if (!grid.hasFood(currentAnt.getLocation())) {
                        // Check if should drop
                        if (!currentAnt.getLocation().equals(currentAnt.getPickUpLocation()) && shouldDrop(currentAnt, grid)) {
                            grid.dropFood(currentAnt.getLocation(), currentAnt.getPickUpLocation());
                            currentAnt.drop();
                        }
                    }
                } else {
                    if (grid.hasFood(currentAnt.getLocation())) {   // If site has food
                        // Check if pick up
                        if (shouldPickUp(currentAnt, grid)) {
                            grid.pickUpFood(currentLocation);
                            currentAnt.setPickUpLocation();
                        }
                    }
                }
                // Move
                move(grid, currentAnt);
            }

            avgFitness += grid.buildClustering(numClusters).evaluateFitness();

            // Logging
            if (iteration % 100 == 0) {
                //printStats(grid, ants);
                //System.out.println(grid.buildClustering(numClusters).evaluateFitness());
                avgFitness /= 100;
                System.out.println(avgFitness);
                avgFitness = 0;
            }


            iteration++;
        } while (shouldContinue(iteration));

        return grid.buildClustering(numClusters);
    }

    /**
     * Return true until either the maximum number of iterations has been reached.
     */
    private boolean shouldContinue(int iteration) {
        return iteration < 10000;
    }

    /**
     * Ants move one space in a random direction, if there is a valid movement that can be made
     */
    private void move(Grid grid, Ant ant) {
        GridLocation antLocation = ant.getLocation();
        List<Integer> moves = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Collections.shuffle(moves);

        while (!moves.isEmpty()) {
            int move = moves.remove(0);
            GridLocation newLocation = new GridLocation(antLocation.x, antLocation.y);
            switch (move) {
                case 1: // Try to move up
                    newLocation.y++;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                case 2: // Try to move down
                    newLocation.y--;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                case 3: // Try to move right
                    newLocation.x++;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                case 4: // Try to move left
                    newLocation.x--;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid ant movement, " + move);
            }
        }
    }

    /**
     * Use parameters and the density of the current region to determine if an ant should pick up the data point
     * at its current location
     */
    private boolean shouldPickUp(Ant ant, Grid grid) {
        double density = grid.getDensity(ant, this.radius);
        return Utilities.randomDouble(1) < Math.pow((k1 / (k1 + density)), 2);
    }

    /**
     * Use parameters and the density of the current region to determine if an ant should drop the data point
     * at its current location
     */
    private boolean shouldDrop(Ant ant, Grid grid) {
        if (grid.isValidDrop(ant.getLocation())) {
            double density = grid.getDensity(ant, this.radius);
            return Utilities.randomDouble(1) < Math.pow((density / (k2 + density)), 2);
        } else {
            return false;
        }
    }

    /**
     * Create new army of ants
     */
    private List<Ant> initializeAnts() {
        List<Ant> ants = new ArrayList<>(this.numAnts);
        for (int i = 0; i < this.numAnts; i++) {
            ants.add(new Ant());
        }
        return ants;
    }

    @Override
    public String toString() {
        return "Ant Colony Optimization";
    }
}
