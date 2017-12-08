package Clusterers.DBSCAN;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

import java.util.stream.Collectors;

/**
 * An implementation of DBSCAN
 * Class must be initialized with minPts and epsilon
 * May then be used to cluster datasets using the Cluster(Dataset dataset) methods
 *
 * @author Zach Connelly
 */
public class DBSCAN implements IDataClusterer {

    private final double epsilon;
    private final int minPoints;

    public DBSCAN(int minPoints, double epsilon) {
        this.minPoints = minPoints;
        this.epsilon = epsilon;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        Clustering clustering = new Clustering();
        int currentClusterLbl = 1;

        // Mark all core points
        findCorePoints(dataset);

        // For each example in the dataset
        for (Datum datum : dataset) {
            // Skip for non core points
            if (!datum.isCore()) {
                continue;
            }
            // If not yet assigned a cluster
            if (datum.getCluster() == 0) {
                datum.setCluster(currentClusterLbl);
                currentClusterLbl++;
            }

            // Assign the same cluster label to neighbors that haven't be assigned a label
            Dataset neighbors = getNeighbors(datum, dataset);
            for (Datum neighbor : neighbors) {
                if (neighbor.getCluster() == 0) {
                    neighbor.setCluster(datum.getCluster());
                }
            }
        }

        // Sort the dataset according to cluster assignment
        dataset.sortByCluster();

        // Loop over the dataset grouping points with the same labels into clusters
        Cluster cluster = new Cluster(dataset.get(0).getCluster());
        clustering.add(cluster);
        for (Datum point : dataset) {
            if (cluster.getClusterId() == point.getCluster()) {     // Add to existing cluster
                cluster.add(point);
            } else {                                                // Form new cluster
                clustering.add(cluster);
                cluster = new Cluster(point.getCluster());
                cluster.add(point);
            }
        }

        return clustering;
    }

    /**
     * Identify all core points within the provided dataset,
     * sets Datum.corePoint as such.
     * A point point is defined to be a CorePoint if there are at least minPts other points within an epsilon radius
     */
    private void findCorePoints(Dataset dataset) {
        dataset.parallelStream().forEach(point -> point.setCore(getNeighbors(point, dataset).size() > this.minPoints));
    }

    /**
     * Computes a list of data points within epsilon of the seed point
     */
    private Dataset getNeighbors(Datum seed, Dataset dataset) {
        return dataset.parallelStream()
                .filter(neighbor -> !neighbor.equals(seed) && Utilities.computeDistance(seed.features, neighbor.features) < this.epsilon)
                .collect(Collectors.toCollection(Dataset::new));
    }

    @Override
    public String toString() {
        return "DBSCAN";
    }
}