package Data;

import java.util.ArrayList;

public class Clustering extends ArrayList<Cluster> {

    private double clusterQuality = Double.MAX_VALUE;

    public Clustering() {

    }

    public double evaluateClusters() {
        // Calculate average intra-cluster distance (lower is better)
        double intraSum = this.stream().mapToDouble(this::getAvgIntraClusterDistance).sum() / this.size();

        // Calculate average inter-cluster distance (higher is better)
        Dataset clusterCenters = new Dataset();
        this.forEach(cluster -> clusterCenters.add(getClusterCenter(cluster)));

        double interSum = clusterCenters.stream()
                .mapToDouble(center1 -> clusterCenters.stream()
                        .filter(center2 -> !center1.equals(center2))
                        .mapToDouble(center1::computeDistance)
                        .sum() / clusterCenters.size() - 1)
                .sum();

        this.clusterQuality = intraSum / interSum;
        System.out.println("Cluster quality: " + this.clusterQuality);

        return this.clusterQuality;
    }

    public double getClusterQuality() {
        return this.clusterQuality;
    }

    private double getAvgIntraClusterDistance(Cluster cluster) {
        return cluster.stream()
                .mapToDouble(sample -> cluster.stream()
                        .mapToDouble(sample::computeDistance)
                        .sum() / cluster.size())
                .sum();
    }

    private Datum getClusterCenter(Cluster cluster) {
        double[] avgVector = new double[cluster.get(0).features.length];

        // Sum feature values for all elements in the cluster
        cluster.forEach(sample -> {
            for (int i = 0, bound = sample.features.length; i < bound; i++) {
                avgVector[i] += sample.features[i];
            }
        });

        // Normalize
        for (int i = 0, bound = avgVector.length; i < bound; i++) {
            avgVector[i] /= cluster.size();
        }

        return new Datum(avgVector);
    }
}
