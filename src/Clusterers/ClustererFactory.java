package Clusterers;

import Clusterers.AntColonyOptimization.ACOClusterer;
import Clusterers.CompetitiveLearning.CompetitiveLearning;
import Clusterers.DBSCAN.DBSCAN;
import Clusterers.KMeans.KMeans;
import Clusterers.ParticleSwarmOptimization.PSOClusterer;

/**
 * Factory handles creation of Clusterer instances and centralizes tunable parameters to allow for simplified
 * modification of the parameters each algorithm uses. Uses the ClustererType enum to build an IDataClusterer of
 * the requested type using the parameters stored here.
 */
public class ClustererFactory {

    /* Universal Parameters */
    private static final int numClusters = 3;

    /* ACO Parameters */
    private static final int numAnts = 200;
    private static final int gridSize = 100;
    private static final double k1 = 3;
    private static final double k2 = 1;
    private static final double radius = 9500.0;

    /* PSO Parameters */
    private static final int numParticles = 15;
    private static final int maxIterations = 500;
    private static final double inertia = 0.3;
    private static final double cognitiveWeight = 1.0;
    private static final double socialWeight = 1.0;

    /* Competitive Network Parameters */
    private static final double learningRate = 0.001;

    /* DBSCAN Parameters */
    private static final int minPoints = 8;
    private static final double epsilon = 1;

    public static IDataClusterer buildClusterer(ClustererType type) {
        switch (type) {
            case ACOClusterer:
                return new ACOClusterer(numAnts, gridSize, k1, k2, radius, numClusters);
            case PSOClusterer:
                return new PSOClusterer(numClusters, numParticles, maxIterations, inertia, cognitiveWeight, socialWeight);
            case CompetitiveNetwork:
                return new CompetitiveLearning(numClusters, learningRate);
            case DBSCAN:
                return new DBSCAN(minPoints, epsilon);
            case kMeans:
                return new KMeans(numClusters);
            default:
                throw new IllegalArgumentException("Invalid clusterer type!");
        }
    }
}
