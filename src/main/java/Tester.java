import Clusterers.*;
import Clusterers.DBSCAN.DBSCAN;
import Data.*;
import Utilites.Utilities;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tester {

    private static boolean loggingEnabled = true;

    private static DecimalFormat doubleFormatter = new DecimalFormat("#.####");

    public static void main(String[] args) {
        Dataset dataset = DatasetBuilder.buildDataSet(DatasetType.Glass);
        IDataClusterer clusterer = null;
        switch (args[0]) {
            case "dbscan":
                clusterer = ClustererFactory.buildClusterer(ClustererType.DBSCAN);
                break;
            case "kmeans":
                clusterer = ClustererFactory.buildClusterer(ClustererType.kMeans);
                break;
            case "pso":
                clusterer = ClustererFactory.buildClusterer(ClustererType.PSOClusterer);
                break;
            case "aco":
                clusterer = ClustererFactory.buildClusterer(ClustererType.ACOClusterer);
                break;
            case "comp":
            default:
                clusterDataset(DatasetType.Glass);
        }
        if (clusterer != null) {
            System.out.println(clusterer.toString());
            Clustering clustering = clusterer.cluster(dataset);
            printClusterStats(clustering, clusterer.toString());
        }
    }

    private static void clusterDataset(DatasetType type) {
        Dataset dataset = DatasetBuilder.buildDataSet(type);
        List<IDataClusterer> clusterers = new ArrayList<>();
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.kMeans));
        //  clusterers.add(ClustererFactory.buildClusterer(ClustererType.DBSCAN));
        //  clusterers.add(ClustererFactory.buildClusterer(ClustererType.CompetitiveNetwork));
        //  clusterers.add(ClustererFactory.buildClusterer(ClustererType.PSOClusterer));
        //  clusterers.add(ClustererFactory.buildClusterer(ClustererType.ACOClusterer));

        clusterers.forEach(clusterer -> {
            if (!loggingEnabled) {
                System.setOut(new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        // NOP
                    }
                }));
            }
            Clustering clustering = clusterer.cluster(dataset);
            if (!loggingEnabled) {
                Utilities.setConsoleOut();
            }
            System.out.println(clusterer.toString());
            System.out.println("Number of Clusters: " + clustering.size());
            System.out.println("Average Inter-Cluster Distance:\t" + doubleFormatter.format(clustering.evaluateInterClusterDistance()));
            System.out.println("Average Intra-Cluster Distance:\t" + doubleFormatter.format(clustering.evaluateIntraClusterDistance()));
            System.out.println("Cluster Quality:\t\t\t\t" + doubleFormatter.format(clustering.evaluateFitness()));
            System.out.println();
            resetDataset(dataset);
        });
    }

    private static void printClusterStats(Clustering clusters, String type) {
        if (clusters.size() == 0) {
            System.out.println("No clusters!");
            return;
        }
        double quality = clusters.evaluateFitness();
        System.out.println(type);
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
        double epsilon = 0;
        double bestQuality = 0.0;
        for (int i = 0; i <= 20; i++) {
            for (double j = 0; j <= 20; j += 0.05) {
                System.out.println("MinPts: " + i + ", Epsilon: " + j);
                IDataClusterer clusterer = new DBSCAN(i, j);
                Clustering clustering = clusterer.cluster(dataset);
                printClusterStats(clustering, clusterer.toString());
                if (clustering.evaluateFitness() < bestQuality) {
                    bestQuality = clustering.evaluateFitness();
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
