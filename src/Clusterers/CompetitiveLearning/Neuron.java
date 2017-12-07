package Clusterers.CompetitiveLearning;

@SuppressWarnings("WeakerAccess")
public class Neuron {

    private final int numConnections;
    private double[] featureOrCenter;

    public Neuron(int numConnections) {
        this.numConnections = numConnections;
    }

    public int getNumConnections() {
        return numConnections;
    }

    public double[] getFeatureOrCenter() {
        return featureOrCenter;
    }

    public void setFeatureOrCenter(double[] newFeatureOrCenter) {
        featureOrCenter = newFeatureOrCenter;
    }
}
