package Clusterers;

import Data.Dataset;
import Clusterers.AntColonyOptimization.Ant;

import java.util.Random;
import Data.Datum;
import java.util.List;

public class Grid {
    Ant ant;
    Datum[][] grid;
    int x;
    int y;
    boolean isVacant = false;
    boolean antOccupies = false;
    boolean dataOccupies = false;

    public Grid(Datum[][] grid, int x, int y){
        this.ant = ant;
        this.grid = grid;
    }

    public static Datum[][] createGrid (Dataset dataset) {
        //TODO
        Datum[][] grid = new Datum[dataset.size()][dataset.size()]; //initializes 2D array of datum
        grid = fillGrid(grid, dataset); //fills grid randomly with data points
        return grid;
    }

    public static int[] gridLocation(Dataset dataset, Datum[][] grid, Datum datum) {
        int[] gridLocation = new int[2];
        for (int i = 0; i < dataset.size(); i++) {
            for (int j = 0; j < dataset.size(); j++) {
                if (datum == grid[i][j]){
                    gridLocation[0] = i;
                    gridLocation[1] = j;
                }
            }
        }
        return gridLocation;
    }


    public static Datum[][] fillGrid(Datum[][] grid, Dataset dataset) {
        Random random = new Random();
        Datum[] dataPoints = dataset.toArray(new Datum[dataset.size()]); //array of data points
        Datum[][] datum = {dataPoints}; // 2D array of datum
        for (int i = 0; i < dataset.size(); i++) {
            for (int j = 0; j < dataset.size(); j++) {
                int x = random.nextInt(dataset.size());
                int y = random.nextInt(dataset.size());
                grid[x][y] = datum[i][j];
            }
        }
        return grid;
    }


    public static Datum getDataPoint(Datum[][] grid, int x, int y) {
        //TODO given a data point return its x and y coordinates on grid
        Datum dataPoint;
        dataPoint = grid[x][y];
        return dataPoint;
    }


/*

    public void updateLocationStatus(int x, int y, Dataset dataset, List<Ant> ants, int[][] searchSpace) {

        for (Ant ant: ants) {
            if(ant.curPosition == ant.curPosition) {
                antOccupies = true;
            }
        }
        for (Datum dataPoint: dataset) {
            if(ant.curPosition == searchSpace[x][y]) {
                dataOccupies = true;
                }
            }
        }
        */

}

