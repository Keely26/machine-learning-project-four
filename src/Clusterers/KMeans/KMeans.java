package Clusterers.KMeans;

import Clusterers.IDataClusterer;
import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.*;

/**
 * KMeans clustering algorithm
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
        Clustering clustering = new Clustering();
        Clustering oldClustering;

        // Initialize centroids
        List<double[]> currentCentroids = new ArrayList<>(this.numClusters);
        for (int i = 0; i < this.numClusters; i++) {
            currentCentroids.add(dataset.get(Utilities.randomInteger(dataset.size())).features);
        }

        int iteration = 0;

        // While not converged
        do {
            oldClustering = clustering;
            clustering = Utilities.assignPointsToClusters(dataset, currentCentroids);
            currentCentroids = clustering.getCentroids();
            iteration++;
        } while (shouldContinue(iteration, clustering, oldClustering));
        System.out.println("Num iterations: " + iteration);

        return clustering;
    }

    // Determine if the algorithm should stop
    private boolean shouldContinue(int iteration, Clustering clustering, Clustering oldClustering) {
        return !Objects.deepEquals(clustering, oldClustering) && iteration < 10000;
    }

    @Override
    public String toString() {
        return "kMeans";
    }
}
