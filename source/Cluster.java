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

       int number_clusters;

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

		BufferedReader cr = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_source));
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

       	BufferedWriter cw0 = new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[0]));

       	cw0.write(cc1); cw0.newLine();
       	cw0.write(cc2); cw0.newLine();
       	cw0.write(cc3); cw0.newLine();

       	cw0.close();

       	int[] assignments = kmeans.getAssignments();       	
       	String line;
       	int counter = 0; 

       	cr.close();
       	cr = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_source));

       	BufferedWriter[] cw = {new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[1])),
                     new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[2])),
                     new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[3]))};
       							

       	do{
       		line = cr.readLine();
       	}while(!line.equals("@data"));

       	while((line = cr.readLine()) != null)  
       	{	
                     if(line.contains("%"))      	  			     	
                            continue;
                     
       		cw[assignments[counter]].write(line);	       		       		       	   			       		
                     cw[assignments[counter]].newLine();
   			counter++;		       		
       	}

       	for(BufferedWriter c : cw)
       	{
       		c.close();
       	}       

       	// System.out.println();
	}

       public void Assign(String file_points, String file_centers, String[] file_clusters) throws FileNotFoundException, IOException       
       {
              BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_points));
              String buffer;

              BufferedWriter[] cw = {new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[0])),
                     new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[1])),
                     new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+file_clusters[2]))};


              while((buffer = br.readLine()) != null)      
              {
                     if(buffer.contains("ambiguous"))
                     {
                            int temp = h_Close(buffer,file_centers);
                            cw[temp].write(buffer);
                            cw[temp].newLine();
                     }
                     
              }  

              for(BufferedWriter c : cw)
              {
                     c.close();
              }                          
       }

       private int h_Close(String point, String name_file_centers) throws FileNotFoundException, IOException
       {
              String[] string_points = point.split(",");

              int[] points = {0,0,0};

              points[0] = Integer.parseInt(string_points[0]);
              points[1] = Integer.parseInt(string_points[1]);
              points[2] = Integer.parseInt(string_points[2]);

              int[] distance = {0,0,0};
              
              BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+name_file_centers));

              distance[0] = h_distance(points,br.readLine());
              distance[1] = h_distance(points,br.readLine());
              distance[2] = h_distance(points,br.readLine());

              if(distance[0] < distance[1])
              {
                     if(distance[0] < distance[2])
                            return 0; 
                     else return 2; 
              }

              else 
              {
                     if(distance[1] < distance[2])
                            return 1;
                     else return 2; 
              }
       }

       private int h_distance(int[] point, String center)
       {      
              String[] string_centers = center.split(",");

              float[] centers = {0,0,0};

              centers[0] = Float.parseFloat(string_centers[0]);
              centers[1] = Float.parseFloat(string_centers[1]);
              centers[2] = Float.parseFloat(string_centers[2]);

              int distance = 0;
              int counter = 0;

              for(int p : point)
              {
                     distance = distance + Math.abs(p-(int)centers[counter]);
                     counter++;
              }    

              return distance;
       }




}