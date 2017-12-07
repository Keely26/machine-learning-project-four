package Clusterers.ParticleSwarmOptimization;

import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Particle {

    private final double inertia;
    private final double cognitiveWeight;
    private final double socialWeight;
    private List<double[]> position;
    private List<double[]> personalBest;
    private List<double[]> velocities;
    private double personalBestQuality = Double.MIN_VALUE;

    public Particle(List<double[]> position, List<double[]> velocities,
                    double inertia, double cognitiveWeight, double socialWeight) {
        this.position = position;
        this.personalBest = position;
        this.velocities = velocities;
        this.inertia = inertia;
        this.cognitiveWeight = cognitiveWeight;
        this.socialWeight = socialWeight;
    }

    public double evaluate(Dataset dataset) {
        Clustering clustering = Utilities.assignPointsToClusters(dataset, this.position);
        double currentQuality = clustering.evaluateFitness();
        updateBest(currentQuality);
        return currentQuality;
    }

    private void updateBest(double currentQuality) {
        if (currentQuality > this.personalBestQuality) {
            this.personalBest = this.position;
            this.personalBestQuality = currentQuality;
        }
    }

    public void updatePosition() {
        for (int i = 0; i < this.position.size(); i++) {
            for (int j = 0; j < this.position.get(i).length; j++) {
                this.position.get(i)[j] += this.velocities.get(i)[j];
            }
        }
    }

    public void updateVelocity(List<double[]> globalBest) {
        for (int i = 0; i < this.velocities.size(); i++) {
            for (int j = 0; j < this.velocities.get(i).length; j++) {
                this.velocities.get(i)[j] = (this.inertia * this.velocities.get(i)[j])
                        + this.cognitiveWeight * Utilities.randomDouble(1) * (this.position.get(i)[j] - this.personalBest.get(i)[j])
                        + this.socialWeight * Utilities.randomDouble(1) * (this.position.get(i)[j] - globalBest.get(i)[j]);
            }
        }
    }

    public Clustering getBestClustering(Dataset dataset) {
        return Utilities.assignPointsToClusters(dataset, this.personalBest);
    }

    public List<double[]> getPosition() {
        return position;
    }

}
