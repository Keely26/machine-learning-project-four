package Clusterers.AntColonyOptimization;

import Clusterers.KMeans.KMeans;
import Data.*;
import Utilites.Utilities;

import java.util.*;

/**
 * Ants move about a transformed 2D grid rather than in the original solution space. This class serves to represent that
 * grid and mediate the interactions with it for the clustering algorithm
 */
public class Grid {

    private Datum[][] grid;
    private HashMap<Integer, Boolean> antOccupiedMap;
    private int gridLength;

    Grid(int gridSize, Dataset dataset, List<Ant> ants) {
        this.gridLength = gridSize;
        initializeGrid(dataset, ants);
    }

    /**
     * Place ants and points on the grid at unique locations
     */
    private void initializeGrid(Dataset dataset, List<Ant> ants) {
        this.grid = new Datum[gridLength][gridLength];
        this.antOccupiedMap = new HashMap<>();

        // Place data points, create a list of all possible placements
        List<GridLocation> locations = new ArrayList<>();
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                locations.add(new GridLocation(i, j));
            }
        }

        // Shuffle list to provide randomness
        Collections.shuffle(locations);

        // Remove locations from the list and assign as data point locations to prevent duplicates
        for (Datum point : dataset) {
            GridLocation randomLocation = locations.remove(0);
            this.grid[randomLocation.x][randomLocation.y] = point;
        }

        // Place ants in the same fashion as before
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

    /**
     * Determine if a valid grid location has food available for pick up
     */
    public boolean hasFood(GridLocation location) {
        return checkBounds(location.x, location.y)
                && this.grid[location.x][location.y] != null
                && !this.grid[location.x][location.y].isCarried();
    }

    /**
     * Determine if a valid grid location is empty, and thus can be dropped on
     */
    public boolean isValidDrop(GridLocation location) {
        return this.grid[location.x][location.y] == null;
    }

    /**
     * Mark the food at the provided location as being carried
     */
    public void pickUpFood(GridLocation location) {
        if (!checkBounds(location.x, location.y)) {
            throw new IndexOutOfBoundsException("Ant is lost. Ant is upset. Ant crashes your program.");
        }
        this.grid[location.x][location.y].setCarried(true);
    }

    /**
     * Take the food from the old location and move it to the new one
     */
    public void dropFood(GridLocation newLocation, GridLocation oldLocation) {
        Datum food = this.grid[oldLocation.x][oldLocation.y];
        if (food == null) {
            throw new NullPointerException("Ant's food is null. Ant doesn't understand null. Ant crashes your program.");
        }
        food.setCarried(false);
        this.grid[oldLocation.x][oldLocation.y] = null;
        this.grid[newLocation.x][newLocation.y] = food;
    }

    /**
     * Compute the density of a given region. For all points within the radius on the grid, compute the distances to
     * those points in the solution space and compute the density proportional to the average distance.
     */
    public double getDensity(Ant ant, double radius) {
        Dataset neighborhood = new Dataset();
        Dataset gridNeighborhood = new Dataset();
        double[] antLocation = new double[]{ant.getLocation().x, ant.getLocation().y};

        // Search for neighbors over the entire grid
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[0].length; j++) {
                // Get distance to location
                Datum gridIJ = this.grid[i][j];
                if (gridIJ != null) {
                    double[] space = new double[]{i, j};
                    double distance = Utilities.computeDistance(antLocation, space);
                    // If the point is within the radius, add it to the neighbor list
                    if (distance < radius) {
                        neighborhood.add(gridIJ);
                        gridNeighborhood.add(new Datum(new double[]{i, j}));
                    }
                }
            }
        }

        // Compute the average distance to neighbors
        double avgDistance;
        if (ant.isCarrying()) {
            // Compute the avg distance between the point the ant is carrying and neighboring points
            Datum food = this.grid[ant.getPickUpLocation().x][ant.getPickUpLocation().y];
            avgDistance = computeAverageDistance(neighborhood, food.features);
        } else {
            // Compute the avg distance between the food at the ants location and the neighbors
            avgDistance = computeAverageDistance(neighborhood, grid[ant.getLocation().x][ant.getLocation().y].features);
        }

        if (avgDistance == 0.0) {
            return avgDistance;
        }

        // Compute the density
        double density = 0.0;
        for (Datum neighbor : gridNeighborhood) {
            density += (1 / radius) * (Utilities.computeDistance(antLocation, neighbor.features) / avgDistance);
        }

        return density;
    }

    /**
     * Compute the average distance between a given point and a list of other points
     */
    private double computeAverageDistance(Dataset neighbors, double[] point) {
        double avg = 0.0;

        // Sum over neighbors
        for (Datum neighbor : neighbors) {
            avg += Utilities.computeDistance(neighbor.features, point);
        }

        // Normalize and return
        return avg / neighbors.size();
    }


    /**
     * Determine if an ant is currently at the supplied location
     */
    public boolean isUnoccupied(GridLocation location) {
        return checkBounds(location.x, location.y) && this.antOccupiedMap.get(location.hashCode()) == null;
    }

    /**
     * Update an ants position on the grid
     */
    public void updateLocation(GridLocation oldLocation, GridLocation newLocation) {
        this.antOccupiedMap.remove(oldLocation.hashCode());
        this.antOccupiedMap.put(newLocation.hashCode(), true);
    }

    /**
     * Given a number of cluster to be formed, use kMeans to cluster the points on the 2D grid world, then
     * translate that clustering back into the higher dimensional solution space and build the clustering
     */
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

    /**
     * Determine if the supplied points are a valid grid location
     */
    private boolean checkBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.grid.length && y < this.grid[0].length;
    }

    /**
     * Return the number of data points on the grid, used for logging and debugging
     */
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

