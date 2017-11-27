import Clusterers.ClustererType;
import Clusterers.IDataClusterer;
import Data.Cluster;
import Data.Clustering;
import Data.Dataset;
import Data.DatasetType;

public class Tester {

    private static final DatasetType dataFile = DatasetType.Glass;

    public static void main(String[] args) {

        /* Build datasets */
        Dataset glassDataset = DatasetBuilder.buildDataSet(DatasetType.Glass);
        Dataset banknoteDataset = DatasetBuilder.buildDataSet(DatasetType.Banknote);
        Dataset irisDataset = DatasetBuilder.buildDataSet(DatasetType.Iris);
        Dataset parkinsonsDataset = DatasetBuilder.buildDataSet(DatasetType.Parkinsons);
        Dataset retinopathyDataset = DatasetBuilder.buildDataSet(DatasetType.Retinopathy);

        /* Cluster */
        IDataClusterer dbScan = ClustererFactory.buildClusterer(ClustererType.DBSCAN);
        Clustering glassClustering = dbScan.cluster(glassDataset);
        Clustering banknoteClustering = dbScan.cluster(banknoteDataset);
        //  Clustering irisClustering = dbScan.cluster(irisDataset);
        // Clustering parkinsonsClustering = dbScan.cluster(parkinsonsDataset);
        // Clustering retinopathyClustering = dbScan.cluster(retinopathyDataset);
    }

    private static void printClusterStats(Clustering clusters, ClustererType type) {
        System.out.println(type.toString());
        for (Cluster cluster : clusters) {
            System.out.println("Cluster: " + cluster.getClusterId() + ", count: " + cluster.size());
        }
        System.out.println();
    }

    private static void resetDataset(Dataset dataset) {
        dataset.parallelStream().forEach(datum -> datum.setCluster(0));
    }
}
