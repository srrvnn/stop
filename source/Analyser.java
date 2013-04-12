import java.io.*;
import java.util.*;

public class Analyser {

	String filetoAnalyse; 
	Map<String, Integer> counterClasses;
	Map<String, Integer> counterMajority;
	String maxkey;

	int total, sum, number;
	
	Analyser() {

		counterClasses = new HashMap<String, Integer>();
		counterMajority = new HashMap<String, Integer>();
		total = 0; sum = 0; number = 0;

		maxkey = "";
	}

	void setFile(String f) {

		filetoAnalyse =  f;
	}

	void countClasses() throws FileNotFoundException, IOException, Exception {		

		total = 0; 
		counterClasses.clear();
		
		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+filetoAnalyse));

		String line = null; 

		while((line = br.readLine()) != null){

			if(line.indexOf("%") != -1)
				continue;	

			total++;

			String[] items = line.split(",");
			h_countClasses(items[items.length-1]);
		}

		System.out.println("-------------------------------");
		System.out.println("File: "+filetoAnalyse);

		int tempmax, max = 0; 		

		for(Map.Entry<String, Integer> entry : counterClasses.entrySet()){

			System.out.println(entry);
			tempmax = (int)entry.getValue();

			if(tempmax > max){

				max = tempmax;	
				maxkey = (String)entry.getKey();
			} 
		}			

		System.out.println("Total : "+total);
		System.out.println("Majority: "+maxkey+" with "+max*100/total+"%");		

		if(counterMajority.containsKey(maxkey))	{

			int temp = (int)counterMajority.get(maxkey);
			counterMajority.put(maxkey,temp+1);			
		}

		else counterMajority.put(maxkey,1);
			
		sum = sum+max*100/total;
		number++;

	}

	String getMajority() {

		return maxkey;
	}

	void countMajority() {

		System.out.println("-------------------------------");
		System.out.println("Grand Majority: "+ sum/number);

		for(Map.Entry<String, Integer> e : counterMajority.entrySet()){
			System.out.println(e);
		}

	}

	void substituteClasses(String file, String dfile, ArrayList<String> c) throws FileNotFoundException, IOException, Exception{

		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+file));
		BufferedWriter bw = new BufferedWriter(new FileWriter("../_logs/"+dfile));

		String line; 

		while((line = br.readLine()) != null){

			if(line.contains("%") || line.equals("") || line ==  null) {

				bw.write(line); bw.newLine();
			}
				
			else {	

				String[] items = line.split(",");
				String write = new String(items[0]+","+items[1]+","+items[2]+","+c.get(Integer.parseInt(items[items.length-1])));
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

		System.out.println("Total : "+total);
		System.out.println("Hits : "+hit);
		System.out.println("Accuracy : "+hit*100/total);

	}

	void h_countClasses(String c) {

		int counter = 0; 

		if(counterClasses.containsKey(c)){

			counter = (int)counterClasses.get(c);
			counterClasses.put(c,counter+1);
		}		

		else {

			counterClasses.put(c,1);
		}		
	}
}