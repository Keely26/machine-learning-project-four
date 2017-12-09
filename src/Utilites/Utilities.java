package Utilites;

import Data.*;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Utilities class provides common functionality and static utility function for use throughout the application
 * Methods should be public static and have no side effects
 */
@SuppressWarnings("WeakerAccess")
public class Utilities {

    private static final Random random = new Random(System.nanoTime());

    /**
     * Computes the Euclidean distance between to vectors
     */
    public static double computeDistance(double[] point1, double[] point2) {
        // If invalid parameters are supplied, throw an error
        if (point1 == null || point2 == null || point1.length != point2.length) {
            throw new IllegalArgumentException("Non-congruent dimensions!");
        }

        // Sum over the length of the vectors
        double sum = 0.0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }

        // Normalize and return
        return Math.sqrt(sum);
    }

    /**
     * Creates a new cluster for each of the provided centroids and adds each
     * data point to the cluster to which it is closest
     *
     * @param dataset   Collection of points to be assigned to clusters
     * @param centroids Collection of cluster center vectors
     * @return A Clustering with points assigned to their respective clusters
     */
    public static Clustering assignPointsToClusters(Dataset dataset, List<double[]> centroids) {
        // Create a clustering consisting of clusters centered at each of the centroids
        Clustering clustering = centroids.stream().map(Cluster::new).collect(Collectors.toCollection(Clustering::new));

        for (Datum dataPoint : dataset) {
            int nearestIndex = -1;
            double nearestDistance = Double.MAX_VALUE;
            // Find the nearest cluster
            for (int j = 0; j < clustering.size(); j++) {
                double distance = Utilities.computeDistance(dataPoint.features, clustering.get(j).getClusterCenter());
                if (distance < nearestDistance) {
                    nearestIndex = j;
                    nearestDistance = distance;
                }
            }
            // Add to nearest cluster
            clustering.get(nearestIndex).add(dataPoint);
        }

        return clustering;
    }

    public static Integer randomInteger(int bound) {
        return random.nextInt(bound);
    }

    public static double randomDouble(double lowerBound, double upperBound) {
        return randomDouble((int) (lowerBound + 1), (int) (upperBound + 1)) % upperBound;
    }

    public static double randomDouble(int lowerBound, int upperBound) {
        return randomDouble(upperBound + lowerBound) - lowerBound;
    }

    public static double randomDouble(int upperBound) {
        return random.nextDouble() * upperBound;
    }

    public static void setFileOut(String filename) {
        setFileOut(filename, ".txt");
    }

    /**
     * Set system out to the provided file path
     */
    public static void setFileOut(String filename, String extension) {
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(filename.concat(extension), false)), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return system out to the default console
     */
    public static void setConsoleOut() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
