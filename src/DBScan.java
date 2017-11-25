import java.util.List;
import java.util.stream.Collectors;

public class DBScan extends ClustererBase {

    private final double epsilon;
    private int minPoints;
    private int clusterIndex;

    DBScan(int minPoints, double maxDistance) {
        this.minPoints = minPoints;
        this.epsilon = maxDistance;
        this.clusterIndex = 1;
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        Clustering clustering = new Clustering();

        for (Datum sample : dataset) {
            if (sample.getCluster() == 0) {                     // Check the point hasn't been clustered
                Dataset neighbours = getNeighbors(sample, dataset);
                if (neighbours.size() >= minPoints) {
                    for (int j = 0; j < neighbours.size(); j++) {
                        Datum neighbor = neighbours.get(j);

                        if (neighbor.getCluster() == 0) {       // Check the point hasn't been clustered
                            List<Datum> individualNeighbours = getNeighbors(neighbor, dataset);
                            if (individualNeighbours.size() >= minPoints) {
                                neighbours = mergeClusters(neighbours, individualNeighbours);
                            }
                        }
                    }
                    // Set cluster
                    neighbours.forEach(element -> element.setCluster(clusterIndex));
                    clustering.add(new Cluster(neighbours, clusterIndex++));
                }
            }
        }
        System.out.println("DBSCAN");
        evaluateClusters(clustering);
        return clustering;
    }

    /**
     * Collect the list of data points within epsilon of the target point
     */
    private Dataset getNeighbors(Datum sample, Dataset dataset) {
        return dataset.parallelStream()
                .filter(datum -> sample.computeDistance(datum) < this.epsilon)
                .collect(Collectors.toCollection(Dataset::new));
    }

    private Dataset mergeClusters(List<Datum> listOne, List<Datum> listTwo) {
        Dataset distinct = new Dataset(listOne);

        listTwo.parallelStream()
                .filter(element -> !distinct.contains(element))
                .forEach(distinct::add);

        return distinct;
    }
}