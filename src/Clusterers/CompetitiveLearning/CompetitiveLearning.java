package Clusterers.CompetitiveLearning;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

import java.util.*;

/**
 * A single layer competitive neural network, an SOM
 *
 * @author Karen Stengel
 */
public class CompetitiveLearning implements IDataClusterer {

    private final double learningRate;
    private final int numClusters;
    private List<Neuron> neurons;

    public CompetitiveLearning(int numClusters, double learningRate) {
        this.learningRate = learningRate;
        this.numClusters = numClusters;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        // Initialize network
        initializeNetwork(dataset);

        Clustering clustering = null;
        Clustering oldClustering;
        int iteration = 0;
        do {
            oldClustering = clustering;
            // Update the network for every example in the dataset
            dataset.forEach(example -> example.setCluster(updateNetwork(example.features)));

            // Evaluate performance
            clustering = buildClustering(dataset);
            System.out.println("Quality: " + clustering.evaluateFitness());
            iteration++;
        } while (shouldContinue(iteration, clustering, oldClustering));

        return buildClustering(dataset);
    }

    /**
     * Return true until either the current and old clusterings have not updated or the
     * maximum number of iterations has been reached.
     */
    private boolean shouldContinue(int iteration, Clustering clustering, Clustering oldClustering) {
        return !Objects.deepEquals(clustering, oldClustering) && iteration < 1000;
    }

    /**
     * Uses the current centroids of the neurons to assign a clustering for the provided dataset
     */
    private Clustering buildClustering(Dataset dataset) {
        Clustering clustering = new Clustering();
        // Create clusters
        for (int i = 0; i < this.numClusters; i++) {
            clustering.add(new Cluster(i));
        }

        // Assign according to cluster ID
        for (Datum datum : dataset) {
            clustering.get(datum.getCluster()).add(datum);
        }

        return clustering;
    }

    /**
     * Given an example data point, finds the most similar output neuron and adjusts the centroid of that neuron
     */
    private int updateNetwork(double[] example) {
        // Identify best output
        Neuron winner = null;
        double bestOutputDistance = Double.MAX_VALUE;
        for (Neuron neuron : this.neurons) {
            double distance = Utilities.computeDistance(example, neuron.getCentroid());
            // If current neuron's centroid is closer to the example, update the current best
            if (distance < bestOutputDistance) {
                bestOutputDistance = distance;
                winner = neuron;
            }
        }

        assert winner != null : "No winner!";

        // Adjust centroid of the winner
        double[] centroid = winner.getCentroid();
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] += learningRate * (example[i] - centroid[i]);
        }
        winner.setCentroid(centroid);

        // Return the cluster index of the winning neuron
        return winner.getClusterIndex();
    }

    // Build new neurons, set centroids randomly
    private void initializeNetwork(Dataset dataset) {
        List<Integer> indices = new ArrayList<>(dataset.size());
        for (int i = 0; i < dataset.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        this.neurons = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            this.neurons.add(new Neuron(i, dataset.get(indices.remove(0)).features));
        }
    }

    @Override
    public String toString() {
        return "Competitive Learning Network";
    }
}
