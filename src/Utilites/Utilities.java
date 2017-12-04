package Utilites;

import java.io.*;
import java.util.Random;

public class Utilities {

    public static final Random random = new Random(System.nanoTime());

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
