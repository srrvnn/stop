import java.io.*;
import java.util.*;

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

	void substituteClasses(String file, String dfile) throws FileNotFoundException, IOException, Exception{

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

	public void printRadius(String cfile) throws FileNotFoundException, IOException, Exception{

		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+cfile));
		String line; 

		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, average = 0, count = 0, sum = 0;
		int count2 = 0 ;

		int[] red = {255,0,0};	

		while((line = br.readLine()) != null){

			String classvalue = new String();					

			classvalue = classes.get(count2++);

			if(!classvalue.contains("red"))
				continue;

			count++;

			int temp = h_distance(red,line);

			sum += temp;

			if(temp < min)
				min = temp;

			if(temp > max)
				max = temp;
		}

		average = (int)sum/count;

		System.out.println("Min Radius : "+min);
		System.out.println("Max Radius : "+max);
		System.out.println("Average Radius : "+average);
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