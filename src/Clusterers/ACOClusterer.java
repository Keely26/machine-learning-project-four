package Clusterers;

import Data.*;
import Utilites.Utilities;

import java.util.*;

public class ACOClusterer implements IDataClusterer {

    private final int numAnts;
    private final double k1, k2, radius, gamma;

    private List<Ant> ants;
    double density;
    int x;
    int y;
    int[] antPosition;
    int[][] grid;
    HashMap<Integer, Datum> compare = new HashMap<>();
    boolean isVacant = false;

    public ACOClusterer(int numAnts, double k1, double k2, double radius, double gamma) {
        this.numAnts = numAnts;
        this.k1 = k1;
        this.k2 = k2;
        this.radius = radius;
        this.gamma = gamma;

    }

    @Override
    public Clustering cluster(Dataset dataset) {
        //compute distance
        dataset.computeDistances();

        //initialize
        initialize(dataset);

        //for each ant
        for (Ant k : ants) {
            //if ant is not carrying and site has data point
            if (!k.isCarrying && !isVacant) {
                density = computeDensity(dataset, radius, gamma);
                probPickUp(k1, density);
                //if U(1,0) <= prob pick up
                //pick up
            }
            //else if ant is carrying item and site is empty
            if (k.isCarrying && isVacant) {
                density = computeDensity(dataset, radius, gamma);
                probDrop(k2, density);
                //if U(1,0) <= prob dropping
                //drop
            }
            //move to randomly selected neighboring site not occupied by ant
            move();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ant Colony Optimization";
    }


    private void initialize(Dataset dataPoints) {
        int gridSize = dataPoints.size();
        Random random = new Random();
        Ant ant;
        // Initialize grid
        grid = new int[gridSize][gridSize];

        //Initialize ants with random positions
        for (int i = 0; i < ants.size(); i++) {
            x = random.nextInt(gridSize);
            y = random.nextInt(gridSize);
            do {
                ant = new Ant(x, y);
                ants.add(i, ant);
            } while (!ants.contains(ant));
        }

        //Place data points on grid
        for (int i = 0; i < dataPoints.size(); i++) {
            //get random data point
            int randomDatum = new Random().nextInt(dataPoints.size());
            //grid[random.nextInt(size)][random.nextInt(size)] = dataPoints.get(random.nextInt(dataPoints.size()));
            grid[random.nextInt(gridSize)][random.nextInt(gridSize)] = randomDatum;
        }
    }

    private Ant createAnt(int x, int y) {
        //antPosition = grid[x][y];
        Ant ant = new Ant(x, y);
        return ant;
    }

    private double computeDistance(double radius, Dataset dataset, double[] curLocation) {
        double avgDistance = 0.0;
        //look to see what points are in radius
        for (Datum point : dataset) {
            if (Utilities.computeDistance(point.features, curLocation) <= radius) {
                //compute distance of all points in radius
                //take average of that
                avgDistance += Utilities.computeDistance(point.features, curLocation);
            }
        }
        return avgDistance;
    }

    private double computeDensity(Dataset dataset, double radius, double gamma) {
        //(1/radius)sum(1 - d(datapoint on location and datapoint in radius)/constant (defines scale of dissimilarity))
        double density = 0;
        density = ((1 / radius) * (1 - (computeDistance(radius, dataset, getAntPosition(dataset.size())) / gamma)));
        return density;
    }

    private double probPickUp(double k1, double density) {
        double probability = Math.pow((k1 / (k1 + density)), 2);
        return probability;
    }

    private double probDrop(double k2, double density) {
        double probability = Math.pow((density / (k2 + density)), 2);
        return probability;
    }

    private void pickUp() {
        //if ant not carrying
        //observes 1 corpse in one of its neighboring sites
        //picked up with prob = 1
        //observes many corpses
        //one is randomly selected
    }

    private void drop() {
        //after ant picks up corpse
        //after moving at least on set away
        //if ant surrounded by at least one other corpse
        //corpse is dropped in randomly selected vacant spot

    }

    private void move() {
    }

    private double[] getAntPosition(int gridSize) {
        double[] position = new double[]{x, y};
        return position;
    }

    private Datum getDataPointLocation(int x, int y) {
        int[] antPosition = new int[]{x, y};
        Datum dataPoint = this.compare.getOrDefault(Arrays.hashCode(antPosition), null);
        if (dataPoint == null) {
            isVacant = true;
        } else {
            isVacant = false;
        }
        return dataPoint;
    }
}

