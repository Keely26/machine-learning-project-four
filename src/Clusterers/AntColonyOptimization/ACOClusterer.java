package Clusterers.AntColonyOptimization;

import Clusterers.IDataClusterer;
import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.*;

public class ACOClusterer implements IDataClusterer {

    private final int numAnts;
    private final double k1, k2, radius, gamma;

    public ACOClusterer(int numAnts, double k1, double k2, double radius, double gamma) {
        this.numAnts = numAnts;
        this.k1 = k1;
        this.k2 = k2;
        this.radius = radius;
        this.gamma = gamma;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        // Initialize
        List<Ant> ants = initializeAnts();
        Grid grid = new Grid(dataset, ants);

        int iteration = 0;
        do {
            // For each ant
            for (Ant currentAnt : ants) {
                // If not carrying
                if (!currentAnt.isCarrying()) {
                    // If site has food
                    if (grid.hasFood(currentAnt.getLocation())) {
                        // Check if pick up
                        if (shouldPickUp(currentAnt, grid)) {
                            grid.getFood(currentAnt.getLocation());
                            currentAnt.setPickUpLocation();
                        }
                    }
                } else {
                    // Ant is carrying
                    if (!grid.hasFood(currentAnt.getLocation())) {
                        // Check if should drop
                        if (!currentAnt.getLocation().equals(currentAnt.getPickUpLocation()) && shouldDrop(currentAnt, grid)) {
                            grid.putFood(currentAnt.getLocation(), currentAnt.removeFood());
                        }
                    }
                }
                // Move
                move(grid, currentAnt);
            }

            iteration++;
        } while (shouldContinue(iteration));

        return grid.buildClustering();
    }

    private boolean shouldContinue(int iteration) {
        return iteration < 1000;
    }

    private void move(Grid grid, Ant ant) {
        GridLocation antLocation = ant.getLocation();
        List<Integer> moves = Arrays.asList(1, 2, 3, 4);
        Collections.shuffle(moves);

        while (!moves.isEmpty()) {
            int move = moves.remove(0);
            GridLocation newLocation = new GridLocation(antLocation.x, antLocation.y);
            switch (move) {
                case 1: // Up
                    newLocation.y++;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                case 2: // Down
                    newLocation.y--;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                case 3: // Right
                    newLocation.x++;
                    if (grid.isUnoccupied(newLocation)) {
                        ant.setLocation(newLocation);
                        grid.updateLocation(antLocation, newLocation);
                        return;
                    }
                    break;
                case 4: // Left
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

    private boolean shouldPickUp(Ant ant, Grid grid) {
        double density = grid.getDensity(ant, this.radius);
        return Utilities.randomDouble(1) < probPickUp(density);
    }

    private boolean shouldDrop(Ant ant, Grid grid) {
        double density = grid.getDensity(ant, this.radius);
        return Utilities.randomDouble(1) < probDrop(density);
    }

    private double probPickUp(double density) {
        return Math.pow((k1 / (k1 + density)), 2);
    }

    private double probDrop(double density) {
        return Math.pow((density / (k2 + density)), 2);
    }

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

