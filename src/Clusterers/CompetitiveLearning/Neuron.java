package Clusterers.CompetitiveLearning;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class Neuron {

    private final int clusterIndex;
    private double[] centroid;

    public Neuron(int clusterIndex, double[] centroid) {
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
