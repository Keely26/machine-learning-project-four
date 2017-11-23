import java.util.List;

public class Sample {

    public final double[] features;
    public int cluster;
    public List<Double> distances;

    public Sample(double[] features) {
        this.features = features;
    }
}
