package Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Dataset extends ArrayList<Datum> {

    public Dataset() {

    }

    public Dataset(List<Datum> data) {
        super(data);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public void sortByCluster() {
        this.sort(Comparator.comparingInt(Datum::getCluster));
    }
}
