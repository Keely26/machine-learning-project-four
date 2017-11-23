public class Sample {

    public final double[] features;
    private int cluster;

    public Sample(double[] features) {
        this.features = features;
    }

    public double computeDistance(Sample sample) {
        assert sample.features.length == this.features.length : "Non-congruent feature lengths!";

        double distance = 0.0;

        for (int i = 0; i < features.length; i++) {
            distance += Math.abs(features[i] - sample.features[i]);
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
