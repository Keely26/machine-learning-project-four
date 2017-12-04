package Utilites;

import Data.*;

import java.io.*;
import java.util.Random;

public class Utilities {

    public static final Random random = new Random(System.nanoTime());

    public static Clustering assignClusters(Dataset dataset, Clustering clustering) {
        // For each element in the dataset, add it to the nearest cluster
        for (Datum datum : dataset) {
            Cluster nearestCluster = clustering.get(0);
            double clusterDistance = Double.MAX_VALUE;
            for (Cluster cluster : clustering) {
                double distance = datum.computeDistance(cluster.getClusterCenter());
                if (distance < clusterDistance) {
                    nearestCluster = cluster;
                    clusterDistance = distance;
                }
            }
            nearestCluster.add(datum);
        }
        return clustering;
    }

    public static Integer randomInteger(int bound) {
        return random.nextInt(bound);
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
