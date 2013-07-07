import java.io.*;
import java.util.*;

import java.util.Arrays;
import java.util.ArrayList;

public class Analyser {

	ArrayList<String> logs;

	int numberofClusters;

	String[] filestoAnalyse;
	String filetoAnalyse; 
	String maxkey;

	ArrayList<String> classes;
	Map<String, Integer> counterClasses;
	Map<String, Integer> counterMajority;	

	int total, sum, number;
	int r1, r2;
	
	Analyser() {

		counterClasses = new HashMap<String, Integer>();
		counterMajority = new HashMap<String, Integer>();
		classes = new ArrayList<String>();
		total = 0; sum = 0; number = 0;

		maxkey = "";

		logs = new ArrayList<String>();
		logs.add("--");
		logs.add("Analyser class instantiated. Logs:");	
	}

	//---- SET AND GET FUNCTIONS

	public void setNumber(int k){

		numberofClusters = k;
		logs.add("Set number of clusters : "+k);
	}

	public void setFile(String[] f){

		filestoAnalyse = f;
	}

	public ArrayList<String> getClasses(){

		return classes;
	}

	//---- LOGIC FUNCTIONS

	public void run() throws FileNotFoundException, IOException, Exception {

		System.out.format("%8d - Analyzing Clustering...\n",numberofClusters);

		for( int i = 0; i < numberofClusters; i++) {

            h_setFile(filestoAnalyse[0]+i+filestoAnalyse[1]);
            h_countClasses();  

            classes.add(i,h_getMajority());
        }

        h_printMajority();
	}		
	

	void h_printMajority() {

		logs.add("-------------------------------");
		logs.add("Average Majority: "+ sum/number + "%");

		StringBuilder sb = new StringBuilder("");

		for(Map.Entry<String, Integer> e : counterMajority.entrySet()){

			sb.append(e.getKey());
			sb.append(" :");
			sb.append(e.getValue()+", ");
		}

		logs.add(sb.toString());

	}

	void substituteClassesC(String file, String dfile) throws FileNotFoundException, IOException, Exception{

		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+file));
		BufferedWriter bw = new BufferedWriter(new FileWriter("../_logs/"+dfile));

		String line; 

		while((line = br.readLine()) != null){

			if(line.contains("%") || line.equals("") || line ==  null) {

				bw.write(line); bw.newLine();
			}
				
			else {	

				String[] items = line.split(",");
				String write = new String(items[0]+","+items[1]+","+items[2]+","+classes.get(Integer.parseInt(items[items.length-1])));
						bw.write(write); bw.newLine();			
				
			}
			
		}

		br.close(); bw.close(); 

	}

	void substituteClassesR(String file, String dfile) throws FileNotFoundException, IOException, Exception{

		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+file));
		BufferedWriter bw = new BufferedWriter(new FileWriter("../_logs/"+dfile));

		String line; 

		while((line = br.readLine()) != null){

			if(line.contains("%") || line.equals("") || line ==  null) {

				bw.write(line); bw.newLine();
			}
				
			else {	
				
				int[] red = {255,0,0};	
				int distance = h_distance(red, line);	

				String[] items = line.split(",");			
				String write = new String();

				 if(distance < r1){

				 	write = new String(items[0]+","+items[1]+","+items[2]+",red");
				 }
				 	

				if(distance > r2){

					write = new String(items[0]+","+items[1]+","+items[2]+",notred"); 
				}
				

				else{

					write = new String(items[0]+","+items[1]+","+items[2]+",ambiguous");
				}				
				
				bw.write(write); bw.newLine();			
				
			}
			
		}

		br.close(); bw.close(); 

	}

	void compareClasses(String labels, String classes) throws FileNotFoundException, IOException, Exception {

		BufferedReader bl = new BufferedReader(new FileReader("../_logs/"+labels));
		BufferedReader bc = new BufferedReader(new FileReader("../_logs/"+classes));

		String line1 = new String();
		String line2 = new String(); 

		int total = 0, hit = 0;

		while((line1 = bl.readLine()) != null){

			line2 = bc.readLine();

			if(line1.contains("%"))
				continue;
			

			if(line1.contains(",notred"))
				continue;		

			total++;

			if(line1.contains("ambiguous") && line2.contains("ambiguous"))
				hit++; 

			if(line1.contains(",red") && line2.contains(",red"))
				hit++;
		}

		System.out.println("-------------------------------");

		System.out.println("Total Pixels : "+total);
		System.out.println("Correctly Classified Pixels : "+hit);
		System.out.println("Accuracy : "+hit*100/total);

	}

	public void computeRadius(String cfile) throws FileNotFoundException, IOException, Exception{

		// needs information from the ArrayList 'classes', hence must be called after run(). 

		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+cfile));
		String line; 

		int numberOfClasses = 2;

		int[] min = new int[numberOfClasses];
		int[] max = new int[numberOfClasses];
		int[] average = new int[numberOfClasses];
		int[] sum = new int[numberOfClasses];
		int[] count = new int[numberOfClasses];

		Arrays.fill(min, Integer.MAX_VALUE);
		Arrays.fill(max, Integer.MIN_VALUE);
		Arrays.fill(average, 0);
		Arrays.fill(sum, 0);
		Arrays.fill(count, 0);

		int count2 = -1;
		int colorindex = -1; 

		int[] red = {255,0,0};	

		while((line = br.readLine()) != null){

			String classvalue = new String();					

			classvalue = classes.get(++count2);

			if(classvalue.contains("red"))
				colorindex = 0;

			if(classvalue.contains("ambiguous"))
				colorindex = 1; 

			int temp = h_distance(red,line);

			++count[colorindex];
			sum[colorindex] += temp;

			if(temp < min[colorindex])
				min[colorindex] = temp;

			if(temp > max[colorindex])
				max[colorindex] = temp;
		}

		for(int i =0; i<numberOfClasses; i++){

			// System.out.println("---------------------------");

			// if(i==0) System.out.println("Values for Red:");
			// if(i==1){

			// 	System.out.println("Values for Ambiguous:");
			// 	r2 = max[i];
			// } 

			average[i] = (int)sum[i]/count[i];
			// System.out.println("Min Radius : "+min[i]);
			// System.out.println("Max Radius : "+max[i]);
			// System.out.println("Average Radius : "+average[i]);		

		}	

		br.close();
		br = new BufferedReader(new FileReader("../_logs/"+cfile));

		int[] sumvariance = new int[numberOfClasses];
		int[] variance = new int[numberOfClasses];
		int[] sd = new int[numberOfClasses];

		Arrays.fill(sumvariance, 0);
		Arrays.fill(variance, 0);
		Arrays.fill(count, 0);
		Arrays.fill(sd, 0);

		count2 = -1;

		while((line = br.readLine()) != null){

			String classvalue = new String();					

			classvalue = classes.get(++count2);

			if(classvalue.contains("red"))
				colorindex = 0;

			if(classvalue.contains("ambiguous"))
				colorindex = 1; 

			int temp = h_distance(red,line);

			++count[colorindex];

			int a = average[colorindex] - temp;	
			sumvariance[colorindex] += (int)Math.pow((double)a,2);
		}	

		for(int i =0; i<numberOfClasses; i++){

			variance[i] = (int)sumvariance[i]/count[i];
			sd[i] = (int)Math.sqrt((double)variance[i]);

			System.out.println("---------------------------");

			if(i==0){

				System.out.println("Values for Red:");	
				r1 = average[i]+sd[i];
				System.out.println("r1:"+r1);
			} 
			if(i==1){

				System.out.println("Values for Ambiguous:");
				r2 = max[i];
				System.out.println("r2:"+r2);
			} 

			System.out.println("Min Radius : "+min[i]);
			System.out.println("Max Radius : "+max[i]);
			System.out.println("Average Radius : "+average[i]);		
			System.out.println("Standard Deviation: "+sd[i]);



		}	


	}

	public void printLogs() {

		if(logs.isEmpty()){
			System.out.println("No logs available.");
			return;
		}

		for(String s : logs)
			System.out.println(s);		
	}

	public void fileLogs() throws FileNotFoundException, IOException {

		BufferedWriter w = new BufferedWriter(new FileWriter("../_logs/logs-Analyser-k"+numberofClusters+".txt"));


		if(logs.isEmpty()){
			System.out.println("No logs available.");
			return;
		}

		for(String s : logs)
			w.write(s+"\n");

        w.close();

	}

	private void h_countClasses() throws FileNotFoundException, IOException, Exception {		

		total = 0; 
		counterClasses.clear();
		
		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+filetoAnalyse));

		String line = null; 

		while((line = br.readLine()) != null){

			if(line.indexOf("%") != -1)
				continue;	

			total++;

			String[] items = line.split(",");
			h_countClass(items[items.length-1]);
		}

		logs.add("-------------------------------");
		logs.add("File: "+filetoAnalyse);

		int tempmax, max = 0; 		

		StringBuilder sb = new StringBuilder("");

		for(Map.Entry<String, Integer> entry : counterClasses.entrySet()){

			sb.append(entry.getKey());
			sb.append(" :");
			sb.append(entry.getValue()+", ");

			tempmax = (int)entry.getValue();

			if(tempmax > max){

				max = tempmax;	
				maxkey = (String)entry.getKey();
			} 
		}	

		logs.add(sb.toString());	

		logs.add("T: "+total + " M: "+maxkey+" with "+max*100/total+"%");			

		if(counterMajority.containsKey(maxkey))	{

			int temp = (int)counterMajority.get(maxkey);
			counterMajority.put(maxkey,temp+1);			
		}

		else counterMajority.put(maxkey,1);
			
		sum = sum+max*100/total;
		number++;		

	}

	String h_getMajority() {

		return maxkey;
	}

	void h_setFile(String f) {

		filetoAnalyse =  f;
	}

	private void h_countClass(String c) {

		int counter = 0; 

		if(counterClasses.containsKey(c)){

			counter = (int)counterClasses.get(c);
			counterClasses.put(c,counter+1);
		}		

		else {

			counterClasses.put(c,1);
		}		
	}	

   private int h_distance(int[] point, String center){ 

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