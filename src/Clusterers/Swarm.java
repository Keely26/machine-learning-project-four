package Clusterers;

import Data.Clustering;

import java.util.ArrayList;

public class Swarm extends ArrayList<Particle> {

    private Clustering globalBest;

    public Swarm(int size) {
        super(size);
    }

    public Clustering getGlobalBest() {
        return this.globalBest;
    }
}
