package Clusterers;

import Data.Clustering;
import Data.Dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import java.lang.Math;
import Data.*;
/*
* KMeans algorithm
* for baseline comparison clustering
*
*
* @author Karen Stengel
* */


public class KMeans implements IDataClusterer {

    private final int numClusters;
    private ArrayList<ArrayList<Datum>> clusterSet;
    private Cluster[] clusters;


    public KMeans(int numClusters) {
        this.numClusters = numClusters;
        clusters = new Cluster[numClusters];
        clusterSet = new ArrayList<ArrayList<Datum>>();
    }

        @Override
        public Clustering cluster(Dataset dataset) {


            Clustering clustering= new Clustering();

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

            //kmeans algorithm
            while(swap){
                swap = false;

                for(int i = 0; i <dataset.size(); i++){
                    double minDist = distance(clusters[0].getClusterCenter(), dataset.get(i).features);
                    int clusterId = 0;

                    for (int j = 1; j < numClusters; j++){
                        double currentDist = distance(clusters[j].getClusterCenter(), dataset.get(i).features);

                        if( currentDist < minDist ){
                            minDist = currentDist;
                            clusterId = j;

                        }
                    }
                    if(!(clusterSet.get(clusterId).contains(dataset.get(i)))){
                        clusterSet.get(clusterId).add(dataset.get(i));
                        swap = true;
                    }

                }

                averageCluster();
                //evaluate clusters
                System.out.println("Epoch: " + epoch + "\t\t");
                clustering.evaluateClusters();
                epoch++;
            }

            return clustering;
        }


        //calculates the Euclidean distance
    public double distance(double[] center, double[] features) {
        double dist = 0.0;

        for (int i = 0; i < center.length; i++) {
            dist += Math.pow(center[i] - features[i], 2);

        }
        return Math.sqrt(dist);
    }

    // averages each cluster to create the new cluster center
    public void averageCluster() {

        for (int i = 0; i < numClusters; i++) {

            double[] newCenter = new double[clusterSet.get(i).get(0).features.length];

            for(int f = 0; f <clusterSet.get(i).get(0).features.length; f++){
                double sumFeature = 0.0;

                for(int d = 0; d < clusterSet.get(i).size(); d++){
                    sumFeature += clusterSet.get(i).get(d).features[f];
                }

                newCenter[f] = sumFeature / clusterSet.get(i).size();
            }

            clusters[i].setClusterCenter(newCenter);

        }

    }
}
