import Clusterers.*;
import Clusterers.DBSCAN.DBSCAN;
import Data.*;
import Utilites.Utilities;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tester {

    private static boolean loggingEnabled = false;

    private static DecimalFormat doubleFormatter = new DecimalFormat("#.######");

    public static void main(String[] args) {
        Dataset dataset = DatasetBuilder.buildDataSet(DatasetType.Iris);
        IDataClusterer cluster = ClustererFactory.buildClusterer(ClustererType.ACOClusterer);
        cluster.cluster(dataset);
//        tenFoldValidation(DatasetType.Iris);
//        tenFoldValidation(DatasetType.Glass);
//        tenFoldValidation(DatasetType.Banknote);
//        tenFoldValidation(DatasetType.Parkinsons);
//        tenFoldValidation(DatasetType.Retinopathy);
    }

    private static void tenFoldValidation(DatasetType type) {
        Dataset dataset = DatasetBuilder.buildDataSet(type);
        List<IDataClusterer> clusterers = new ArrayList<>();
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.kMeans));
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.DBSCAN));
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.CompetitiveNetwork));
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.PSOClusterer));
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.ACOClusterer));

        System.out.println("Computing 10 fold statistics for " + dataset.toString());

        clusterers.forEach(clusterer -> {
            double[] fitnesses = new double[10];

            System.out.println(clusterer.toString());

            for (int i = 0; i < 10; i++) {
                System.out.print("Trial ".concat(Integer.toString(i + 1).concat(".. ")));
                if (!loggingEnabled) {
                    System.setOut(new PrintStream(new OutputStream() {
                        @Override
                        public void write(int b) {
                            // NOP
                        }
                    }));
                }
                long startTime = System.currentTimeMillis();
                fitnesses[i] = clusterer.cluster(dataset).evaluateFitness();

                if (!loggingEnabled) {
                    Utilities.setConsoleOut();
                }
                System.out.println("done, Runtime: ".concat(Double.toString((System.currentTimeMillis() - startTime) / 1000.0)).concat("s"));
            }

            // Compute statistics
            double mean = 0.0, stdDev = 0.0, window;
            for (double fitness : fitnesses) {
                mean += fitness;
            }
            mean /= fitnesses.length;
            for (double fitness : fitnesses) {
                stdDev += Math.pow(mean - fitness, 2);
            }
            stdDev = Math.sqrt(stdDev / fitnesses.length);
            window = (1.96) * stdDev / Math.sqrt(fitnesses.length);

            System.out.println("Mean:\t\t\t".concat(doubleFormatter.format(mean)));
            System.out.println("StdDev:\t\t\t".concat(doubleFormatter.format(stdDev)));
            System.out.println("95% interval:\t("
                    .concat(doubleFormatter.format(mean - window))
                    .concat(", ")
                    .concat(doubleFormatter.format(mean + window))
                    .concat(")"));

            System.out.println();
        });
    }


    private static void printClusterStats(Clustering clusters, ClustererType type) {
        if (clusters.size() == 0) {
            System.out.println("No clusters!");
            return;
        }
        double quality = clusters.evaluateFitness();
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
        double epsilon = 0;
        double bestQuality = 0.0;
        for (int i = 5; i <= 20; i++) {
            for (double j = 0; j <= 20; j += 0.1) {
                System.out.println("MinPts: " + i + ", Epsilon: " + j);
                IDataClusterer DBSCAN = new DBSCAN(i, j);
                Clustering clustering = DBSCAN.cluster(dataset);
                printClusterStats(clustering, ClustererType.DBSCAN);
                if (clustering.evaluateFitness() > bestQuality) {
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
