/* @author srrvnn */

package imageresearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Gather{


	ArrayList<String> list_images; 
	ArrayList<String> list_trainimages;
	ArrayList<String> list_testimages; 

	public Gather(String folder_files){
            
                list_images = new ArrayList<String>();
                list_trainimages = new ArrayList<String>();
                list_testimages = new ArrayList<String>();

		System.out.print("Gathering images . . ."); 
		list_images = h_getFiles(new File(folder_files)); 
		System.out.println("done");

	}

	public void ChooseTestRandom(){

	}

	public void ChooseTestForced(){

        int count_testimages = 0;   
        int no_testimages = list_images.size() / 6;       
        
        Random r = new Random();            
        
        String[] forcedtests1 = {  
            "_labels\\s3 bright (1)\\s3b1-01.png", "_labels\\s3 bright (1)\\s3b1-02.png",            
            "_labels\\s3 bright (1)\\s3b1-028.png", "_labels\\s3 bright (1)\\s3b1-029.png",                        
        };
        
        for( String f : forcedtests1){
            list_testimages.add(f);         
            list_images.remove(f);
            ++count_testimages;
        }
        
        
        while(count_testimages < no_testimages/3){
            
            String s = list_images.get(r.nextInt(list_images.size()));

            if(s.indexOf("bright")!=-1){
                
                list_testimages.add(s);         
                list_images.remove(s);
                ++count_testimages;
            }
            
        }
        
        
        String[] forcedtests2 = {  
            "_labels\\s1 clear (2)\\s1b1-01.png", "_labels\\s1 clear (2)\\s1b1-02.png",            
            "_labels\\s1 clear (2)\\s1b1-029.png", "_labels\\s1 clear (2)\\s1b1-030.png",            
            "_labels\\s1 clear (2)\\s1b2-01.png", "_labels\\s1 clear (2)\\s1b2-02.png",            
            "_labels\\s1 clear (2)\\s1b2-029.png", "_labels\\s1 clear (2)\\s1b2-030.png",            
        };
        
        for( String f : forcedtests2){
            list_testimages.add(f);         
            list_images.remove(f);
            ++count_testimages;
        }
        
        while(count_testimages < (int)(no_testimages/1.5)){
            
            String s = list_images.get(r.nextInt(list_images.size()));
            
            if(s.indexOf("clear")!=-1){
                
                list_testimages.add(s);         
                list_images.remove(s);
                ++count_testimages;
            }
            
        }
        
    //-- 1.5 Choose 1/18 dark test images.         
        
        String[] forcedtests3 = {  
            "_labels\\s2 dark (2)\\s2b1-011.png", "_labels\\s2 dark (2)\\s2b1-012.png",            
            "_labels\\s2 dark (2)\\s2b1-028.png", "_labels\\s2 dark (2)\\s2b1-029.png",                        
            "_labels\\s2 dark (2)\\s2b2-01.png", "_labels\\s2 dark (2)\\s2b2-02.png",            
            "_labels\\s2 dark (2)\\s2b2-029.png", "_labels\\s2 dark (2)\\s2b2-030.png",                        
        };
        
        for( String f : forcedtests3){
            list_testimages.add(f);         
            list_images.remove(f);
            ++count_testimages;
        }
        
        while(count_testimages < no_testimages){
            
            String s = list_images.get(r.nextInt(list_images.size()));
            
            if(s.indexOf("dark")!=-1){
                
                list_testimages.add(s);         
                list_images.remove(s);
                ++count_testimages;
            }
        }

	}

	public ArrayList<String> GetImages(){

		return list_images;
	} 

	public ArrayList<String> GetTrainImages(){

		return list_trainimages; 
	}

	public ArrayList<String> GetTestImages(){

		return list_testimages; 
	}

	public static ArrayList<String> h_getFiles(final File folder_files){
        
        ArrayList<String> list_files = new ArrayList<String>();
        
        for(final File tEntry : folder_files.listFiles()){
            
            if(tEntry.isDirectory()){
                
                list_files.addAll(h_getFiles(tEntry));
            }
            
            else if(tEntry.getName().indexOf("l-")==-1){  
                
               list_files.add(folder_files.toString()+"\\"+tEntry.getName()); 
               
            }
        }
        
        return list_files;
        
    }

}

