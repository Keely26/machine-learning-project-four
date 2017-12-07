package Clusterers;
/**
 * Ant class acts as a container to hold a set of antID numbers, their positions, and their paths
 */
import Data.Dataset;
import Data.Datum;

import java.util.List;
import java.util.Random;

public class Ant {
    boolean isCarrying;
    int antID;
    int x;
    int y;
    int curPosition;
    Datum carrying;

    public Ant(int x, int y) {
        this.antID = antID;
        this.x = x;
        this.y = y;
    }

    public static List<Ant> createAnts(Dataset dataset, List<Ant> ants) {
        Random random = new Random();
        //Initialize ants with random positions
        for (int i = 0; i < ants.size(); i++) {
            int x = random.nextInt(dataset.size());
            int y = random.nextInt(dataset.size());
            do {
                ants.add(i, new Ant(x, y));
            } while (!ants.contains(new Ant(x, y)));
        }
        return ants;
    }

    public static void move(Ant ant, int x, int y, Datum[][] grid) {
        //TODO
        int[] antLocation = new int[]{x, y};
        Datum corpse = Grid.getDataPoint(grid, x, y);

        if (Grid.getDataPoint(grid, x, y + 1) == null) { //if above is empty
            x = x;
            y = y + 1;
        } else if(Grid.getDataPoint(grid, x, y - 1) == null) { //if below is empty
            x = x;
            y = y - 1;
        } else if(Grid.getDataPoint(grid, x + 1, y) == null) { // if right is empty
            x = x + 1;
            y = y;
        } else if(Grid.getDataPoint(grid, x - 1, y) == null) { // if left is empty
            x = x - 1;
            y = y;
        }

    }

    public Datum isCarrying(Datum datum) {
        carrying = datum;
        return carrying;
    }


    public Datum[] checkNeighborhood(double radius) {
        //TODO
        //TODO return list of data points in neighborhood
        Datum[] neighborhood = new Datum[(int)Math.pow(radius, 2)];
        return neighborhood;
        /*
            private void checkNeighborhood(double radius, int x, int y, int[] neighborhood, Ant ant, int[][] grid) {

        for (int i = 0; i < radius; i++) {

            if (grid[ant.x][ant.y + i]
            }
        }
    }
         */
    }

    public Datum myCorpse() {
        //TODO
        double[] feature = new double[2];
        Datum corpse = new Datum(feature);
        return corpse;
    }






}
