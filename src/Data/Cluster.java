package Data;

/**
 * Cluster represents a contain of data points, a Dataset, with additional logic to compute centroids
 * Allows for simplified maintainability of cluster IDs
 */
public class Cluster extends Dataset {

    private int clusterId;
    private double[] clusterCenter;

    public Cluster(int clusterId) {
        this.clusterId = clusterId;
    }

    public Cluster(double[] clusterCenter) {
        this.clusterCenter = clusterCenter;
    }

    public int getClusterId() {
        return clusterId;
    }

    public double[] getClusterCenter() {
        return clusterCenter;
    }

    /**
     * Compute the centroid of the cluster as the average of all the points in the cluster
     */
    public double[] getCentroid() {
        // If no elements, return the assigned center
        if (this.isEmpty()) {
            return this.clusterCenter;
        }
        double[] centroid = new double[this.getFeatureSize()];

        // Sum over data points
        for (Datum datum : this) {
            for (int j = 0; j < centroid.length; j++) {
                centroid[j] += datum.features[j];
            }
        }

        // Normalize
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= this.size();
        }

        return centroid;
    }
}
