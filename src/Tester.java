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

    private static int numTrials = 30;
    private static boolean loggingEnabled = false;
    private static DecimalFormat doubleFormatter = new DecimalFormat("#.######");

    public static void main(String[] args) {
        runTrials(DatasetType.Iris);
        runTrials(DatasetType.Glass);
        runTrials(DatasetType.Banknote);
        runTrials(DatasetType.Parkinsons);
        runTrials(DatasetType.Retinopathy);
    }

    private static void runTrials(DatasetType type) {
        Dataset dataset = DatasetBuilder.buildDataSet(type);
        List<IDataClusterer> clusterers = new ArrayList<>();
        clusterers.add(ClustererFactory.buildClusterer(ClustererType.CompetitiveNetwork));

        System.out.println("Computing " + numTrials + " trial statistics for " + dataset.toString());

        clusterers.forEach(clusterer -> {
            double[] fitnesses = new double[numTrials];
            double[] runtimes = new double[numTrials];

            System.out.println(clusterer.toString());

            for (int i = 0; i < numTrials; i++) {
                System.out.print("Trial "
                        .concat(Integer.toString(i + 1))
                        .concat(".. "));
                if (!loggingEnabled) {
                    System.setOut(new PrintStream(new OutputStream() {
                        @Override
                        public void write(int b) {
                            // NOP
                        }
                    }));
                }
                long startTime = System.currentTimeMillis();
                Clustering clustering = clusterer.cluster(dataset);
                double quality = clustering.evaluateFitness();
                printClusterStats(clustering, quality);
                fitnesses[i] = quality;
                runtimes[i] = (System.currentTimeMillis() - startTime) / 1000.0;

                if (!loggingEnabled) {
                    Utilities.setConsoleOut();
                }
                System.out.println("done, Runtime: ".concat(Double.toString(runtimes[i]).concat("s")));
                resetDataset(dataset);
            }

            // Compute statistics
            double meanFit = 0.0, meanRun = 0.0, stdDevFit = 0.0, stdDevRun = 0.0, windowFit, windowRun;
            for (int i = 0; i < numTrials; i++) {
                meanFit += fitnesses[i];
                meanRun += runtimes[i];
            }
            meanFit /= numTrials;
            meanRun /= numTrials;
            for (int i = 0; i < numTrials; i++) {
                stdDevFit += Math.pow(meanFit - fitnesses[i], 2);
                stdDevRun += Math.pow(meanRun - runtimes[i], 2);

            }
            stdDevFit = Math.sqrt(stdDevFit / numTrials);
            stdDevRun = Math.sqrt(stdDevRun / numTrials);
            windowFit = (1.96) * stdDevFit / Math.sqrt(numTrials);
            windowRun = (1.96) * stdDevRun / Math.sqrt(numTrials);

            System.out.println("Mean Quality:\t\t\t"
                    .concat(doubleFormatter.format(meanFit)));
            System.out.println("StdDev Quality:\t\t\t"
                    .concat(doubleFormatter.format(stdDevFit)));
            System.out.println("95% Quality interval:\t("
                    .concat(doubleFormatter.format(meanFit - windowFit))
                    .concat(", ")
                    .concat(doubleFormatter.format(meanFit + windowFit))
                    .concat(")"));

            System.out.println("Mean Runtime:\t\t\t"
                    .concat(doubleFormatter.format(meanRun)));
            System.out.println("StdDev Runtime:\t\t\t"
                    .concat(doubleFormatter.format(stdDevRun)));
            System.out.println("95% Runtime interval:\t("
                    .concat(doubleFormatter.format(meanRun - windowRun))
                    .concat(", ")
                    .concat(doubleFormatter.format(meanRun + windowRun))
                    .concat(")"));

            System.out.println();
        });
    }


    private static void printClusterStats(Clustering clusters, double quality) {
        if (clusters.size() == 0) {
            System.out.println("No clusters!");
            return;
        }

        System.out.println("Quality: " + quality);
        for (Cluster cluster : clusters) {
            System.out.println("Cluster: " + cluster.getClusterId() + ", count: " + cluster.size());
        }
        System.out.println();
    }

    private static void resetDataset(Dataset dataset) {
        dataset.parallelStream().forEach(datum -> datum.setCluster(0));
    }

    private static void findGoodParams(DatasetType type) {
        Dataset dataset = DatasetBuilder.buildDataSet(type);
        int bestMin = -1;
        double bestEp = -1;
        double bestQual = -1;
        for (int i = 5; i <= 15; i++) {
            for (double j = 0; j <= 10; j += 0.25) {
                System.out.println("MinPts: " + i + ", Epsilon: " + j);
                IDataClusterer DBSCAN = new DBSCAN(i, j);
                Clustering clustering = DBSCAN.cluster(dataset);
                double quality = clustering.evaluateFitness();
                if (quality > bestQual) {
                    bestQual = quality;
                    bestEp = j;
                    bestMin = i;

                }
                printClusterStats(clustering, quality);
                resetDataset(dataset);
            }
        }
        System.out.println("MinPts: " + bestMin);
        System.out.println("Epsilon: " + bestEp);
    }
}
