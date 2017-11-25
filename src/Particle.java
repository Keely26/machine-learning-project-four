public class Particle {

    private Clustering bestPosition;
    private Clustering position;
    private double[] velocity;

    public Particle() {
    }

    public Particle(Clustering position, double[] velocity) {
        this.position = position;
        this.bestPosition = position;
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

    public Clustering getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(Clustering bestPosition) {
        this.bestPosition = bestPosition;
    }
}
