package Data;

import java.util.Arrays;

public class Datum {

    public final double[] features;
    private int cluster;
    private boolean core;
    private boolean carried;

    public Datum(double[] features) {
        this.features = features;
    }

    public int getCluster() {
        return this.cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public boolean isCore() {
        return this.core;
    }

    public void setCore(boolean core) {
        this.core = core;
    }

    @Override
    public String toString() {
        return Integer.toString(cluster).concat(", ").concat(Arrays.toString(features));
    }

    public boolean isCarried() {
        return carried;
    }

    public void setCarried(boolean carried) {
        this.carried = carried;
    }
}
