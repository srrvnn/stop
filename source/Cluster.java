// package imageresearch;

import java.io.*;
import weka.core.*;
import weka.classifiers.*;
import weka.clusterers.SimpleKMeans;
import weka.core.converters.ArffLoader;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Cluster
{

	String file_source; 
	String[] file_clusters;

	Cluster()
	{}

	public void SetSource(String s)
	{
		file_source = s; 
	}

	public void SetClusters(String[] s)
	{
		file_clusters = s; 
	}

	public void Run() throws FileNotFoundException, IOException, Exception
	{

		BufferedReader cr = new BufferedReader(new FileReader("data/"+file_source));
       	Instances cd = new Instances(cr);

       	String[] options = new String[2];
       	options[0] = "-R";
       	options[1] = "4";

       	Remove remove = new Remove();
       	remove.setOptions(options);
       	remove.setInputFormat(cd);

       	Instances nd = Filter.useFilter(cd,remove);

       	SimpleKMeans kmeans = new SimpleKMeans(); 

       	kmeans.setPreserveInstancesOrder(true);
       	kmeans.setNumClusters(3);
       	kmeans.setMaxIterations(1500);
       	kmeans.buildClusterer(nd);

       	String cc1 = kmeans.getClusterCentroids().instance(0).toString();  
       	String cc2 = kmeans.getClusterCentroids().instance(1).toString();  
       	String cc3 = kmeans.getClusterCentroids().instance(2).toString();

       	BufferedWriter cw0 = new BufferedWriter(new FileWriter("data/"+file_clusters[0]));

       	cw0.write(cc1); cw0.newLine();
       	cw0.write(cc2); cw0.newLine();
       	cw0.write(cc3); cw0.newLine();

       	cw0.close();

       	int[] assignments = kmeans.getAssignments();       	
       	String line;
       	int counter = 0; 

       	cr.close();
       	cr = new BufferedReader(new FileReader("data/"+file_source));

       	BufferedWriter[] cw = {new BufferedWriter(new FileWriter("data/"+file_clusters[1])),
                     new BufferedWriter(new FileWriter("data/"+file_clusters[2])),
                     new BufferedWriter(new FileWriter("data/"+file_clusters[3]))};
       							

       	do{
       		line = cr.readLine();
       	}while(!line.equals("@data"));

       	while((line = cr.readLine()) != null)  
       	{	      	
  			cw[assignments[counter]].newLine();     	
       		cw[assignments[counter]].write(line);	       		       		       	   			       		
   			counter++;		       		
       	}

       	for(BufferedWriter c : cw)
       	{
       		c.close();
       	}       

       	System.out.println();
	}


}