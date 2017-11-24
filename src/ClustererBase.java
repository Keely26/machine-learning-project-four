import java.util.List;

public class ClustererBase implements IDataClusterer {

    protected void evaluateClusters(List<Cluster> clusters) {
        // Calculate average intra-cluster distance (lower is better)
        double intraSum = 0.0;
        for (Cluster cluster : clusters) {
            for (Sample sample : cluster) {
                for (Sample neighbor : cluster) {
                    intraSum += sample.computeDistance(neighbor);
                }
            }
        }

        // Calculate average inter-cluster distance (higher is better)
        double interSum = 0.0;


    }

    @Override
    public List<Cluster> cluster(Dataset dataset) {
        assert false : "Base class cannot be used to cluster!";
        return null;
    }
}
