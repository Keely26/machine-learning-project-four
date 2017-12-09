package Clusterers.ParticleSwarmOptimization;

import Clusterers.IDataClusterer;
import Clusterers.KMeans.KMeans;
import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.*;

/**
 * Implementation of the Particle Swarm Optimization algorithm for data clustering
 *
 * @author Zach Connelly
 */
public class PSOClusterer implements IDataClusterer {

    private final int numClusters;
    private final int numParticles;
    private final int maxIterations;
    private final double inertia;
    private final double cognitiveWeight;
    private final double socialWeight;

    private Swarm particleSwarm;

    public PSOClusterer(int numClusters, int numParticles, int maxIterations, double inertia,
                        double cognitiveWeight, double socialWeight) {
        this.numClusters = numClusters;
        this.numParticles = numParticles;
        this.maxIterations = maxIterations;
        this.inertia = inertia;
        this.cognitiveWeight = cognitiveWeight;
        this.socialWeight = socialWeight;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        this.initializeParticles(dataset);

        Clustering clustering = null;
        Clustering oldClustering;
        int iteration = 0;
        do {
            // Update old clustering, logging
            oldClustering = clustering;
            logIteration(iteration, dataset);

            // For each particle, update position, evaluate, update velocity, set personal and global bests
            particleSwarm.parallelStream().forEach(particle -> {
                particle.updatePosition();
                particle.updateVelocity(particleSwarm.getGlobalBest());
            });

            // Evaluate
            clustering = Utilities.assignPointsToClusters(dataset, particleSwarm.getGlobalBest());
            System.out.println("Quality: " + clustering.evaluateFitness());
            iteration++;
        } while (notConverged(iteration, clustering, oldClustering));

        // Retrieve best clustering
        return Utilities.assignPointsToClusters(dataset, particleSwarm.getGlobalBest());
    }

    /**
     * Return true until either the current and old clusterings have not updated or the
     * maximum number of iterations has been reached.
     */
    private boolean notConverged(int iteration, Clustering clustering, Clustering oldClustering) {
        return !Objects.deepEquals(clustering, oldClustering) && iteration < maxIterations;
    }

    /**
     * Set up the swarm with random starting locations and velocities
     */
    private void initializeParticles(Dataset dataset) {
        this.particleSwarm = new Swarm(this.numParticles);

        double[] maxValues = getMaxFeatureVector(dataset);
        double[] minValues = getMinFeatureVector(dataset);

        // Loop over number of particles
        for (int i = 0; i < this.numParticles; i++) {
            List<double[]> initialCenterPositions = new ArrayList<>();
            List<double[]> initialCenterVelocities = new ArrayList<>();

            // Use k Means to pick starting centers
            KMeans kMeans = new KMeans(this.numClusters);
            Clustering clustering = kMeans.cluster(dataset);
            for (int j = 0; j < this.numClusters; j++) {
                double[] startingPosition = clustering.get(j).getCentroid();
                double[] startingVelocity = new double[dataset.getFeatureSize()];

                // Set initial velocities to values between the min and max values
                for (int k = 0; k < startingPosition.length; k++) {
                    startingVelocity[k] = Utilities.randomDouble(Math.sqrt(minValues[k]), Math.sqrt(maxValues[k]));
                }
                initialCenterPositions.add(startingPosition);
                initialCenterVelocities.add(startingVelocity);
            }

            // Create new particle
            this.particleSwarm.add(new Particle(initialCenterPositions, initialCenterVelocities, this.inertia,
                    this.cognitiveWeight, this.socialWeight));
        }

        // Perform an initial evaluation to find personal and global bests
        this.particleSwarm.evaluateSwarm(dataset);
    }

    /**
     * Find the minimum value in the dataset for each feature
     */
    private double[] getMinFeatureVector(Dataset dataset) {
        double[] minValues = new double[dataset.getFeatureSize()];

        // Initialize each element to max value
        for (int i = 0; i < dataset.getFeatureSize(); i++) {
            minValues[i] = Double.MAX_VALUE;
        }

        // Find min value for each feature
        dataset.forEach(datum -> {
            for (int i = 0; i < dataset.getFeatureSize(); i++) {
                if (datum.features[i] < minValues[i]) {
                    minValues[i] = datum.features[i];
                }
            }
        });

        return minValues;
    }

    /**
     * Find the maximum value in the dataset for each feature
     */
    private double[] getMaxFeatureVector(Dataset dataset) {
        double[] maxValues = new double[dataset.getFeatureSize()];

        // Initialize each element to min value
        for (int i = 0; i < dataset.getFeatureSize(); i++) {
            maxValues[i] = Double.MIN_VALUE;
        }

        // Find max value for each feature
        dataset.forEach(datum -> {
            for (int i = 0; i < dataset.getFeatureSize(); i++) {
                if (datum.features[i] > maxValues[i]) {
                    maxValues[i] = datum.features[i];
                }
            }
        });

        return maxValues;
    }

    /**
     * Perform logging for each iteration
     */
    private void logIteration(int iteration, Dataset dataset) {
        Clustering currentBest = Utilities.assignPointsToClusters(dataset, this.particleSwarm.getGlobalBest());
        System.out.print("Iteration: " + iteration);
        System.out.println(", best clustering:");
        System.out.println(currentBest.toString());
        System.out.println("Quality: " + currentBest.evaluateFitness());
        System.out.println("Inter Distance: " + currentBest.evaluateInterClusterDistance());
        System.out.println("Intra Distance: " + currentBest.evaluateIntraClusterDistance());
        System.out.println();
    }

    @Override
    public String toString() {
        return "Particle Swarm Optimization";
    }
}
