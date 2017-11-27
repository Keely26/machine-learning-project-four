package Clusterers;

import Data.Clustering;
import Data.Dataset;

public class KMeans implements IDataClusterer {

    private final int numClusters;

    public KMeans(int numClusters) {
        this.numClusters = numClusters;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        return null;
    }
}
