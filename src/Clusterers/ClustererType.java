package Clusterers;

public enum ClustererType {
    ACOClusterer("Ant Colony Optimization"),
    PSOClusterer("Particle Swarm Optimization"),
    CompetitiveNetwork("Competitive Learning Network"),
    DBSCAN("DBSCAN Clustering"),
    kMeans("k-Means Clustering");

    private String algorithmName;

    ClustererType(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    @Override
    public String toString() {
        return algorithmName;
    }
}
