package Clusterers;

import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.ArrayList;
import java.util.List;

public class PSOClusterer implements IDataClusterer {

    private final int numClusters;
    private final int numParticles;
    private final int maxIterations;
    private final double inertia;

    private Swarm particleSwarm;

    public PSOClusterer(int numClusters, int numParticles, int maxIterations, double inertia) {
        this.numClusters = numClusters;
        this.numParticles = numParticles;
        this.maxIterations = maxIterations;
        this.inertia = inertia;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        this.initializeParticles(dataset);

        int iteration = 0;
        do {
            logIteration(iteration, dataset);
            // For each particle, update position, evaluate, update velocity, set personal and global bests
            particleSwarm.parallelStream().forEach(particle -> {
                particle.updatePosition();
                particle.updateVelocity(particleSwarm.getGlobalBest().getPosition());
            });

            iteration++;
        } while (iteration < maxIterations && notConverged());

        // Retrieve best clustering
        return particleSwarm.getGlobalBest().getPersonalBest(dataset);
    }

    private boolean notConverged() {
        return true;
    }

    private void initializeParticles(Dataset dataset) {
        this.particleSwarm = new Swarm(this.numParticles);

        double[] maxValues = getMaxFeatureVector(dataset);
        double[] minValues = getMinFeatureVector(dataset);

        for (int i = 0; i < this.numParticles; i++) {
            List<double[]> initialClusterCenters = new ArrayList<>();
            List<double[]> initialCenterVelocities = new ArrayList<>();

            for (int j = 0; j < this.numClusters; j++) {
                double[] startingPosition = new double[dataset.getFeatureSize()];
                double[] startingVelocity = new double[dataset.getFeatureSize()];

                for (int k = 0; k < startingPosition.length; k++) {
                    startingPosition[k] = Utilities.randomDouble(minValues[k], maxValues[k]);
                    startingVelocity[k] = Utilities.randomDouble(0, Math.sqrt(maxValues[k]));
                }
                initialClusterCenters.add(startingPosition);
                initialCenterVelocities.add(startingVelocity);
            }

            this.particleSwarm.add(new Particle(initialClusterCenters, initialCenterVelocities, this.inertia));
        }

        this.particleSwarm.evaluateSwarm(dataset);
    }

    private double[] getMinFeatureVector(Dataset dataset) {
        double[] minValues = new double[dataset.getFeatureSize()];

        // Initialize each element to max value
        for (int i = 0; i < dataset.getFeatureSize(); i++) {
            minValues[i] = Double.MAX_VALUE;
        }

        // Find min value for each feature
        dataset.forEach(datum -> {
            for (int i = 0; i < dataset.getFeatureSize(); i++) {
                if (datum.features[i] < minValues[i]) {
                    minValues[i] = datum.features[i];
                }
            }
        });

        return minValues;
    }

    private double[] getMaxFeatureVector(Dataset dataset) {
        double[] maxValues = new double[dataset.getFeatureSize()];

        // Initialize each element to min value
        for (int i = 0; i < dataset.getFeatureSize(); i++) {
            maxValues[i] = Double.MIN_VALUE;
        }

        // Find max value for each feature
        dataset.forEach(datum -> {
            for (int i = 0; i < dataset.getFeatureSize(); i++) {
                if (datum.features[i] > maxValues[i]) {
                    maxValues[i] = datum.features[i];
                }
            }
        });

        return maxValues;
    }

    private void logIteration(int iteration, Dataset data) {
        Clustering currentBest = this.particleSwarm.getGlobalBest().getPersonalBest(data);
        System.out.print("Iteration: " + iteration);
        System.out.println(", best clustering:");
        System.out.println(currentBest.toString());
        System.out.println("Quality: " + currentBest.evaluateClusters());
        System.out.println();
    }
}
