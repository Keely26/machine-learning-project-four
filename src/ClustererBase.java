import java.util.ArrayList;
import java.util.List;

public class ClustererBase implements IDataClusterer {

    protected void evaluateClusters(List<Cluster> clusters) {
        // Calculate average intra-cluster distance (lower is better)
        double intraSum = 0.0;
        for (Cluster cluster : clusters) {
            double clusterDistance = getAvgIntraClusterDistance(cluster);
            System.out.println("Cluster " + cluster.clusterId + " intra-cluster distance: " + clusterDistance);
            intraSum += clusterDistance;
        }
        intraSum /= clusters.size();

        // Calculate average inter-cluster distance (higher is better)
        List<Sample> clusterCenters = new ArrayList<>(clusters.size());
        clusters.forEach(cluster -> clusterCenters.add(getClusterCenter(cluster)));

        double interSum = 0.0;
        for (Sample center1 : clusterCenters) {
            for (Sample center2 : clusterCenters) {
                if (!center1.equals(center2)) {
                    interSum += center1.computeDistance(center2);
                }
            }
            interSum /= clusterCenters.size() - 1;
        }

        System.out.println("Avg Inter-cluster distance: " + interSum);
        System.out.println("Cluster quality: " + intraSum / interSum);
    }

    private double getAvgIntraClusterDistance(Cluster cluster) {
        double interClusterDistance = 0.0;
        for (Sample sample : cluster) {
            for (Sample neighbor : cluster) {
                interClusterDistance += sample.computeDistance(neighbor);
            }
            interClusterDistance /= cluster.size();
        }
        return interClusterDistance;
    }

    private Sample getClusterCenter(Cluster cluster) {
        double[] avgVector = new double[cluster.get(0).features.length];

        // Sum feature values for all elements in the cluster
        cluster.forEach(sample -> {
            for (int i = 0, bound = sample.features.length; i < bound; i++) {
                avgVector[i] += sample.features[i];
            }
        });

        // Normalize
        for (int i = 0, bound = avgVector.length; i < bound; i++) {
            avgVector[i] /= cluster.size();
        }

        return new Sample(avgVector);
    }


    @Override
    public List<Cluster> cluster(Dataset dataset) {
        assert false : "Base class cannot be used to cluster!";
        return null;
    }
}
