// @author srrvnn

import java.util.ArrayList;
import java.lang.String;
import java.lang.Double;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Cohener {

    private static final String folder_anntation = "_annotation";

	ArrayList<String> logs;

	String folder; 

	int numberLabels;
	int numberAnnotators;
	Double[] kappa; 
	int[][] agreement;	

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Cohener c = new Cohener();
		c.setFolder("_annotation");
		c.setLabels(4);

		c.run();
		c.fileLogs();
	}


	Cohener() {

		logs = new ArrayList<String>();

		logs.add("--");
		logs.add("Cohener class instantiated. Logs:");	

	}

	public void setFolder(String s){

		folder = new String(s);

		logs.add("Root Folder set as : "+folder);
	}

	public void setLabels(int l){

		numberLabels = l;
		kappa = new Double[l];

		agreement = new int[l][];

		for(int i = 0; i < l; i++)
			agreement[i] = new int[]{0,0,0,0};	


		logs.add("Labels set as : "+numberLabels);
	}

	public void run() throws FileNotFoundException, IOException {

        String[] labels = {"Clearly Red","Ambiguously Not Red","Ambiguously Red","Clearly Not Red"};

		int loop; 

		logs.add("Kappa computation begins");

		// Get number of annotators 

		ArrayList<String> annotatornames = 	h_getAnnotatorNames(new File("../"+folder));
		numberAnnotators = annotatornames.size();


		logs.add(numberAnnotators +" annotators found.");

		if(numberAnnotators != 2){
			logs.add("Error : Number of annotators does not equal two. ");
			return;
		}		

		// Get all images from the first annotator

		ArrayList<String> allsourceimages = h_getFiles(new File("../_annotation/"+annotatornames.get(0)));

		// For each image, look at other image and count instances of interest

		BufferedImage sourceimage; 
		BufferedImage[] labelimage = new BufferedImage[numberAnnotators]; 		
        
        // BufferedWriter fout = new BufferedWriter(new FileWriter("../_logs/"+name_file));
        
        long count_pixels = 0;
        
        for( String image : allsourceimages ){

        	String IMG = image; 
        	String IMGL = h_getLabelName(image);

        	sourceimage = ImageIO.read(new File(IMG)); 

        	System.out.println(IMGL);        	

        	logs.add("Opening labelled images for computation");

        	for(loop = 0; loop < numberAnnotators; loop++){

        			String temp = IMGL.replaceAll(annotatornames.get(0), annotatornames.get(loop));
        			labelimage[loop] = ImageIO.read(new File(temp));

        			logs.add("Opening :"+temp);
        	}       

        	// counts for each image 

        	int ignored = 0, considered = 0; 		      	                    

            for(int i = 0; i< sourceimage.getHeight(); i++){
                for(int j=0; j < sourceimage.getWidth(); j++){

                	int[] label1 = h_getPixelData(labelimage[0], i, j);
                	int[] label2 = h_getPixelData(labelimage[1], i, j);

                	if(h_isWhite(label1) || h_isWhite(label2))
            			++ignored;

            		else{           		

                        ++considered;
            			h_updateAgreement(label1,label2);           			
            		}
                }
            }

            count_pixels += considered;
            logs.add(ignored + " pixels ignored & " + considered + " pixels considered");
            
        }

        logs.add("--");

        logs.add(count_pixels + " pixels were considered in total.");

        double kappatotal = 0;

        for(loop = 0; loop < numberLabels; loop++){

        	int yy = agreement[loop][0];
        	int yn = agreement[loop][1];            	
        	int ny = agreement[loop][2];
        	int nn = agreement[loop][3];

        	int total = yy + yn + ny + nn;

        	logs.add("Values for label " + labels[loop] + " are: "+ yy + " " + yn + " " + ny + " " + nn);           	

        	double pra = 0, pre = 0;

        	// escaping wierd cases to calculate kappa values

        	if((yy+nn) == 0)
        		pra = 0;

        	else 
        		pra = (double)(yy+nn)/total;

        	if(nn == total)
        		pre = (((ny+nn)/total)*((yn+nn)/total));

        	if(yy == total)
        		pre = (((yy+yn)/total)*((ny+yy)/total));

        	if(nn != total && yy != total)
        		pre = (((yy+yn)/total)*((ny+yy)/total)) + (((ny+nn)/total)*((yn+nn)/total));

        	if((pre == pra) || (pre == 1))
        		kappa[loop] = 0.0;

        	else 
        		kappa[loop] = (pra - pre)/(1 - pre);

        	logs.add("kappa for label :" + kappa[loop]);

        	kappatotal += kappa[loop];
        }

        logs.add("Total kappa over all labels: "+ kappatotal);

		System.out.println("The average kappa value over all labels is : " + (double)kappatotal/numberLabels);
        logs.add("The average kappa value over all labels is : " + (double)kappatotal/numberLabels);

		logs.add("Kappa computation ends.");

        logs.add("--");
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

		BufferedWriter w = new BufferedWriter(new FileWriter("../_logs/logs-Cohener.txt"));


		if(logs.isEmpty()){
			System.out.println("No logs available.");
			return;
		}

		for(String s : logs)
			w.write(s+"\n");

        w.close();

	}

	// Helper Functions 

	private void h_updateAgreement(int[] l1, int[] l2) {

		String[] labels = {"RED","GREEN","BLUE","YELLOW"};
		int label1 = 3;
		int label2 = 3;

		if(h_isRed(l1)) label1 = 0;
		if(h_isGreen(l1)) label1 = 1;
		if(h_isBlue(l1)) label1 = 2;
		if(h_isYellow(l1)) label1 = 3;

		if(h_isRed(l2)) label2 = 0;
		if(h_isGreen(l2)) label2 = 1;
		if(h_isBlue(l2)) label2 = 2;
		if(h_isYellow(l2)) label2 = 3;		

		for(int i = 0; i < numberLabels; i++){

			if(label1 == i){

				if(label2 == i)
					agreement[i][0]++;
				else 
					agreement[i][1]++;
			}

			else{

				if(label2 == i)
					agreement[i][2]++;
				else 				
					agreement[i][3]++;	
			}
		}
	}

	//-- Lists all folders inside the root folder to get annotator names. 

	private ArrayList<String> h_getAnnotatorNames(File f) {

		ArrayList<String> annotatornames =  new ArrayList<String>();
		File rootFolder = f;

		for(File annotatorfolder : rootFolder.listFiles()){

			annotatornames.add(annotatorfolder.getName());
		}

		return annotatornames;
	}

	//-- List all source images, ignoring ones with 'l-' in their names.

	private ArrayList<String> h_getFiles(final File f) {

    	File folder_files = f;

    	logs.add("Seeing inside :" + f.getPath());
        
        ArrayList<String> list_files = new ArrayList<String>();

        try {
        
        for(final File tEntry : folder_files.listFiles()){
            
            if(tEntry.isDirectory()){
                
                list_files.addAll(h_getFiles(tEntry));
            }
            
            else if(tEntry.getName().indexOf("l")==-1){  
                
               list_files.add(folder_files.toString()+"\\"+tEntry.getName()); 
               
            }
        }

        }

        catch(Exception e) {

        	logs.add("Error : Something inside h_getFiles");
        	logs.add(e.getMessage());
        }
        	
        
        
        return list_files;        
    }

    // function to compute the label name given the name of an image

    private String h_getLabelName(String s){

    	String sourcename = s; 

    	String t1 = sourcename.substring(0,sourcename.lastIndexOf("\\")+1);
        String t2 = sourcename.substring(sourcename.lastIndexOf("\\")+1,sourcename.length());           
        
        String labelname = t1+t2.substring(0,t2.indexOf("."))+"l."+t2.substring(t2.indexOf(".")+1);

        return labelname;
    }    

    // function to retreive a pixel's rgb values given the image and index

    private int[] h_getPixelData(BufferedImage img, int x, int y) {
        
        int h = img.getHeight(); 
        int w = img.getWidth(); 
        
        int rgb[];
        
        if(x<0 || x==h || y<0 || y==w){
            
        rgb = new int[] {0,0,0};
            
        }
            
        else {
        
        int argb = img.getRGB(y,x);
        
        rgb = new int[] {
          (argb >> 16) & 0xff, 
          (argb >>  8) & 0xff, 
          (argb      ) & 0xff
        };
        
        }
      
        return rgb;
    }

    // function to detect colors in the labelled pixels 

    private boolean h_isWhite(int[] rgb) {
        
        if(rgb[0] > 230 && rgb[1] > 230 && rgb[2] > 230) return true;
        else return false;                 
    }

    public boolean h_isRed(int[] rgb) {

        if(rgb[0] > 230 && rgb[1] < 25 && rgb[2] < 25)      
            return true;

        return false;
    }

    private boolean h_isYellow(int[] rgb) {

        if(rgb[0] > 230 && rgb[1] > 230 && rgb[2] < 25)      
            return true;
        
        return false;
    }

    private boolean h_isBlue(int[] rgb) {

        if(rgb[0] < 25 && rgb[1] < 25 && rgb[2] > 230)      
            return true;
        
        return false;
    }

    private boolean h_isGreen(int[] rgb) {

        if(rgb[0] < 25 && rgb[1] > 230 && rgb[2] < 25)      
            return true;
        
        return false;
    }
}