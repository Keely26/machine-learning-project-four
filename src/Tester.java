import java.util.Arrays;

public class Tester {

    private static final DatasetType dataFile = DatasetType.Glass;

    public static void main(String[] args) {

        Dataset dataset = DatasetBuilder.buildDataSet(dataFile);
        dataset.forEach(datum -> System.out.println(Arrays.toString(datum.features)));
    }
}
