import java.util.List;

public class PSOClusterer implements IDataClusterer {

    private final int numParticles;

    public PSOClusterer(int numParticles) {
        this.numParticles = numParticles;
    }

    @Override
    public List<Cluster> cluster(Dataset dataset) {
        return null;
    }
}
