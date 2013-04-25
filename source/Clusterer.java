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
    String file_centers;
	  String[] file_trainclusters;     
    String[] file_testclusters;
    String code_condition; 
    // ArrayList[] points_clusters; 

    int number_clusters;

	Clusterer()
	{
        code_condition = "";
        // points_clusters = new ArrayList[3];
    }

	public void setSource(String s){

		file_source = s; 
	}

  public void setNumber(int k){

    number_clusters = k;
  }

	public void setResults(String[] s, String c, String[] t){

		  file_trainclusters = s; 
      file_testclusters = t;
      file_centers = c;      
	}

	public void run() throws FileNotFoundException, IOException, Exception
	{

        System.out.format("%8d - Clustering pixels...\n",number_clusters);

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

       	BufferedWriter cw0 = new BufferedWriter(new FileWriter("../_logs/"+file_centers));

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

              BufferedWriter[] cw = new BufferedWriter[number_clusters];

              for(counter = 0; counter < number_clusters; counter++){

                     String number = Integer.toString(counter);
                     cw[counter] = new BufferedWriter((new FileWriter("../_logs/"+file_trainclusters[0]+number+file_trainclusters[1])));
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

	}

    public void assign(String file_points, String file_centers, String file_assignments) throws FileNotFoundException, IOException  {

        BufferedReader br = new BufferedReader(new FileReader("../_logs/"+file_points));
        BufferedWriter bw = new BufferedWriter(new FileWriter("../_logs/"+file_assignments));

        String buffer;

        BufferedWriter[] cw = new BufferedWriter[number_clusters];

        for(int counter = 0; counter < number_clusters; counter++){

             String number = Integer.toString(counter);
             cw[counter] = new BufferedWriter((new FileWriter("../_logs/"+file_testclusters[0]+number+file_testclusters[1])));
        }

        int[] countMembers = new int[number_clusters];

        for(int countMember : countMembers)
            countMember = 0;

        int countOutliers = 0;

        while((buffer = br.readLine()) != null)      
        { 

             if(buffer.contains("%")){
                bw.write(buffer); bw.newLine();
             }                   
                

            else {

             if(h_qualify(buffer))
             {
                    int temp = h_Close(buffer,file_centers);
                    bw.write(buffer+","); bw.write(Integer.toString(temp)); bw.newLine();

                    cw[temp].write(buffer);
                    cw[temp].newLine();

                    countMembers[temp]++;
             } 

             else if(buffer.indexOf("%") == -1)
             {
                    bw.write(number_clusters); bw.newLine();               
                    countOutliers++;
             }
            }
        }  

        // System.out.println("Ambiguous Pixels: "+countMembers[0]+","+countMembers[1]+","+countMembers[2]);
        // System.out.println("Other Pixels: "+countOutliers);

        for(BufferedWriter c : cw)
        {
             c.close();
        }           

        bw.close();          
    }

    private int h_Close(String point, String file_centers) throws FileNotFoundException, IOException {

        String[] string_points = point.split(",");

        int[] points = {0,0,0};

        points[0] = Integer.parseInt(string_points[0]);
        points[1] = Integer.parseInt(string_points[1]);
        points[2] = Integer.parseInt(string_points[2]);

        int[] distance = new int[number_clusters];

        BufferedReader br = new BufferedReader(new FileReader("../_logs/"+file_centers));

        for(int i = 0; i < number_clusters; i++)
             distance[i] = h_distance(points, br.readLine());

        return h_smallest(distance);
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

       private int h_smallest(int[] distances){

              int minvalue = Integer.MAX_VALUE; 
              int minindex = 0, counter = 0;

              for( int d : distances){
                     if( d < minvalue){

                            minindex = counter;
                            minvalue = d;
                     }
                            
                     counter++;
              }

              return minindex;
       }

       public boolean h_qualify(String s)
       {
            if(s.contains("%")) return false; 

            if(code_condition.equals("")) return true;

            if(code_condition.equals("OnlyAmbiguous"))
            {
                if(s.contains("ambiguous"))
                   return true;
                else     
                   return false;          
            }

            if(code_condition.equals("NoNotRed"))
            {
                if(s.contains(",notred"))
                    return false; 
                else 
                    return true;
            }

            return true;    
        }

}