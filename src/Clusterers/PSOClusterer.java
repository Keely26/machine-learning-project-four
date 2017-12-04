package Clusterers;

import Data.*;
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
            // For each particle, update position, evaluate, update velocity, set personal and global bests
            particleSwarm.forEach((Particle particle) -> {
                // Update particle position
                List<double[]> currentPosition = particle.getPosition();
                List<double[]> currentVelocity = particle.getVelocity();
                List<double[]> updatedPosition = new ArrayList<>();

                for (int i = 0; i < currentPosition.size(); i++) {
                    double[] clusterCenter = currentPosition.get(i);
                    double[] centerVelocity = currentVelocity.get(i);
                    double[] updatedCenterPosition = new double[clusterCenter.length];

                    assert clusterCenter.length == centerVelocity.length : "Non-congruent position/velocity vectors!";

                    for (int j = 0, length = clusterCenter.length; j < length; j++) {
                        updatedCenterPosition[j] = clusterCenter[j] + centerVelocity[j];
                    }

                    updatedPosition.add(updatedCenterPosition);
                }

                // Update particle velocity
                List<double[]> updatedVelocity = new ArrayList<>();
                List<double[]> personalBest = particle.getPersonalBest();
                List<double[]> globalBest = particleSwarm.getGlobalBest().getPersonalBest();

                for (int i = 0; i < updatedVelocity.size(); i++) {
                    double[] centerVelocity = new double[dataset.getFeatureSize()];
                    for (int j = 0; j < centerVelocity.length; j++) {
                        centerVelocity[j] = (inertia * currentVelocity.get(i)[j])
                                + (Utilities.randomDouble(1) * personalBest.get(i)[j])
                                + (Utilities.randomDouble(1) * globalBest.get(i)[j]);
                    }
                }

                particle.setPosition(updatedPosition);
                particle.setVelocity(updatedVelocity);
            });

            // Evaluate swarm
            this.particleSwarm.evaluateSwarm(dataset);

            iteration++;
        } while (iteration < maxIterations && notConverged());

        return particleSwarm.getGlobalBest().getPersonalBest(dataset);
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
                    startingPosition[i] = Utilities.randomDouble((int) minValue, (int) maxValue);
                    startingVelocity[i] = Utilities.randomDouble((int) Math.sqrt(maxValue));
                }
                initialClusterCenters.add(startingPosition);
                initialCenterVelocities.add(startingVelocity);
            }

            this.particleSwarm.add(new Particle(initialClusterCenters, initialCenterVelocities));
        }
        this.particleSwarm.evaluateSwarm(dataset);
    }
}
