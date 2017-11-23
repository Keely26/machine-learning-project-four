import java.util.ArrayList;
import java.util.List;

public interface IDataClusterer {
    void cluster(Dataset dataset);

    default void computeDistances(Dataset dataset) {
        for (int i = 0; i < dataset.size(); i++) {
            List<Double> distances = new ArrayList<>(dataset.size());
            for (int j = 0; j < dataset.size(); j++) {
                if (i == j) {
                    distances.add(0.0);
                } else {
                    // Sum distance over each of the features
                    double sum = 0.0;
                    for (int k = 0; k < dataset.get(i).features.length; k++) {
                        sum += Math.pow(dataset.get(i).features[k] - dataset.get(j).features[k], 2);
                    }
                    distances.add(sum / dataset.get(i).features.length);
                }
            }
            dataset.get(i).distances = distances;
        }
    }
}
