package Data;

public class Datum {

    public final double[] features;
    private int cluster;

    public Datum(double[] features) {
        this.features = features;
    }

    public double computeDistance(Datum datum) {
        assert datum.features.length == this.features.length : "Non-congruent feature lengths!";

        double distance = 0.0;

        for (int i = 0; i < features.length; i++) {
            distance += Math.abs(features[i] - datum.features[i]);
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
}
