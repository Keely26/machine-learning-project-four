package Data;

public class Datum {

    public final double[] features;
    private int cluster;
    private boolean core;

    Datum(double[] features) {
        this.features = features;
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
