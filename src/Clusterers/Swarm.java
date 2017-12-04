package Clusterers;

import Data.Dataset;

import java.util.ArrayList;
import java.util.List;

public class Swarm extends ArrayList<Particle> {

    private List<double[]> globalBest;
    private double globalBestQuality;

    public Swarm(int size) {
        super(size);
    }

    public List<double[]> getGlobalBest() {
        return this.globalBest;
    }

    public double evaluateSwarm(Dataset dataset) {
        this.forEach(particle -> {
            double quality = particle.evaluate(dataset);
            if (quality > this.globalBestQuality) {
                this.globalBest = particle.getPosition();
                this.globalBestQuality = quality;
            }
        });
        return this.globalBestQuality;
    }
}
