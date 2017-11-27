package Data;

public class Cluster extends Dataset {

    private int clusterId;
    private double[] clusterCenter;

    public Cluster(Dataset elements, int clusterId) {
        super(elements);
        this.clusterId = clusterId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public double[] getClusterCenter() {
        return clusterCenter;
    }

    public void setClusterCenter(double[] clusterCenter) {
        this.clusterCenter = clusterCenter;
    }
}
