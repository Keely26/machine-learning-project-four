import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DBScan implements IDataClusterer {

    private final double epsilon;
    private int minPoints;
    private int clusterIndex;

    DBScan(int minPoints, double maxDistance) {
        this.minPoints = minPoints;
        this.epsilon = maxDistance;
        this.clusterIndex = 1;
    }

    @Override
    public List<Cluster> cluster(Dataset dataset) {
        List<Cluster> clusters = new ArrayList<>();

        for (Sample sample : dataset) {
            if (sample.getCluster() == 0) {                     // Check the point hasn't been clustered
                List<Sample> neighbours = getNeighbors(sample, dataset);
                if (neighbours.size() >= minPoints) {
                    for (int j = 0; j < neighbours.size(); j++) {
                        Sample neighbor = neighbours.get(j);

                        if (neighbor.getCluster() == 0) {       // Check the point hasn't been clustered
                            List<Sample> individualNeighbours = getNeighbors(neighbor, dataset);
                            if (individualNeighbours.size() >= minPoints) {
                                neighbours = mergeClusters(neighbours, individualNeighbours);
                            }
                        }
                    }
                    // Set cluster
                    neighbours.forEach(element -> element.setCluster(clusterIndex));
                    clusters.add(new Cluster(neighbours, clusterIndex++));
                }
            }
        }
        return clusters;
    }

    /**
     * Collect the list of data points within epsilon of the target point
     */
    private List<Sample> getNeighbors(Sample sample, Dataset dataset) {
        return dataset.parallelStream()
                .filter(datum -> sample.computeDistance(datum) < this.epsilon)
                .collect(Collectors.toList());
    }

    private List<Sample> mergeClusters(List<Sample> listOne, List<Sample> listTwo) {
        List<Sample> distinct = new ArrayList<>(listOne);

        listTwo.parallelStream()
                .filter(element -> !distinct.contains(element))
                .forEach(distinct::add);

        return distinct;
    }
}