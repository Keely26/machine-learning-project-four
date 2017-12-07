package Clusterers.CompetitiveLearning;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

import java.util.*;

public class CompetitiveLearning implements IDataClusterer {

    private final double learningRate;
    private List<Neuron> inputs;
    private List<Neuron> outputs;
    private Cluster[] clusters;

    public CompetitiveLearning(int inputs, int outputs, double learningRate) {
        this.learningRate = learningRate;
        initializeNetwork(inputs, outputs);
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        Clustering clustering = new Clustering();
        //enforce that the size of the input layer is equal to the number'
        //of features in the dataset
        if (!inputs.equals(dataset.getFeatureSize())) {
            initializeNetwork(dataset.getFeatureSize(), outputs.size());
        }

        //randomly select cluster centers and create clusters
        Random random = new Random();
        for (int i = 0; i < outputs.size(); i++) {
            int randomIndex = random.nextInt(dataset.size() - i);
            double[] featuresCenter = dataset.get(randomIndex).features;
            clusters[i] = new Cluster(dataset, i);
            clusters[i].setClusterCenter(featuresCenter);
            outputs.get(i).setFeatureOrCenter(clusters[i].getClusterCenter());

            Collections.swap(dataset, randomIndex, dataset.size() - i);
        }

        //while not convergence
        int epoch = 0;
        boolean recluster = true;

        while (recluster) {
            int dataLeft = dataset.size() - outputs.size();

            //for each data point, break up into features and assign one feature/input neuron
            //then do the competitive learning stuff
            for (Datum datum : dataset) {
                recluster = false;
                //calculate the distance between each output neuron and the features
                //save the output that has the minimum distance
                double minDist = Utilities.computeDistance(datum.features, outputs.get(0).getFeatureOrCenter());
                int clusterID = 0;

                for (int i = 1; i < outputs.size(); i++) {
                    double currentDist = Utilities.computeDistance(datum.features, outputs.get(i).getFeatureOrCenter());

                    if (currentDist < minDist) {
                        minDist = currentDist;
                        clusterID = i;
                    }
                }
                //move the winning cluster closer to the inputs
                if (outputs.get(clusterID).getFeatureOrCenter() != updateCenter(outputs.get(clusterID))) {
                    outputs.get(clusterID).setFeatureOrCenter(updateCenter(outputs.get(clusterID)));
                    recluster = true;
                }

                //update cluster centers to be equal to the output neuron features
                clusters[clusterID].setClusterCenter(outputs.get(clusterID).getFeatureOrCenter());
            }
            //evaluate clusters
            System.out.println("Epoch: " + epoch + "\t\t");
            clustering.evaluateFitness();
            epoch++;
        }

        return clustering;
    }

    //create network
    private void initializeNetwork(int inputs, int outputs) {
        this.inputs = new ArrayList<>(inputs);
        this.outputs = new ArrayList<>(outputs);

        for (int i = 0; i < inputs; i++) {
            this.inputs.add(new Neuron(1));
        }

        for (int i = 0; i < outputs; i++) {
            this.outputs.add(new Neuron(inputs));
        }
    }

    //center update rule
    private double[] updateCenter(Neuron center) {
        double[] newCenter = center.getFeatureOrCenter();
        for (int c = 0; c < inputs.size(); c++) {
            newCenter[c] = learningRate * (inputs.get(c).getFeatureOrCenter()[0] - newCenter[c]);
        }
        return newCenter;
    }

    @Override
    public String toString() {
        return "Competitive Learning Network";
    }
}
