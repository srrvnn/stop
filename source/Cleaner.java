import java.io.*;
import java.util.*;

public class Cleaner{

	Cleaner() {}

	public void deleteFilesFromFolder(String nameFolder) {

		ArrayList<String> ListFiles = getFiles(new File("../"+nameFolder));

		for(String f : ListFiles){

			File d = new File(f);
			d.delete();
		}
	}

	private ArrayList<String> getFiles(final File folder)
    {        
        ArrayList<String> list_files = new ArrayList<String>();
        
        for(final File tEntry : folder.listFiles()){
            
            if(tEntry.isDirectory()){
                
                list_files.addAll(getFiles(tEntry));
            }
            
            else {  
                
               list_files.add(folder.toString()+"\\"+tEntry.getName());                
            }
        }
        
        return list_files;        
    }
}