package Utilites;

import java.io.*;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class Utilities {

    private static final Random random = new Random(System.nanoTime());

    public static double computeDistance(double[] point1, double[] point2) {
        assert point1.length == point2.length : "Non-congruent dimensions!";

        double sum = 0.0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }

        return Math.sqrt(sum);
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
