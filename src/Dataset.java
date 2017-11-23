import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Dataset extends ArrayList<Sample> {

    public void shuffle() {
        Collections.shuffle(this);
    }

    public void sortByCluster() {
        this.sort(Comparator.comparingInt(Sample::getCluster));
    }
}
