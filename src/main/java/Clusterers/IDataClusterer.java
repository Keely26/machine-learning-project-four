package Clusterers;

import Data.Clustering;
import Data.Dataset;

public interface IDataClusterer {
    Clustering cluster(Dataset dataset);

    String toString();
}
