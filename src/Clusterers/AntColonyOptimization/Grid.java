package Clusterers.AntColonyOptimization;

import Clusterers.KMeans.KMeans;
import Data.*;
import Utilites.Utilities;

import java.util.*;

public class Grid {

    private Datum[][] grid;
    private HashMap<Integer, Boolean> antOccupiedMap;
    private int gridLength;

    Grid(int gridSize, Dataset dataset, List<Ant> ants) {
        this.gridLength = gridSize;
        initializeGrid(dataset, ants);
    }

    private void initializeGrid(Dataset dataset, List<Ant> ants) {
        this.grid = new Datum[gridLength][gridLength];
        this.antOccupiedMap = new HashMap<>();

        // Place data points
        List<GridLocation> locations = new ArrayList<>();
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                locations.add(new GridLocation(i, j));
            }
        }

        Collections.shuffle(locations);

        for (Datum point : dataset) {
            GridLocation randomLocation = locations.remove(0);
            this.grid[randomLocation.x][randomLocation.y] = point;
        }

        // Place ants
        locations = new ArrayList<>();
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                locations.add(new GridLocation(i, j));
            }
        }

        Collections.shuffle(locations);

        for (Ant ant : ants) {
            GridLocation randomLocation = locations.remove(0);
            ant.setLocation(randomLocation);
            this.antOccupiedMap.put(ant.hashCode(), true);
        }
    }

    public boolean hasFood(GridLocation location) {
        return checkBounds(location.x, location.y)
                && this.grid[location.x][location.y] != null
                && !this.grid[location.x][location.y].isCarried();
    }

    public boolean isValidDrop(GridLocation location) {
        return this.grid[location.x][location.y] == null;
    }

    public void pickUpFood(GridLocation location) {
        if (!checkBounds(location.x, location.y)) {
            throw new IndexOutOfBoundsException("Ant is lost. Ant is upset. Ant crashes your program.");
        }
        this.grid[location.x][location.y].setCarried(true);
    }

    public void dropFood(GridLocation newLocation, GridLocation oldLocation) {
        Datum food = this.grid[oldLocation.x][oldLocation.y];
        if (food == null) {
            throw new NullPointerException("Ant's food is null. Ant doesn't understand null. Ant crashes your program.");
        }
        food.setCarried(false);
        this.grid[oldLocation.x][oldLocation.y] = null;
        this.grid[newLocation.x][newLocation.y] = food;
    }

    public double getDensity(Ant ant, double radius) {
        Dataset neighborhood = new Dataset();
        Dataset gridNeighborhood = new Dataset();
        double[] antLocation = new double[]{ant.getLocation().x, ant.getLocation().y};

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                // Get distance to location
                Datum gridIJ = this.grid[i][j];
                if (gridIJ != null) {
                    double[] space = new double[]{i, j};
                    double distance = Utilities.computeDistance(antLocation, space);
                    if (distance < radius) {
                        neighborhood.add(gridIJ);
                        gridNeighborhood.add(new Datum(new double[]{i, j}));
                    }
                }
            }
        }

        double avgDistance;
        if (ant.isCarrying()) {
            // Compute the avg distance between the point the ant is carrying and neighboring points
            Datum food = this.grid[ant.getPickUpLocation().x][ant.getPickUpLocation().y];
            avgDistance = computeAverageDistance(neighborhood, food.features);
        } else {
            avgDistance = computeAverageDistance(neighborhood, grid[ant.getLocation().x][ant.getLocation().y].features);
        }

        if (avgDistance == 0.0) {
            return avgDistance;
        }

        double density = 0.0;
        for (Datum neighbor : gridNeighborhood) {
            density += (1 / radius) * (Utilities.computeDistance(antLocation, neighbor.features) / avgDistance);
        }

        return density;
    }

    private double computeAverageDistance(Dataset neighbors, double[] point) {
        double avg = 0.0;
        for (Datum neighbor : neighbors) {
            avg += Utilities.computeDistance(neighbor.features, point);
        }
        return avg / neighbors.size();
    }


    public boolean isUnoccupied(GridLocation location) {
        return checkBounds(location.x, location.y) && this.antOccupiedMap.get(location.hashCode()) == null;
    }

    public void updateLocation(GridLocation oldLocation, GridLocation newLocation) {
        this.antOccupiedMap.remove(oldLocation.hashCode());
        this.antOccupiedMap.put(newLocation.hashCode(), true);
    }

    public Clustering buildClustering(int numClusters) {
        Clustering clustering = new Clustering();

        // Use kMeans to cluster the points on the 2D grid
        KMeans kMeans = new KMeans(numClusters);
        Dataset twoDDataset = new Dataset();
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                Datum loc = this.grid[i][j];
                if (loc != null) {
                    twoDDataset.add(new Datum(new double[]{i, j}));
                }
            }
        }
        Clustering twoDClustering = kMeans.cluster(twoDDataset);

        // Convert the 2D clustering back into the original problem
        for (int i = 0; i < twoDClustering.size(); i++) {
            Cluster twoDCluster = twoDClustering.get(i);
            Cluster solutionCluster = new Cluster(i);
            for (Datum gridLocation : twoDCluster) {
                int x = (int) gridLocation.features[0];
                int y = (int) gridLocation.features[1];
                solutionCluster.add(this.grid[x][y]);
            }
            clustering.add(solutionCluster);
        }

        return clustering;
    }

    private boolean checkBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.grid.length && y < this.grid[0].length;
    }

    public int checkNumDataPoints() {
        int count = 0;
        for (Datum[] row : this.grid) {
            for (int j = 0; j < this.grid.length; j++) {
                if (row[j] != null) {
                    count++;
                }
            }
        }
        return count;
    }
}

