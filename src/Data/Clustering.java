package Data;

import Utilites.Utilities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The clustering class serves as a top level collection of data points segregated into defined clusters
 * This class also provides the functionality to evaluate the quality of clusters
 */
public class Clustering extends ArrayList<Cluster> {

    public Clustering(List<Cluster> clusters) {
        super(clusters);
    }

    public Clustering() {}

    /**
     * Computes the ratio of inter/intra cluster distance
     * Larger values indicate better a clustering
     */
    public double evaluateFitness() {
        double interClusterDistance = evaluateInterClusterDistance();
        double intraClusterDistance = evaluateIntraClusterDistance();
        if (interClusterDistance > 0) {
            return interClusterDistance + 1 / intraClusterDistance;
        } else {
            return 1 / intraClusterDistance;
        }
    }

    /**
     * Computes the average distance between data vectors within each cluster
     * Better clusters tend to have smaller Intra Cluster Distances
     */
    public double evaluateIntraClusterDistance() {
        double intraClusterDistance = 0.0;

        for (Cluster cluster : this) {
            // Skip if empty
            if (cluster.isEmpty()) {
                continue;
            }

            // Compute centroid
            double[] centroid = cluster.getCentroid();
            double distance = 0.0;

            // Sum distances to centroids
            for (Datum point : cluster) {
                distance += Utilities.computeDistance(point.features, centroid);
            }

            intraClusterDistance += distance;
        }

        return intraClusterDistance / nonEmptyClusterCount();
    }

    /**
     * Computes the average distance between the centroids of clusters
     * Better clusters tend to have larger Inter Cluster Distances
     */
    public double evaluateInterClusterDistance() {
        double interClusterDistance = 0.0;
        List<double[]> centroids = new ArrayList<>();
        // Compute centroids
        for (int i = 0; i < this.size(); i++) {
            double[] centroid = this.get(i).getCentroid();
            if (centroid != null) {
                centroids.add(centroid);
            }
        }
        // Sum the average distance between centroids
        int count = 0;
        for (int i = 0; i < centroids.size(); i++) {
            for (int j = i; j < centroids.size(); j++) {
                interClusterDistance += Utilities.computeDistance(centroids.get(i), centroids.get(j));
                count++;
            }
        }

        // Normalize and return
        return interClusterDistance / count;
    }

    /**
     * Compute the centroid vectors of each of the clusters in the collection
     */
    public List<double[]> getCentroids() {
        return this.parallelStream().map(Cluster::getCentroid).collect(Collectors.toList());
    }

    /**
     * Return the number of clusters that contain elements
     */
    private int nonEmptyClusterCount() {
        int num = 0;
        for (Cluster cluster : this) {
            if (!cluster.isEmpty()) {
                num++;
            }
        }
        return num;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            builder.append("Cluster ")
                    .append(i + 1)
                    .append(", Points:")
                    .append("\t")
                    .append(this.get(i).size())
                    .append("\t")
                    .append(Arrays.toString(this.get(i).getClusterCenter()))
                    .append("\n");
        }
        return builder.toString();
    }
}
