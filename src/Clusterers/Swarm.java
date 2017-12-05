package Clusterers;

import Data.Dataset;

import java.util.ArrayList;

public class Swarm extends ArrayList<Particle> {

    private Particle globalBest;
    private double globalBestQuality = Double.NEGATIVE_INFINITY;

    public Swarm(int size) {
        super(size);
    }

    public Particle getGlobalBest() {
        return this.globalBest;
    }

    public double evaluateSwarm(Dataset dataset) {
        this.forEach(particle -> {
            double quality = particle.evaluate(dataset);
            if (quality > this.globalBestQuality) {
                this.globalBest = particle;
                this.globalBestQuality = quality;
            }
        });
        return this.globalBestQuality;
    }
}
