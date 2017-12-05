package Clusterers;

import Data.*;
import Utilites.Utilities;

import java.util.List;

public class Particle {

    private List<double[]> position;
    private List<double[]> personalBest;
    private List<double[]> velocities;
    private double personalBestQuality = Double.NEGATIVE_INFINITY;
    private double inertia;

    public Particle() {
    }

    public Particle(List<double[]> position, List<double[]> velocities, double inertia) {
        this.position = position;
        this.personalBest = position;
        this.velocities = velocities;
        this.inertia = inertia;
    }

    public double evaluate(Dataset dataset) {
        Clustering clustering = new Clustering();
        for (double[] centerVector : this.position) {
            clustering.add(new Cluster(centerVector));
        }

        // Build clustering
        clustering = Utilities.assignClusters(dataset, clustering);

        double currentQuality = clustering.evaluateClusters();
        if (currentQuality > this.personalBestQuality) {
            this.personalBest = this.position;
            this.personalBestQuality = currentQuality;
        }

        return currentQuality;
    }

    public void updatePosition() {
        for (int i = 0, clusterIndex = this.position.size(); i < clusterIndex; i++) {
            double[] clusterCenter = this.position.get(i);
            double[] centerVelocity = this.velocities.get(i);
            double[] updatedCenterPosition = new double[clusterCenter.length];

            for (int j = 0, length = clusterCenter.length; j < length; j++) {
                updatedCenterPosition[j] = clusterCenter[j] + centerVelocity[j];
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

    public Clustering getPersonalBest(Dataset dataset) {
        Clustering clustering = new Clustering();
        this.personalBest.forEach(center -> clustering.add(new Cluster(center)));
        return Utilities.assignClusters(dataset, clustering);
    }

    public List<double[]> getPosition() {
        return position;
    }

    public List<double[]> getVelocities() {
        return velocities;
    }
}
