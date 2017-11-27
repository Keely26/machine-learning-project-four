package Clusterers;

public class Neuron {

    private final int numConnections;

    public Neuron(int numConnections) {
        this.numConnections = numConnections;
    }

    public int getNumConnections() {
        return numConnections;
    }
}
