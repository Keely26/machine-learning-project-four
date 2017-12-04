package Data;

import java.util.*;

public class Dataset extends ArrayList<Datum> {

    private HashMap<Integer, Double> distances = new HashMap<>();

    public Dataset() {

    }

    public Dataset(List<Datum> data) {
        super(data);
    }

    public int getFeatureSize() {
        return this.get(0).features.length;
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public void sortByCluster() {
        this.sort(Comparator.comparingInt(Datum::getCluster));
    }

    public double getDistance(Datum point1, Datum point2) {
        if (Arrays.equals(point1.features, point2.features)) {
            return 0.0;
        }
        return getDistance(point1.features, point2.features);
    }

    private double getDistance(double[] vector1, double[] vector2) {
        assert vector1.length == vector2.length : "Non-congruent feature vector sized!";
        Integer key = Arrays.hashCode(vector1) + Arrays.hashCode(vector2);
        double tableDistance = this.distances.getOrDefault(key, -1.0);
        if (tableDistance > 0) {
            return tableDistance;
        } else {
            double sum = 0.0;
            for (int i = 0; i < vector1.length; i++) {
                sum += Math.pow(vector1[i] - vector2[i], 2);
            }
            return sum / vector1.length;
        }
    }

    // Tabulate the distances between any two points in the dataset.
    // Expensive! O(n^2), should only be performed once per dataset!
    public void computeDistances() {
        this.parallelStream().forEach(point1 -> this.forEach(point2 -> {
            // Todo: Hash not guaranteed to be unique
            Integer key = Arrays.hashCode(point1.features) + Arrays.hashCode(point2.features);
            double distance = point1.computeDistance(point2.features);
            this.distances.putIfAbsent(key, distance);
        }));
    }
}
