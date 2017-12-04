package Clusterers;

import Data.*;
import Utilites.Utilities;

import java.util.*;
import java.util.stream.Collectors;

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
            System.out.println("Iteration: " + iteration);
            Particle best = new Particle(particleSwarm.getGlobalBest(), null, 0);
            Clustering c = Utilities.assignClusters(dataset, particleSwarm.getGlobalBest());
            c.forEach(cluster -> {
                System.out.println("Cluster: " + cluster.getClusterId() + ", Count: " + cluster.size());
            });
            // For each particle, update position, evaluate, update velocity, set personal and global bests
            particleSwarm.forEach((Particle particle) -> {
                particle.updatePosition();
                particle.updateVelocity(particleSwarm.getGlobalBest());
            });

            // Evaluate swarm
            double quality = this.particleSwarm.evaluateSwarm(dataset);
            System.out.println(", " + quality);

            iteration++;
        } while (iteration < maxIterations && notConverged());

        // Retrieve best clustering
        Clustering clustering = this.particleSwarm.getGlobalBest()
                .stream()
                .map(Cluster::new)
                .collect(Collectors.toCollection(Clustering::new));
        return Utilities.assignClusters(dataset, clustering);
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

    private static double[] getMinFeatureVector(Dataset dataset) {
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

    private static double[] getMaxFeatureVector(Dataset dataset) {
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
}
