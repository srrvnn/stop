import java.io.*;
import java.util.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Tabler {
	

	public static void main(String[] args) throws FileNotFoundException, IOException, Exception{

		int[] ks = {2,5,10,20,30,40,50,60,70,80,90,100,110};

		Tabler otabler = new Tabler();

		otabler.startHTML();

		for( int k : ks){

			otabler.setFile("logs-Analyser-k"+k+".txt");
			otabler.buildHTML();
		}		

		otabler.endHTML();

		otabler.writeHTML();
	}

	String fileToAnalyze, fileToWrite;
	boolean headerdone, footerdone;
	ArrayList<String> bw;

	Tabler(){

		fileToAnalyze = new String();	
		bw = new ArrayList<String>();
	}	

	public void setFile(String s){

		fileToAnalyze = s;		
	}

	public void startHTML() {


		bw.add("<html>"); 		
		bw.add("<head>"); 
		bw.add("<title> Cluster Evaluation </title>"); 
		bw.add("</head>"); 

		bw.add("<body>"); 

		bw.add("<style> table, th, td, tr {border: 1px solid black; padding: 5px} </style>");

		bw.add("<table>"); 
	}

	public void endHTML() {

		bw.add("<tr> <td> these tables are automatically generated. @ author: srrvnn </td> </tr>" );			
		

		bw.add("</table>"); 
		bw.add("</body"); 
		bw.add("</html>"); 		
	}

	
	public void buildHTML() throws FileNotFoundException, IOException, Exception{


		// open the file 
		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+fileToAnalyze));		


		
		bw.add("<tr> <th> Cluster Number </th> <th> Total Pixels </th> <th> Red Pixels </th> <th> Ambiguous Pixels </th> <th> Majority Class </th> </tr>"); 
		


		String line; boolean found;
		String[] info = new String[3];

		while((line = br.readLine()) != null){

			if(line.contains("Average Majority")){

				break;
			}

			if(line.contains("File")){

				info[0] = line; 
				info[1] = br.readLine();
				info[2] = br.readLine();		
			

				bw.add("<tr>");

				// td for cluster number;

				Pattern p = Pattern.compile("-?\\d+");
				Matcher m = p.matcher(info[0]);

				found = false;

				while (m.find()) {				

					found = true;
					bw.add("<td>" + m.group() + "</td>");	  				
				}

				if(!found){

					bw.add("<td> - </td>" );

				}

				// td for cluser total pixels; 

				p = Pattern.compile("T: \\d+");
				m = p.matcher(info[2]);

				found = false;

				while (m.find()) {

					found = true;

					bw.add("<td>");
	  				bw.add(m.group());
	  				bw.add("</td>");
	  				

	  				break;
				}

				if(!found){

					bw.add("<td> - </td>" );

				}

				// td for cluster red pixels;`

				p = Pattern.compile("red :\\d+");
				m = p.matcher(info[1]);

				found = false;

				while (m.find()) {

					found = true;

					bw.add("<td>");
	  				bw.add(m.group());
	  				bw.add("</td>");
	  				

	  				break;
				}

				if(!found){

					bw.add("<td> - </td>" );

				}				

				// td for cluster ambiguous pixels;

				p = Pattern.compile("ambiguous :\\d+");
				m = p.matcher(info[1]);

				found = false;

				while (m.find()) {

					found = true;

					bw.add("<td>");
	  				bw.add(m.group());
	  				bw.add("</td>");	  				

	  				break;
				}

				if(!found){

					bw.add("<td> - </td>" );
				}

				// td for cluser majority 
				
					bw.add("<td>" + info[2].replaceAll("T: \\d+ M: ", "") + "</td>");	  					  				

				// td for cluser majority percentage;				

				bw.add("</tr>");	
				

			}
			
		}

		String line2 = br.readLine();		
		bw.add("<tr> <td colspan=\"5\"> Number of Clusters: "+fileToAnalyze.replaceAll("[^\\d]+","")+ " - " + line2 + " - " +line +"</td> </tr>");		

		bw.add("</table>");
		bw.add("<br/><br/>");

		bw.add("<table>");

	}
	


	public void writeHTML() throws FileNotFoundException, IOException{


		BufferedWriter w = new BufferedWriter(new FileWriter("../_logs/clusters.html"));


		if(bw.isEmpty()){
			System.out.println("No logs available.");
			return;
		}

		for(String s : bw)
			w.write(s+"\n");

        w.close();
	}


}