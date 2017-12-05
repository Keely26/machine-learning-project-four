package Clusterers;

import Data.*;

import java.util.*;

/**
 * KMeans algorithm
 * for baseline comparison clustering
 *
 * @author Karen Stengel
 */
public class KMeans implements IDataClusterer {

    private final int numClusters;
    private ArrayList<ArrayList<Datum>> clusterSet;
    private Cluster[] clusters;


    public KMeans(int numClusters) {
        this.numClusters = numClusters;
        clusters = new Cluster[numClusters];
        clusterSet = new ArrayList<>();
    }

    @Override
    public Clustering cluster(Dataset dataset) {
        Clustering clustering = new Clustering();

        //randomly select numCluster cluster centers
        //create new cluster with them; set clusterID to be equal to the index in clusters
        //swaps chosen indices to end and then excludes them
        Random random = new Random();
        for (int i = 0; i < numClusters; i++) {
            int randomIndex = random.nextInt(dataset.size() - i);
            double[] featuresCenter = dataset.get(randomIndex).features;
            clusters[i] = new Cluster(dataset, i);
            clusters[i].setClusterCenter(featuresCenter);
            Collections.swap(dataset, randomIndex, dataset.size() - i);
        }

        boolean swap = true;
        int epoch = 0;

        // kMeans algorithm
        while (swap) {
            swap = false;

            for (Datum datum : dataset) {
                double minDist = datum.computeDistance(clusters[0].getClusterCenter());
                int clusterId = 0;

                for (int j = 1; j < numClusters; j++) {
                    double currentDist = datum.computeDistance(clusters[j].getClusterCenter());

                    if (currentDist < minDist) {
                        minDist = currentDist;
                        clusterId = j;
                    }
                }
                if (!(clusterSet.get(clusterId).contains(datum))) {
                    clusterSet.get(clusterId).add(datum);
                    swap = true;
                }

            }

            averageCluster();
            //evaluate clusters
            System.out.println("Epoch: " + epoch + "\t\t");
            clustering.evaluate();
            epoch++;
        }
        return clustering;
    }

    // averages each cluster to create the new cluster center
    private void averageCluster() {
        for (int i = 0; i < numClusters; i++) {
            double[] newCenter = new double[clusterSet.get(i).get(0).features.length];

            for (int f = 0; f < clusterSet.get(i).get(0).features.length; f++) {
                double sumFeature = 0.0;

                for (int d = 0; d < clusterSet.get(i).size(); d++) {
                    sumFeature += clusterSet.get(i).get(d).features[f];
                }

                newCenter[f] = sumFeature / clusterSet.get(i).size();
            }
            clusters[i].setClusterCenter(newCenter);
        }
    }
}
