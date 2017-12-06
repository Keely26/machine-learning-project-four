package Data;

@SuppressWarnings("WeakerAccess")
public class Cluster extends Dataset {

    private int clusterId;
    private double[] clusterCenter;

    public Cluster(Dataset elements, int clusterId) {
        super(elements);
        this.clusterId = clusterId;
    }

    public Cluster(int clusterId) {
        this.clusterId = clusterId;
    }

    public Cluster(double[] clusterCenter) {
        this.clusterCenter = clusterCenter;
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

    public double[] getCentroid() {
        double[] centroid = new double[this.getFeatureSize()];

        for (Datum datum : this) {
            for (int j = 0; j < centroid.length; j++) {
                centroid[j] += datum.features[j];
            }
        }
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= this.size();
        }

        return centroid;
    }
}
