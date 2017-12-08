package Clusterers;

import Clusterers.AntColonyOptimization.ACOClusterer;
import Clusterers.CompetitiveLearning.CompetitiveLearning;
import Clusterers.DBSCAN.DBSCAN;
import Clusterers.KMeans.KMeans;
import Clusterers.ParticleSwarmOptimization.PSOClusterer;

public class ClustererFactory {

    /* Universal Parameters */
    private static final int numClusters = 7;

    /* ACO Parameters */
    private static final int numAnts = 100;
    private static final double k1 = 2;
    private static final double k2 = 2;
    private static final double radius = 10.0;
    private static final double gamma = 2.0;

    /* PSO Parameters */
    private static final int numParticles = 1000;
    private static final int maxIterations = 100;
    private static final double inertia = 0.72;
    private static final double cognitiveWeight = 1.49;
    private static final double socialWeight = 1.49;

    /* Competitive Network Parameters */
    private static final int numInputNeurons = 10;
    private static final double learningRate = 0.2;

    /* DBSCAN Parameters */
    private static final int minPoints = 407;
    private static final double epsilon = 1.5;

    public static IDataClusterer buildClusterer(ClustererType type) {
        switch (type) {
            case ACOClusterer:
                return new ACOClusterer(numAnts, k1, k2, radius, numClusters);
            case PSOClusterer:
                return new PSOClusterer(numClusters, numParticles, maxIterations, inertia, cognitiveWeight, socialWeight);
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
