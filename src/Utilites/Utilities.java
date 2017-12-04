package Utilites;

import java.util.Random;

public class Utilities {

    public static final Random random = new Random(System.nanoTime());

    public static Integer randomInteger(int bound) {
        return random.nextInt(bound);
    }

    public static double randomDouble(int lowerBound, int upperBound) {
        return randomDouble(upperBound + lowerBound) - lowerBound;
    }

    public static double randomDouble(int bound) {
        return random.nextDouble() * bound;
    }
}
