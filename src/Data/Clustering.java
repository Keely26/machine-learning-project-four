package Data;

import java.util.ArrayList;

public class Clustering extends ArrayList<Cluster> {

    public void evaluateClusters() {
        // Calculate average intra-cluster distance (lower is better)
        double intraSum = 0.0;
        for (Cluster cluster : this) {
            double clusterDistance = getAvgIntraClusterDistance(cluster);
            System.out.println("Cluster " + cluster.getClusterId() + " intra-cluster distance: " + clusterDistance);
            intraSum += clusterDistance;
        }
        intraSum /= this.size();

        // Calculate average inter-cluster distance (higher is better)
        Dataset clusterCenters = new Dataset();
        this.forEach(cluster -> clusterCenters.add(getClusterCenter(cluster)));

        double interSum = 0.0;
        for (Datum center1 : clusterCenters) {
            for (Datum center2 : clusterCenters) {
                if (!center1.equals(center2)) {
                    interSum += center1.computeDistance(center2);
                }
            }
            interSum /= clusterCenters.size() - 1;
        }

        System.out.println("Avg Inter-cluster distance: " + interSum);
        System.out.println("Cluster quality: " + intraSum / interSum);
    }

    private double getAvgIntraClusterDistance(Cluster cluster) {
        double interClusterDistance = 0.0;
        for (Datum sample : cluster) {
            for (Datum neighbor : cluster) {
                interClusterDistance += sample.computeDistance(neighbor);
            }
            interClusterDistance /= cluster.size();
        }
        return interClusterDistance;
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
