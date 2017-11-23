import java.util.Arrays;
import java.util.List;

public class Tester {

    private static final DatasetType dataFile = DatasetType.Glass;

    public static void main(String[] args) {

        Dataset dataset = DatasetBuilder.buildDataSet(dataFile);
        dataset.forEach(datum -> System.out.println(Arrays.toString(datum.features)));

        IDataClusterer dbScan = ClustererFactory.buildClusterer(ClustererType.DBScan);
        List<Cluster> clusters = dbScan.cluster(dataset);

        printClusterStats(clusters, ClustererType.DBScan);
    }

    private static void printClusterStats(List<Cluster> clusters, ClustererType type) {
        System.out.println(type.toString());
        for (Cluster cluster : clusters) {
            System.out.println("Cluster: " + cluster.clusterId + ", count: " + cluster.size());
        }
        System.out.println();
    }
}
