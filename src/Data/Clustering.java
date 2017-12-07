package Data;

import Utilites.Utilities;

import java.util.*;
import java.util.stream.Collectors;

public class Clustering extends ArrayList<Cluster> {

    public Clustering(List<Cluster> clusters) {
        super(clusters);
    }

    public Clustering() {

    }

    // Computes the ratio of inter/intra cluster distance
    // Larger values indicate better a clustering
    public double evaluateFitness() {
        double interClusterDistance = evaluateInterClusterDistance();
        double intraClusterDistance = evaluateIntraClusterDistance();
        if (interClusterDistance > 0) {
            return interClusterDistance / intraClusterDistance;
        } else {
            return 1 / intraClusterDistance;
        }
    }

    // Computes the average distance between data vectors within each cluster
    // Better clusters tend to have smaller Intra Cluster Distances
    public double evaluateIntraClusterDistance() {
        double intraClusterDistance = 0.0;

        for (Cluster cluster : this) {
            if (cluster.isEmpty()) {
                continue;
            }

            double[] centroid = cluster.getCentroid();
            double distance = 0.0;
            for (Datum point : cluster) {
                distance += Utilities.computeDistance(point.features, centroid);
            }

            intraClusterDistance += distance / nonEmptyClusterCount();
        }

        return intraClusterDistance;
    }

    // Computes the average distance between the centroids of clusters
    // Better clusters tend to have larger Inter Cluster Distances
    public double evaluateInterClusterDistance() {
        double interClusterDistance = 0.0;
        // Filter out empty clusters?
        List<double[]> centroids = this.parallelStream().map(Cluster::getCentroid).collect(Collectors.toList());
        for (int i = 0; i < centroids.size(); i++) {
            for (int j = i; j < centroids.size(); j++) {
                interClusterDistance += Utilities.computeDistance(centroids.get(i), centroids.get(j));
            }
        }

        return interClusterDistance / nChoose2(this.size());
    }

    public List<double[]> getCentroids() {
        return this.parallelStream().map(Cluster::getCentroid).collect(Collectors.toList());
    }

    private int nChoose2(int n) {
        switch (n) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                int nFactorial = 1;
                for (int i = n; i > 0; i--) {
                    nFactorial *= i;
                }
                int nLessTwoFactorial = 1;
                for (int i = n - 2; i > 0; i--) {
                    nFactorial *= i;
                }
                return nFactorial / (2 * nLessTwoFactorial);
        }
    }

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
