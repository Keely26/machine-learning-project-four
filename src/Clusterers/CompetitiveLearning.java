package Clusterers;

import Data.Clustering;
import Data.Dataset;

import java.util.ArrayList;
import java.util.List;

public class CompetitiveLearning implements IDataClusterer {

    private List<Neuron> inputs;
    private List<Neuron> outputs;

    private final double learningRate;

    public CompetitiveLearning(int inputs, int outputs, double learningRate) {
        this.learningRate = learningRate;
        initializeNetwork(inputs, outputs);
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        return null;
    }

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
}
