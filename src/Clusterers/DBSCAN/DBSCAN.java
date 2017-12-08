package Clusterers.DBSCAN;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

import java.util.stream.Collectors;

public class DBSCAN implements IDataClusterer {

    private final double epsilon;
    private final int minPoints;
    private int clusterIndex;

    public DBSCAN(int minPoints, double maxDistance) {
        this.minPoints = minPoints;
        this.epsilon = maxDistance;
        this.clusterIndex = 1;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        Clustering clustering = new Clustering();
        this.clusterIndex = 0;

        // Mark all core points
        findCorePoints(dataset);

        dataset.forEach(dataPoint -> {
            // If the data point is non-core or has already been clustered, continue
            if (!dataPoint.isCore() || dataPoint.getCluster() != 0) {
                return;
            }
            this.clusterIndex++;
            dataPoint.setCluster(clusterIndex);

            expandCluster(dataPoint, dataset);
        });

        dataset.sortByCluster();
        Cluster cluster = new Cluster(dataset.get(0).getCluster());
        clustering.add(cluster);
        for (Datum next : dataset) {
            if (cluster.getClusterId() == next.getCluster()) {
                cluster.add(next);
            } else {
                clustering.add(cluster);
                cluster = new Cluster(next.getCluster());
                cluster.add(next);
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

    private void expandCluster(Datum seedPoint, Dataset dataset) {
        int clusterId = seedPoint.getCluster();
        Dataset neighbors = getNeighbors(seedPoint, dataset);
        neighbors.forEach(neighbor -> {
            if (neighbor.getCluster() != 0) {
                return;
            }
            neighbor.setCluster(clusterId);
            if (neighbor.isCore()) {
                expandCluster(neighbor, dataset);
            }
        });
    }

    /**
     * Collect the list of data points within epsilon of the target point
     */
    private Dataset getNeighbors(Datum sample, Dataset dataset) {
        return dataset.parallelStream()
                .filter(neighbor -> !neighbor.equals(sample) && Utilities.computeDistance(sample.features, neighbor.features) < this.epsilon)
                .collect(Collectors.toCollection(Dataset::new));
    }

    @Override
    public String toString() {
        return "DBSCAN";
    }
}