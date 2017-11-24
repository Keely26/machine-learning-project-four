import java.util.List;

public class PSOClusterer extends ClustererBase {

    private final int numParticles;

    public PSOClusterer(int numParticles) {
        this.numParticles = numParticles;
    }

    @Override
    public List<Cluster> cluster(Dataset dataset) {
        return null;
    }
}
