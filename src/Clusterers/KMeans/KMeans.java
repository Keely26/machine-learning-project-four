package Clusterers.KMeans;

import Clusterers.IDataClusterer;
import Data.*;
import Utilites.Utilities;

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
        // Randomly select numCluster cluster centers
        // Create new cluster with them; set clusterID to be equal to the index in clusters
        // Swaps chosen indices to end and then excludes them
        Random random = new Random();
        for (int i = 0; i < numClusters; i++) {

            clusterSet.add(new ArrayList<>());
            int randomIndex = random.nextInt(dataset.size() - i);
            double[] featuresCenter = dataset.get(randomIndex).features;
            clusters[i] = new Cluster(dataset, i);
            clusters[i].setClusterCenter(featuresCenter);
            clusterSet.get(i).add(dataset.get(randomIndex));
            Collections.swap(dataset, randomIndex, dataset.size() - i - 1);
        }
        //System.out.println(clusterSet.size());
        boolean swap = true;
        int epoch = 0;

        // kMeans algorithm
        while (epoch < 5000) {


            for (Datum datum : dataset) {
                double minDist = Utilities.computeDistance(datum.features, clusters[0].getClusterCenter());
                int clusterId = 0;

                for (int j = 1; j < numClusters; j++) {
                    double currentDist = Utilities.computeDistance(datum.features, clusters[j].getClusterCenter());

                    if (currentDist < minDist) {
                        minDist = currentDist;
                        clusterId = j;
                    }
                }


                if (!(clusterSet.get(clusterId).contains(datum))) {
                    //remove from previous cluster
                    for (int i = 0; i < numClusters; i++) {
                        if (clusterSet.get(i).contains(datum)) {
                            clusterSet.get(i).remove(datum);
                            clusterSet.get(i).trimToSize();
                        }
                    }
                    datum.setCluster(clusterId);

                    clusterSet.get(clusterId).add(datum);
                    //swap = true;
                }

            }

            //System.out.println(clusterSet.size());
            averageCluster();

            //evaluate clusters
            System.out.println("Epoch: " + epoch + "\t\t");
            clustering.evaluateFitness();

            epoch++;
        }
        return clustering;
    }

    // averages each cluster to create the new cluster center
    private void averageCluster() {
        for (int i = 0; i < numClusters; i++) {
            double[] newCenter = new double[clusterSet.get(i).get(0).features.length];

            for (int f = 0; f < clusterSet.get(i).get(0).features.length; f++) { //for each feature
                double sumFeature = 0.0;


                for (int d = 0; d < clusterSet.get(i).size(); d++) {
                    if (clusterSet.get(i).get(d).features.length == 10) {

                        sumFeature += clusterSet.get(i).get(d).features[f];
                    }
                }

                newCenter[f] = sumFeature / clusterSet.get(i).size();

            }
            clusters[i].setClusterCenter(newCenter);
            System.out.println(newCenter[0]);
        }
    }
}
