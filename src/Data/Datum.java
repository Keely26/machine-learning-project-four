package Data;

public class Datum {

    public final double[] features;
    private int cluster;
    private boolean core;

    public Datum(double[] features) {
        this.features = features;
    }

    /*
     * Return the manhattan distance between this point an an arbitrary point of the same dimensionality
     */
    public double computeDistance(double[] featureVector) {
        assert featureVector.length == this.features.length : "Non-congruent feature lengths!";

        double distance = 0.0;

        for (int i = 0; i < features.length; i++) {
            distance += Math.pow(features[i] - featureVector[i], 2);
        }

        return distance;
    }

    public int getCluster() {
        return this.cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return Integer.toString(cluster);
    }

    public boolean isCore() {
        return this.core;
    }

    public void setCore(boolean core) {
        this.core = core;
    }
}
