package Clusterers;

import Data.*;
import Utilites.Utilities;

import java.util.ArrayList;
import java.util.List;
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
            System.out.print("Iteration: " + iteration);
            // For each particle, update position, evaluate, update velocity, set personal and global bests
            particleSwarm.forEach((Particle particle) -> {
                // Update particle position
                // Update particle velocity
                particle.updatePosition();
                particle.updateVelocity(particleSwarm.getGlobalBest());
            });

            // Evaluate swarm
            double quality = this.particleSwarm.evaluateSwarm(dataset);
            System.out.println(", " + quality);

            iteration++;
        } while (iteration < maxIterations && notConverged());

        Clustering clustering = this.particleSwarm.getGlobalBest().stream().map(Cluster::new).collect(Collectors.toCollection(Clustering::new));
        return Utilities.assignClusters(dataset, clustering);
    }

    private boolean notConverged() {
        return true;
    }

    private void initializeParticles(Dataset dataset) {
        this.particleSwarm = new Swarm(this.numParticles);

        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        for (Datum point : dataset) {
            for (int j = 0, cutoff = dataset.getFeatureSize(); j < cutoff; j++) {
                double value = point.features[j];
                if (value > maxValue) {
                    maxValue = value;
                }
                if (value < minValue) {
                    minValue = value;
                }
            }
        }

        for (int i = 0; i < this.numParticles; i++) {
            List<double[]> initialClusterCenters = new ArrayList<>();
            List<double[]> initialCenterVelocities = new ArrayList<>();

            for (int j = 0; j < this.numClusters; j++) {
                double[] startingPosition = new double[dataset.getFeatureSize()];
                double[] startingVelocity = new double[dataset.getFeatureSize()];

                for (int k = 0; k < startingPosition.length; k++) {
                    startingPosition[k] = Utilities.randomDouble((int) minValue, (int) maxValue);
                    startingVelocity[k] = Utilities.randomDouble((int) Math.sqrt(maxValue));
                }
                initialClusterCenters.add(startingPosition);
                initialCenterVelocities.add(startingVelocity);
            }

            this.particleSwarm.add(new Particle(initialClusterCenters, initialCenterVelocities, this.inertia));
        }
        this.particleSwarm.evaluateSwarm(dataset);
    }
}
