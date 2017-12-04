package Clusterers;

import Data.*;

import java.util.List;

public class Particle {

    private List<double[]> position;
    private List<double[]> personalBest;
    private double personalBestQuality;

    private List<double[]> velocity;

    public Particle() {
    }

    public Particle(List<double[]> position, List<double[]> velocity) {
        this.position = position;
        this.personalBest = position;
        this.velocity = velocity;
    }

    public double evaluate(Dataset dataset) {
        Clustering clustering = new Clustering();
        this.position.forEach(center -> clustering.add(new Cluster(center)));
        // Build clustering
        this.assignClusters(dataset, clustering);

        double currentQuality = clustering.evaluateClusters();
        if (currentQuality > this.personalBestQuality) {
            this.personalBest = this.position;
            this.personalBestQuality = currentQuality;
        }

        return currentQuality;
    }

    public List<double[]> getPosition() {
        return position;
    }

    public void setPosition(List<double[]> position) {
        this.position = position;
    }

    public List<double[]> getVelocity() {
        return velocity;
    }

    public void setVelocity(List<double[]> velocity) {
        this.velocity = velocity;
    }

    public List<double[]> getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(List<double[]> bestPosition) {
        this.personalBest = bestPosition;
    }

    public Clustering getPersonalBest(Dataset dataset) {
        Clustering clustering = new Clustering();
        this.personalBest.forEach(center -> clustering.add(new Cluster(center)));
        return this.assignClusters(dataset, clustering);
    }

    // Todo: Fix cluster assignment
    private Clustering assignClusters(Dataset dataset, Clustering clustering) {
        // For each element in the dataset, add it to the nearest cluster
        for (Datum datum : dataset) {
            Cluster nearestCluster = clustering.get(0);
            double clusterDistance = Double.MAX_VALUE;
            for (Cluster cluster : clustering) {
                double distance = datum.computeDistance(cluster.getClusterCenter());
                if (distance < clusterDistance) {
                    nearestCluster = cluster;
                    clusterDistance = distance;
                }
            }
            // Assign dataset.get(i) to nearestCluster
            nearestCluster.add(datum);
        }
        return clustering;
//        for (int i = 0; i < dataset.size(); i++) {
//            Datum datum = dataset.get(i);
//            Cluster nearestCluster = clustering.get(0);
//            double clusterDistance = Double.MAX_VALUE;
//            for (Cluster cluster : clustering) {
//                double distance = datum.computeDistance(cluster.getClusterCenter());
//                if (distance < clusterDistance) {
//                    nearestCluster = cluster;
//                    clusterDistance = distance;
//                }
//            }
//            nearestCluster.add(datum);
//            if (!assignedClustering.contains(nearestCluster)) {
//
//            }
//        }
    }
}
