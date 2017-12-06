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

    public double evaluateFitness() {
        double fitness = 0.0;

        for (Cluster cluster : this) {
            if (cluster.size() == 0) {
                continue;
            }
            double[] centroid = cluster.getCentroid();
            double distance = 0.0;
            for (Datum point : cluster) {
                distance += Utilities.computeDistance(point.features, centroid);
            }

            fitness += distance / cluster.size();
        }

        return fitness / this.size();
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
