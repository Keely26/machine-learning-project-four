import Clusterers.*;

public class ClustererFactory {

    /* Universal Parameters */
    private static final int numClusters = 6;

    /* ACO Parameters */
    private static final int numAnts = 50;
    private static final double k1 = 2;
    private static final double k2 = 2;
    private static final double radius = 10.0;
    private static final double gamma = 2.0;

    /* PSO Parameters */
    private static final int numParticles = 30;
    private static final int maxIterations = 200;
    private static final double inertia = 1.0;

    /* Competitive Network Parameters */
    private static final int numInputNeurons = 10;
    private static final double learningRate = 0.2;

    /* DBSCAN Parameters */
    private static final int minPoints = 6;
    private static final double epsilon = 17;


    public static IDataClusterer buildClusterer(ClustererType type) {
        switch (type) {
            case ACOClusterer:
                return new ACOClusterer(numAnts, k1, k2, radius, gamma);
            case PSOClusterer:
                return new PSOClusterer(numClusters, numParticles, maxIterations, inertia);
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
