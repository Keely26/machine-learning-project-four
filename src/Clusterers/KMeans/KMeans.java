package Clusterers.KMeans;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * KMeans algorithm
 * for baseline comparison clustering
 *
 * @author Karen Stengel
 */
public class KMeans implements IDataClusterer {

    private final int numClusters;

    public KMeans(int numClusters) {
        this.numClusters = numClusters;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        Clustering clustering;

        // Initialize centroids
        List<double[]> currentCentroids = new ArrayList<>(this.numClusters);
        List<double[]> prevCentroids = new ArrayList<>(this.numClusters);
        for (int i = 0; i < this.numClusters; i++) {
            currentCentroids.add(dataset.get(Utilities.randomInteger(dataset.size())).features);
        }

        int iteration = 0;

        // While not converged
        do {
            clustering = assignPointsToClusters(dataset, currentCentroids);
            currentCentroids = clustering.getCentroids();

            iteration++;
        } while (shouldContinue(iteration, currentCentroids, prevCentroids));

        return clustering;
    }


    private Clustering assignPointsToClusters(Dataset dataset, List<double[]> centroids) {
        Clustering clustering = centroids.stream().map(Cluster::new).collect(Collectors.toCollection(Clustering::new));

        for (Datum dataPoint : dataset) {
            int nearestIndex = -1;
            double nearestDistance = Double.MAX_VALUE;
            for (int j = 0; j < clustering.size(); j++) {
                double distance = Utilities.computeDistance(dataPoint.features, clustering.get(j).getClusterCenter());
                if (distance < nearestDistance) {
                    nearestIndex = j;
                    nearestDistance = distance;
                }
            }
            clustering.get(nearestIndex).add(dataPoint);
        }

        return clustering;
    }

    // Determine if the algorithm should stop
    private boolean shouldContinue(int iteration, List<double[]> currentCentroids, List<double[]> prevCentroids) {
        // Todo: Other cutoff conditions?
        return iteration < 10000;
    }

    @Override
    public String toString() {
        return "kMeans";
    }
}
