package Clusterers.ParticleSwarmOptimization;

import Data.Dataset;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class Swarm extends ArrayList<Particle> {

    private Particle globalBest;
    private double globalBestQuality = Double.MIN_VALUE;

    public Swarm(int size) {
        super(size);
    }

    public Particle getGlobalBest() {
        return this.globalBest;
    }

    public void evaluateSwarm(Dataset dataset) {
        this.forEach(particle -> {
            double quality = particle.evaluate(dataset);
            if (quality > this.globalBestQuality) {
                this.globalBest = particle;
                this.globalBestQuality = quality;
            }
        });
    }
}