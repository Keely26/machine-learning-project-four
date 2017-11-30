package Clusterers;

import Data.Clustering;

public class Particle {

    private Clustering personalBest;
    private Clustering position;
    private double[] velocity;

    public Particle() {
    }

    public Particle(Clustering position, double[] velocity) {
        this.position = position;
        this.personalBest = position;
        this.velocity = velocity;
    }

    public Clustering getPosition() {
        return position;
    }

    public void setPosition(Clustering position) {
        this.position = position;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public Clustering getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(Clustering bestPosition) {
        this.personalBest = bestPosition;
    }
}
