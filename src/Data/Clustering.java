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
        clusterCenters.computeDistances();

        double interSum = 0.0;
        for (int i = 0; i < clusterCenters.size(); i++) {
            for (int j = 0; j < clusterCenters.size(); j++) {
                interSum = clusterCenters.getDistance(clusterCenters.get(i), clusterCenters.get(j));
            }
        }
        interSum /= (Math.pow(clusterCenters.size(), 2));

        this.clusterQuality = intraSum / interSum;
        System.out.println("Cluster quality: " + this.clusterQuality);

        return this.clusterQuality;
    }

    public double getClusterQuality() {
        return this.clusterQuality;
    }

    private double getAvgIntraClusterDistance(Cluster cluster) {
        double avgDistance = 0.0;
        for (int i = 0; i < cluster.size(); i++) {
            for (int j = 0; j < cluster.size(); j++) {
                if (i != j) {
                    avgDistance += cluster.getDistance(cluster.get(i), cluster.get(j));
                }
            }
        }
        return avgDistance / Math.pow(cluster.size(), 2);
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
