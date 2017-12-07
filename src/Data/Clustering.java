package Data;

import Utilites.Utilities;

import java.util.*;

public class Clustering extends ArrayList<Cluster> {

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

        return fitness / this.getSize();
    }

    private int getSize() {
        int num = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).size() > 0) {
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
