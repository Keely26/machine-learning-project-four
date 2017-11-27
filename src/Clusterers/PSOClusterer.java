package Clusterers;

import Data.Clustering;
import Data.Dataset;

public class PSOClusterer implements IDataClusterer {

    private final int numClusters;
    private final int numParticles;

    public PSOClusterer(int numClusters, int numParticles) {
        this.numClusters = numClusters;
        this.numParticles = numParticles;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        return null;
    }
}
