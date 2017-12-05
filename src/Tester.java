import Clusterers.*;
import Data.*;

public class Tester {

    private static final DatasetType dataFile = DatasetType.Glass;

    public static void main(String[] args) {

        Dataset dataset = DatasetBuilder.buildDataSet(DatasetType.Glass);
        IDataClusterer psoClusterer = ClustererFactory.buildClusterer(ClustererType.PSOClusterer);
        Clustering clustering = psoClusterer.cluster(dataset);
        printClusterStats(clustering, ClustererType.PSOClusterer);
        //  findGoodParams(DatasetBuilder.buildDataSet(DatasetType.Glass));

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
        double quality = clusters.evaluate();
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
        int minPoints = 0;
        int epsilon = 0;
        double bestQuality = 0.0;
        for (int i = 5; i <= 20; i++) {
            for (int j = 0; j <= 20; j++) {
                System.out.println("MinPts: " + i + ", Epsilon: " + j);
                IDataClusterer DBSCAN = new DBSCAN(i, j);
                Clustering clustering = DBSCAN.cluster(dataset);
                printClusterStats(clustering, ClustererType.DBSCAN);
                if (clustering.getClusterQuality() > bestQuality) {
                    bestQuality = clustering.getClusterQuality();
                    minPoints = i;
                    epsilon = j;
                }
                resetDataset(dataset);
            }
        }
        System.out.println("MinPts: " + minPoints);
        System.out.println("Epsilon: " + epsilon);
    }
}
