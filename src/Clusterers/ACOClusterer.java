package Clusterers;

import Data.Clustering;
import Data.Dataset;
import Data.Datum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ACOClusterer implements IDataClusterer {

    private final int numAnts;
    private final double k1, k2, radius, gamma;

    private List<Ant> ants;
    double density;
    int x;
    int y;
    int numSurroundingCorpses;
    int numSurroundingVancant;
    int numSurroundingAnts;
    int[] antPosition;
    HashMap<Integer, Datum> distances = new HashMap<>();
    HashMap<Integer, Datum> gridDistances = new HashMap<>();


    public ACOClusterer(int numAnts, double k1, double k2, double radius, double gamma) {
        this.numAnts = numAnts;
        this.k1 = k1;
        this.k2 = k2;
        this.radius = radius;
        this.gamma = gamma;

    }

    @Override
    public Clustering cluster(Dataset dataset) {
        //compute distance of data points before placed on grid
        dataset.computeDistances();

        // Initialize grid
        Datum[][] grid = Grid.createGrid(dataset); //creates empty 2D grid of Datum

        //Initialize ants with random positions
        ants = Ant.createAnts(dataset, ants);

        //for each ant
        for (Ant k : ants) {
            Ant.move(k, k.x, k.y, grid);
            Datum dataPoint = Grid.getDataPoint(grid, k.x, k.y);
            //if ant is not carrying and site has data point
            if (k.carrying == null && dataPoint != null) { //TODO && !.isVacant
                density = similarityFunction(radius, dataset, k, grid);
                double Pp = probPickUp(k1, density);
                //TODO check if U(1,0) <= prob pick up is same as below
                if (Pp <= gamma) {
                    pickUp(k, dataPoint, grid);
                }
            }
            //else if ant is carrying item and site is empty
            if (k.carrying != null && dataPoint == null) { //TODO && isVacant
                density = similarityFunction(radius, dataset, k, grid);
                double Pd = probDrop(k2, density);
                //if U(1,0) <= prob dropping
                //TODO check if U(1,0) <= prob pick up is same as below
                if (Pd <= gamma) {
                    drop(k, dataPoint, grid);
                }
            }
            //move to randomly selected neighboring site not occupied by ant
            Ant.move(k, k.x, k.y, grid);
        }
        return null;
    }


    private double similarityFunction(double radius, Dataset dataset, Ant ant, Datum[][] grid) {
        Datum[] neighborhood = ant.checkNeighborhood(radius);
        double similarity = 0.0;
        for (int i = 0; i < neighborhood.length; i++) {
            int[] datumLocation = Grid.gridLocation(dataset, grid, neighborhood[i]);
            //int[] x2Coordinates = Grid.getGridLocation(neighborhood[i], ant);
            //double similarity = ((1 / radius) * (getDistance(getDataPointLocation(ant.x, ant.y), neighborhood[i])/dataset.getDistance()));
            similarity =+ ((1 / radius) * (getDistance(ant.x, ant.y, datumLocation[0], datumLocation[1])/dataset.getDistance(getDataPointLocation(ant.x, ant.y), getDataPointLocation(datumLocation[0], datumLocation[1]))));

        }
        return  similarity;
    }

    private double probPickUp(double k1, double density) {
        double probability = Math.pow((k1 / (k1 + density)), 2);
        return probability;
    }

    private double probDrop(double k2, double density) {
        double probability = Math.pow((density / (k2 + density)), 2);
        return probability;
    }

    private void pickUp(Ant ant, Datum dataPoint, Datum[][] grid) {
        ant.carrying = dataPoint;
        //Grid.getDataPoint(grid, ant.x, ant.y) = null;

    }

    private void drop(Ant ant, Datum corpse, Datum[][] grid) {
        ant.carrying = null;
        //Grid.getDataPoint(grid, ant.x, ant.y) = null;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        double distance = Math.sqrt((Math.pow((x2 - x1), 2)) + (Math.pow((y2 - y1), 2)));
        return distance;
    }


    private Datum getDataPointLocation(int x, int y) {
        int[] antPosition = new int[]{x, y};
        Datum dataPoint = this.distances.getOrDefault(Arrays.hashCode(antPosition), null);
        return dataPoint;
    }

}

