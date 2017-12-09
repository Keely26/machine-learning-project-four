package Utilites;

import Data.*;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Utilities {

    private static final Random random = new Random(System.nanoTime());

    public static double computeDistance(double[] point1, double[] point2) {
        if (point1 == null || point2 == null ||point1.length != point2.length) {
            System.out.println("Non-congruent dimensions!");
            System.exit(-1);
        }
        double sum = 0.0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }

        return Math.sqrt(sum);
    }

    public static Clustering assignPointsToClusters(Dataset dataset, List<double[]> centroids) {
        Clustering clustering = centroids.stream().map(Cluster::new).collect(Collectors.toCollection(Clustering::new));

        for (Datum dataPoint : dataset) {
            int nearestIndex = -1;
            double nearestDistance = Double.MAX_VALUE;
            for (int j = 0; j < clustering.size(); j++) {
                double distance = Utilities.computeDistance(dataPoint.features, clustering.get(j).getClusterCenter());
                if (distance < nearestDistance) {
                    nearestIndex = j;
                    nearestDistance = distance;
                }
            }
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

    public static void setFileOut(String filename, String extension) {
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(filename.concat(extension), false)), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setConsoleOut() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
