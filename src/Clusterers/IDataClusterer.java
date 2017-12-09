package Clusterers;

import Data.Clustering;
import Data.Dataset;

/**
 * Clusterer interface enforces the cluster method and allows the exchange of implementations between
 * individual clusters. Interface simplifies the lifecycle of instances of various data clusters
 */
public interface IDataClusterer {
    Clustering cluster(Dataset dataset);
    String toString();
}
