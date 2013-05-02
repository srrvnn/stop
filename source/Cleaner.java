// @author srrvnn

import java.io.File;
import java.util.ArrayList;

public class Cleaner{

	Cleaner() {}

	public void deleteFilesFromFolder(String nameFolder) {

		ArrayList<String> ListFiles = h_getFiles(new File("../"+nameFolder));

		for(String f : ListFiles){

			File d = new File(f);
			d.delete();
		}
	}

	private ArrayList<String> h_getFiles(final File folder)
    {        
        ArrayList<String> list_files = new ArrayList<String>();

        try{
        
	        for(final File tEntry : folder.listFiles()){
	            
	            if(tEntry.isDirectory()){
	                
	                list_files.addAll(h_getFiles(tEntry));
	            }
	            
	            else {  
	                
	               list_files.add(folder.toString()+"\\"+tEntry.getName());                
	            }
	        }

        }

        catch (Exception e){

        	System.out.println("Error, reported at 'Cleaner'.");
        }

        
        return list_files;        
    }
}