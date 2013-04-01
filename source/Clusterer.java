// package imageresearch;

import java.io.*;
import java.util.ArrayList;
import weka.core.*;
import weka.classifiers.*;
import weka.clusterers.SimpleKMeans;
import weka.core.converters.ArffLoader;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Clusterer
{

	String file_source; 
	String[] file_clusters;
       // ArrayList[] points_clusters; 

       int number_clusters;

	Clusterer()
	{
              // points_clusters = new ArrayList[3];
       }

	public void setSource(String s)
	{
		file_source = s; 
	}

	public void setClusters(String[] s)
	{
		file_clusters = s; 
              number_clusters = file_clusters.length - 1;
	}

	public void run() throws FileNotFoundException, IOException, Exception
	{

              int counter;

		BufferedReader cr = new BufferedReader(new FileReader("../_logs/"+file_source));
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
       	kmeans.setNumClusters(number_clusters);
       	kmeans.setMaxIterations(1500);
       	kmeans.buildClusterer(nd);

       	// String cc1 = kmeans.getClusterCentroids().instance(0).toString();  
       	// String cc2 = kmeans.getClusterCentroids().instance(1).toString();  
       	// String cc3 = kmeans.getClusterCentroids().instance(2).toString();

       	BufferedWriter cw0 = new BufferedWriter(new FileWriter("../_logs/"+file_clusters[0]));

              for(counter = 0; counter < number_clusters ; counter++)
              {
                     cw0.write(kmeans.getClusterCentroids().instance(counter).toString());
                     cw0.newLine();
              }
       	
       	cw0.close();

       	int[] assignments = kmeans.getAssignments();       	
       	String line;       	

       	cr.close();
       	cr = new BufferedReader(new FileReader("../_logs/"+file_source));

       	// BufferedWriter[] cw = {new BufferedWriter(new FileWriter("../_logs/"+file_clusters[1])),
        //              new BufferedWriter(new FileWriter("../_logs/"+file_clusters[2])),
        //              new BufferedWriter(new FileWriter("../_logs/"+file_clusters[3]))};

              BufferedWriter[] cw = new BufferedWriter[number_clusters];

              for(counter = 0; counter < number_clusters; counter++){

                     cw[counter] = new BufferedWriter((new FileWriter("../_logs/"+file_clusters[counter+1])));
              }
       							

       	do{
       		line = cr.readLine();
       	}while(!line.equals("@data"));

              counter = 0;

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

       public void Assign(String file_points, String file_centers, String point_positions, String[] file_clusters) throws FileNotFoundException, IOException       
       {
              BufferedReader br = new BufferedReader(new FileReader("../_logs/"+file_points));
              BufferedWriter ppw = new BufferedWriter(new FileWriter("../_logs/"+point_positions));

              String buffer;

              BufferedWriter[] cw = {new BufferedWriter(new FileWriter("../_logs/"+file_clusters[0])),
                     new BufferedWriter(new FileWriter("../_logs/"+file_clusters[1])),
                     new BufferedWriter(new FileWriter("../_logs/"+file_clusters[2]))};

              int[] cn = {0,0,0};
              int c3 = 0;

              while((buffer = br.readLine()) != null)      
              {                    

                     if(buffer.contains("ambiguous"))
                     {
                            int temp = h_Close(buffer,file_centers);
                            ppw.write(Integer.toString(temp)); ppw.newLine();

                            cw[temp].write(buffer);
                            cw[temp].newLine();

                            cn[temp]++;
                     }      
                     else if(buffer.indexOf("%") == -1)
                     {
                            ppw.write("3"); ppw.newLine();               
                            c3++;
                     }
              }  

              // System.out.println("Ambiguous Pixels: "+cn[0]+","+cn[1]+","+cn[2]);
              // System.out.println("Other Pixels: "+c3);

              for(BufferedWriter c : cw)
              {
                     c.close();
              }           

              ppw.close();          
       }

       private int h_Close(String point, String name_file_centers) throws FileNotFoundException, IOException
       {
              String[] string_points = point.split(",");

              int[] points = {0,0,0};

              points[0] = Integer.parseInt(string_points[0]);
              points[1] = Integer.parseInt(string_points[1]);
              points[2] = Integer.parseInt(string_points[2]);

              int[] distance = {0,0,0};
              
              BufferedReader br = new BufferedReader(new FileReader("../_logs/"+name_file_centers));

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