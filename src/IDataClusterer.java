import java.util.List;

public interface IDataClusterer {
    List<Cluster> cluster(Dataset dataset);
}
