package Clusterers.CompetitiveLearning;

import java.util.Arrays;

/**
 * Neuron class stores a cluster centroid and index to be used with the Competitive Learning Network clusterer
 */
public class Neuron {

    private final int clusterIndex;
    private double[] centroid;

    Neuron(int clusterIndex, double[] centroid) {
        this.clusterIndex = clusterIndex;
        this.centroid = centroid;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    public int getClusterIndex() {
        return clusterIndex;
    }

    @Override
    public String toString() {
        return "Index: " + clusterIndex + ", " + Arrays.toString(centroid);
    }
}
