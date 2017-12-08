package Data;

import java.util.*;

public class Dataset extends ArrayList<Datum> {

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
}

