public class ClustererFactory {

    private static final int numParticles = 100;
    private static final int minPoints = 8;
    private static final double epsilon = 10.0;

    public static IDataClusterer buildClusterer(ClustererType type) {
        switch (type) {
            case ACOClusterer:
                return new ACOClusterer();
            case PSOClusterer:
                return new PSOClusterer(numParticles);
            case CompetitiveNetwork:
                return new CompetitiveLearning();
            case DBScan:
                return new DBScan(minPoints, epsilon);
            case kMeans:
                return new KMeans();
            default:
                throw new IllegalArgumentException("Invalid clusterer type!");
        }
    }
}
