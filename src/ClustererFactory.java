import Clusterers.*;

public class ClustererFactory {

    /* Universal Parameters */
    private static final int numClusters = 4;

    /* ACO Parameters */
    private static final int numAnts = 50;
    private static final double k1 = 2;
    private static final double k2 = 2;
    private static final double radius = 10.0;

    /* PSO Parameters */
    private static final int numParticles = 100;
    private static final int maxIterations = 10000;

    /* Competitive Network Parameters */
    private static final int numInputNeurons = 10;
    private static final double learningRate = 0.2;

    /* DBSCAN Parameters */
    private static final int minPoints = 8;
    private static final double epsilon = 8;


    public static IDataClusterer buildClusterer(ClustererType type) {
        switch (type) {
            case ACOClusterer:
                return new ACOClusterer(numAnts, k1, k2, radius);
            case PSOClusterer:
                return new PSOClusterer(numClusters, numParticles, maxIterations);
            case CompetitiveNetwork:
                return new CompetitiveLearning(numInputNeurons, numClusters, learningRate);
            case DBSCAN:
                return new DBSCAN(minPoints, epsilon);
            case kMeans:
                return new KMeans(numClusters);
            default:
                throw new IllegalArgumentException("Invalid clusterer type!");
        }
    }
}
