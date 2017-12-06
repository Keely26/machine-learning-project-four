package Clusterers;

import Data.*;
import Utilites.Utilities;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Particle {

    private List<double[]> position;
    private List<double[]> personalBest;
    private List<double[]> velocities;
    private double personalBestQuality = Double.NEGATIVE_INFINITY;
    private double inertia;

    public Particle(List<double[]> position, List<double[]> velocities, double inertia) {
        this.position = position;
        this.personalBest = position;
        this.velocities = velocities;
        this.inertia = inertia;
    }

    public double evaluate(Dataset dataset) {
        Clustering clustering = getClustering(this.position, dataset);
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
        for (int i = 0, clusterIndex = this.position.size(); i < clusterIndex; i++) {
            double[] clusterCenter = this.position.get(i);
            double[] updatedCenterPosition = new double[clusterCenter.length];

            for (int j = 0, length = clusterCenter.length; j < length; j++) {
                updatedCenterPosition[j] = clusterCenter[j] + this.velocities.get(i)[j];
            }

            this.position.set(i, updatedCenterPosition);
        }
    }

    public void updateVelocity(List<double[]> globalBest) {
        for (int i = 0; i < this.velocities.size(); i++) {
            for (int j = 0, length = this.velocities.get(0).length; j < length; j++) {
                this.velocities.get(i)[j] = (this.inertia * this.velocities.get(i)[j])
                        + (Utilities.randomDouble(1) * (this.personalBest.get(i)[j] - this.velocities.get(i)[j]))
                        + (Utilities.randomDouble(1) * (globalBest.get(i)[j] - this.velocities.get(i)[j]));
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
