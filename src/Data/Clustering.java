package Data;

import Utilites.Utilities;

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
        avgClusterSimilarity /= Math.pow(this.size(), 5);


        double avgCusterSeparation = 0;     // Larger is better
        for (int i = 0; i < this.size(); i++) {
            for (int j = i + 1; j < this.size(); j++) {
                double[] centerI = this.get(i).getClusterCenter();
                double[] centerJ = this.get(j).getClusterCenter();
                avgCusterSeparation += Utilities.computeDistance(centerI, centerJ);
            }
        }
        avgCusterSeparation /= this.size();

        this.clusterQuality = avgClusterSimilarity + avgCusterSeparation;
        return clusterQuality;
    }

    private double computeSimilarity(Cluster cluster) {
        if (cluster.size() == 0) {
            return this.emptyClusterPenalty;
        }

        double[] center = cluster.getClusterCenter();
        double similarity = 0.0;

        for (Datum point : cluster) {
            similarity += Utilities.computeDistance(point.features, center);
        }

        return similarity / cluster.size();
    }


    public double getClusterQuality() {
        return this.clusterQuality;
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
