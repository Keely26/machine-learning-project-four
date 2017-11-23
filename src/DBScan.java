import java.util.ArrayList;
import java.util.List;

public class DBScan implements IDataClusterer {

    private final int minPoints;
    private final double epsilon;

    public DBScan(int minPoints, double epsilon) {
        this.minPoints = minPoints;
        this.epsilon = epsilon;
    }

    @Override
    public void cluster(Dataset dataset) {
        assert dataset != null : "Dataset must be supplied.";

        dataset.stream()
                .filter(sample -> sample.cluster > 0)
                .forEach((Sample sample) -> {
                    List<Sample> neighbors = getNeighbors(sample, dataset);
                    if (neighbors.size() < minPoints) {
                        sample.cluster = -1;
                        return;
                    } else {
                        // Something about seeds
                    }
                });
    }

    /**
     * Collect the list of data points within epsilon of the target point
     */
    private List<Sample> getNeighbors(Sample sample, Dataset dataset) {
        List<Sample> neighbors = new ArrayList<>();

        for (int i = 0; i < sample.distances.size(); i++) {
            if (sample.distances.get(i) < epsilon) {
                neighbors.add(dataset.get(i));
            }
        }

        return neighbors;
    }
}
