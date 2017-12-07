package Clusterers;

import Data.*;
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
    private double personalBestQuality = Double.MAX_VALUE;

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
        Clustering clustering = getClustering(this.position, dataset);
        double currentQuality = clustering.evaluateFitness();
        updateBest(currentQuality);
        return currentQuality;
    }

    private void updateBest(double currentQuality) {
        if (currentQuality < this.personalBestQuality) {
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
        return this.getClustering(this.personalBest, dataset);
    }

    public List<double[]> getPosition() {
        return position;
    }

    private Clustering getClustering(List<double[]> centers, Dataset dataset) {
        Clustering clustering = new Clustering();
        centers.forEach(center -> clustering.add(new Cluster(center)));

        dataset.forEach(dataPoint -> {
            double nearestDistance = Double.MAX_VALUE;
            int nearestIndex = 0;
            for (int i = 0; i < clustering.size(); i++) {
                double currentDistance = Utilities.computeDistance(dataPoint.features, centers.get(i));
                if (currentDistance < nearestDistance) {
                    nearestIndex = i;
                    nearestDistance = currentDistance;
                }
            }
            clustering.get(nearestIndex).add(dataPoint);
        });

        return clustering;
    }
}
