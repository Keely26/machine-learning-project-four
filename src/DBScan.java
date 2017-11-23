public class DBScan implements IDataClusterer {

    private final int minPoints;
    private final double epsilon;

    public DBScan(int minPoints, double epsilon) {
        this.minPoints = minPoints;
        this.epsilon = epsilon;
    }

    @Override
    public void cluster(Dataset dataset) {
        assert dataset != null : "Dataset must be supplied.";

        dataset.stream()
                .filter(sample -> sample.cluster == 0)
                .forEach((Sample sample) -> {
                    do {
                        System.out.println("foo");
                    } while (true);
                });
    }
}
