import Clusterers.*;
import Data.Cluster;
import Data.Clustering;
import Data.Dataset;
import Data.DatasetType;

public class Tester {

    private static final DatasetType dataFile = DatasetType.Glass;

    public static void main(String[] args) {
        findGoodParams(DatasetBuilder.buildDataSet(DatasetType.Glass));

        /* Build datasets */
//        Dataset glassDataset = DatasetBuilder.buildDataSet(DatasetType.Glass);
//        Dataset banknoteDataset = DatasetBuilder.buildDataSet(DatasetType.Banknote);
//        Dataset irisDataset = DatasetBuilder.buildDataSet(DatasetType.Iris);
//        Dataset parkinsonsDataset = DatasetBuilder.buildDataSet(DatasetType.Parkinsons);
//        Dataset retinopathyDataset = DatasetBuilder.buildDataSet(DatasetType.Retinopathy);

        /* Cluster */
//        IDataClusterer dbScan = ClustererFactory.buildClusterer(ClustererType.DBSCAN);
//        Clustering glassClustering = dbScan.cluster(glassDataset);
//        printClusterStats(glassClustering, ClustererType.DBSCAN);

      //  Clustering banknoteClustering = dbScan.cluster(banknoteDataset);
        //  Clustering irisClustering = dbScan.cluster(irisDataset);
        // Clustering parkinsonsClustering = dbScan.cluster(parkinsonsDataset);
        // Clustering retinopathyClustering = dbScan.cluster(retinopathyDataset);
    }

    private static void printClusterStats(Clustering clusters, ClustererType type) {
        if (clusters.size() == 0) {
            System.out.println("No clusters!");
            return;
        }
        Double quality = clusters.evaluateClusters();
        System.out.println(type.toString());
        System.out.println("Quality: " + quality);
        for (Cluster cluster : clusters) {
            System.out.println("Cluster: " + cluster.getClusterId() + ", count: " + cluster.size());
        }
        System.out.println();
    }

    private static void resetDataset(Dataset dataset) {
        dataset.parallelStream().forEach(datum -> datum.setCluster(0));
    }

    private static void findGoodParams(Dataset dataset) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.println("MinPts: " + i + ", Epsilon: " + j);
                IDataClusterer DBSCAN = new DBSCAN(i, j);
                DBSCAN.cluster(dataset);
                printClusterStats(DBSCAN.cluster(dataset), ClustererType.DBSCAN);
                resetDataset(dataset);
            }
        }
    }
}
