package Clusterers.ParticleSwarmOptimization;

import Data.Clustering;
import Data.Dataset;
import Utilites.Utilities;

import java.util.List;

/**
 * PSO Particle maintains a position, velocity, and a personal best position.
 * Also handles the logic for updating positions and velocities each iteration of the algorithm.
 */
public class Particle {

    private final double inertia;
    private final double cognitiveWeight;
    private final double socialWeight;
    private List<double[]> position;
    private List<double[]> personalBest;
    private List<double[]> velocities;
    private double personalBestQuality = Double.MIN_VALUE;

    Particle(List<double[]> position, List<double[]> velocities,
             double inertia, double cognitiveWeight, double socialWeight) {
        this.position = position;
        this.personalBest = position;
        this.velocities = velocities;
        this.inertia = inertia;
        this.cognitiveWeight = cognitiveWeight;
        this.socialWeight = socialWeight;
    }

    /**
     * Evaluates the fitness of the particles current location on the provided dataset.
     */
    public double evaluate(Dataset dataset) {
        Clustering clustering = Utilities.assignPointsToClusters(dataset, this.position);
        double currentQuality = clustering.evaluateFitness();
        updateBest(currentQuality);
        return currentQuality;
    }

    /**
     * Given a fitness evaluation, determines if is better than the current personal best,
     * if so records the value as the new personal best.
     */
    private void updateBest(double currentQuality) {
        if (currentQuality > this.personalBestQuality) {
            this.personalBest = this.position;
            this.personalBestQuality = currentQuality;
        }
    }

    /**
     * Updates the particles position by adding the current velocity to the current position.
     */
    public void updatePosition() {
        for (int i = 0; i < this.position.size(); i++) {
            for (int j = 0; j < this.position.get(i).length; j++) {
                this.position.get(i)[j] += this.velocities.get(i)[j];
            }
        }
    }

    /**
     * Updates the current velocity according to the velocity update equation.
     * v(t + 1) = inertia * v(t) + w1 * U(0,1) * delta(personBest - current) + w2 * U(0,1) * delta(globalBest - current)
     * Uses the weights provided during instance construction as well as the supplied vector for the global best position
     */
    public void updateVelocity(List<double[]> globalBest) {
        for (int i = 0; i < this.velocities.size(); i++) {
            for (int j = 0; j < this.velocities.get(i).length; j++) {
                this.velocities.get(i)[j] = (this.inertia * this.velocities.get(i)[j])
                        + this.cognitiveWeight * Utilities.randomDouble(1) * (this.position.get(i)[j] - this.personalBest.get(i)[j])
                        + this.socialWeight * Utilities.randomDouble(1) * (this.position.get(i)[j] - globalBest.get(i)[j]);
            }
        }
    }

    public List<double[]> getPosition() {
        return position;
    }
}
