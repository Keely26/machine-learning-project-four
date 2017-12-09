package Data;

import java.util.*;

/**
 * Wrapper for an ArrayList of Datum, allows for specific overridden operations on the datasets
 */
public class Dataset extends ArrayList<Datum> {

    private String name;

    public Dataset() {}

    public Dataset(List<Datum> data) {
        super(data);
    }

    // Return the length of the data vectors in the dataset
    public int getFeatureSize() {
        return this.get(0).features.length;
    }

    // Order elements by cluster ID descending
    public void sortByCluster() {
        this.sort(Comparator.comparingInt(Datum::getCluster));
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

