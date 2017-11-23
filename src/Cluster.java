import java.util.ArrayList;
import java.util.List;

public class Cluster extends ArrayList<Sample> {
    final int clusterId;

    public Cluster(List<Sample> elements, int clusterId) {
        super(elements);
        this.clusterId = clusterId;
    }
}
