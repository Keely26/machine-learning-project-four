package Data;

import java.util.ArrayList;
import java.util.List;

public class Clustering extends ArrayList<Cluster> {

    private double clusterQuality = Double.MAX_VALUE;
    private double emptyClusterPenalty = -1000000000;

    public Clustering(List<Cluster> clusters) {
        super(clusters);
    }

    public Clustering() {

    }

    public double evaluate() {
        if (this.size() == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        double avgClusterSimilarity = 0;    // Larger is better
        for (int i = 0; i < this.size(); i++) {
            avgClusterSimilarity += computeSimilarity(this.get(i));
        }
        avgClusterSimilarity /= this.size();


        double avgCusterSeperation = 0;     // Larger is better




        // Return large value for a strong clustering
        // A strong clustering has high similarity between the members of each cluster
        // while having low similarity between the

        return avgClusterSimilarity;
    }

    private double computeSimilarity(Cluster cluster) {
        double[] center = cluster.getClusterCenter();
        double similarity = 0.0;


        return similarity;
    }











    public double evaluateClusters() {
        if (this.size() == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        // Calculate average intra-cluster distance (lower is better)
        double intraSum = this.stream()
                .mapToDouble(this::getAvgIntraClusterDistance)
                .sum() / this.size();

        // Calculate average inter-cluster distance (higher is better)
        Dataset clusterCenters = new Dataset();
        this.forEach(cluster -> {
            clusterCenters.add(getClusterCenter(cluster));
        });
        clusterCenters.computeDistances();

        double interSum = 0.0;
        for (int i = 0; i < clusterCenters.size(); i++) {
            for (int j = 0; j < clusterCenters.size(); j++) {
                interSum += this.get(i).size() * clusterCenters.getDistance(clusterCenters.get(i), clusterCenters.get(j));
            }
        }

        interSum /= (Math.pow(clusterCenters.size(), 2));

        this.clusterQuality = interSum / intraSum;
        return this.clusterQuality;
    }

    public double getClusterQuality() {
        return this.clusterQuality;
    }

    private double getAvgIntraClusterDistance(Cluster cluster) {
        if (cluster.size() == 0) {
            return emptyClusterPenalty;
        }
        double avgDistance = 0.0;
        for (int i = 0; i < cluster.size(); i++) {
            for (int j = 0; j < cluster.size(); j++) {
                if (i != j) {
                    avgDistance += cluster.getDistance(cluster.get(i), cluster.get(j));
                }
            }
            avgDistance /= cluster.size();
        }
        return avgDistance / cluster.size();
    }

    private Datum getClusterCenter(Cluster cluster) {
        if (cluster.getClusterCenter() != null) {
            return new Datum(cluster.getClusterCenter());
        }
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            builder.append("Cluster ")
                    .append(i + 1)
                    .append(", Points:")
                    .append(this.get(i).size())
                    .append("\n");
        }
        return builder.toString();
    }
}
