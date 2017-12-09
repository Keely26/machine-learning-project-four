package Clusterers.ParticleSwarmOptimization;

import Data.Dataset;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a collection of particles for PSO as well as the global best clustering and its fitness
 */
public class Swarm extends ArrayList<Particle> {

    private List<double[]> globalBest;
    private double globalBestQuality = Double.MIN_VALUE;

    Swarm(int size) {
        super(size);
    }

    public List<double[]> getGlobalBest() {
        return this.globalBest;
    }

    /**
     * Evaluate the clustering of each particle in the swarm, if any have a better fitness than the current
     * best fitness, update the current best
     */
    public void evaluateSwarm(Dataset dataset) {
        this.forEach(particle -> {
            double quality = particle.evaluate(dataset);
            if (quality > this.globalBestQuality) {
                this.globalBest = particle.getPosition();
                this.globalBestQuality = quality;
            }
        });
    }
}
