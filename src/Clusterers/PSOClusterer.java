package Clusterers;

import Data.Clustering;
import Data.Dataset;

public class PSOClusterer implements IDataClusterer {

    private final int numClusters;
    private final int numParticles;
    private final int maxIterations;

    private Swarm particleSwarm;

    public PSOClusterer(int numClusters, int numParticles, int maxIterations) {
        this.numClusters = numClusters;
        this.numParticles = numParticles;
        this.maxIterations = maxIterations;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        this.initializeParticles(dataset);

        int iteration = 0;
        do {
            // For each particle, update position, evaluate, update velocity, set personal and global bests
            particleSwarm.forEach((Particle particle) -> {

            });

            iteration++;
        } while (iteration < maxIterations && notConverged());

        return particleSwarm.getGlobalBest();
    }

    private boolean notConverged() {
        return true;
    }

    private void initializeParticles(Dataset data) {
        this.particleSwarm = new Swarm(this.numParticles);
        for (int i = 0; i < this.numParticles; i++) {
            // Todo: Randomize starting positions
            Clustering initialClustering = new Clustering();
            double[] initialVelocity = new double[data.getFeatureSize()];

            this.particleSwarm.add(new Particle(initialClustering, initialVelocity));
        }
    }
}
