public class Particle {

    private double[] position;
    private double[] velocity;

    private double[] bestPosition;

    public Particle() {}

    public Particle(double[] position, double[] velocity) {
        this.position = position;
        this.bestPosition = position;
        this.velocity = velocity;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double[] getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(double[] bestPosition) {
        this.bestPosition = bestPosition;
    }
}
