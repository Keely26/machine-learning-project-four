public class PSOClusterer extends ClustererBase {

    private final int numClusters;
    private final int numParticles;

    PSOClusterer(int numClusters, int numParticles) {
        this.numClusters = numClusters;
        this.numParticles = numParticles;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        return null;
    }
}
