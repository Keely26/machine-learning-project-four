import java.util.Arrays;

public class Tester {

    private static final DatasetType dataFile = DatasetType.Glass;

    public static void main(String[] args) {

        Dataset dataset = DatasetBuilder.buildDataSet(dataFile);
        dataset.forEach(datum -> System.out.println(Arrays.toString(datum.features)));

        IDataClusterer dbScan = ClustererFactory.buildClusterer(ClustererType.DBScan);
        Clustering clusters = dbScan.cluster(dataset);

        // printClusterStats(clusters, ClustererType.DBScan);
    }

    private static void printClusterStats(Clustering clusters, ClustererType type) {
        System.out.println(type.toString());
        for (Cluster cluster : clusters) {
            System.out.println("Cluster: " + cluster.getClusterId() + ", count: " + cluster.size());
        }
        System.out.println();
    }
}
