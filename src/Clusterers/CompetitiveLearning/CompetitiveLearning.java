package Clusterers.CompetitiveLearning;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

import java.util.*;

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

        int iteration = 0;
        do {
            dataset.forEach(example -> example.setCluster(updateNetwork(example.features)));
            System.out.println("Quality: " + buildClustering(dataset).evaluateFitness());
            iteration++;
        } while (shouldContinue(iteration));

        return buildClustering(dataset);
    }

    // Todo: convergence detection
    private boolean shouldContinue(int iteration) {
        return iteration < 1000;
    }

    private Clustering buildClustering(Dataset dataset) {
        Clustering clustering = new Clustering();
        for (int i = 0; i < this.numClusters; i++) {
            clustering.add(new Cluster(i));
        }

        for (Datum datum : dataset) {
            clustering.get(datum.getCluster()).add(datum);
        }
        return clustering;
    }

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
