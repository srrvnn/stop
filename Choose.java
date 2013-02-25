package imageresearch;

import java.io.*;

public class Choose {
   
    String name_fromfile;
    String name_tofile;
    
    String code_condition; 
    
    public void SetFromFile(String s)
    {
        name_fromfile = new String(s);
    }
    
    public void SetToFile(String s)
    {
        name_tofile = new String(s);
    }
    
    public void SetCondition(String s)
    {
        code_condition = new String(s);
    }
    
    public void ChooseRows() throws FileNotFoundException, IOException
    {
        
        BufferedReader r = new BufferedReader(new FileReader(name_fromfile));
        BufferedWriter w = new BufferedWriter(new FileWriter(name_tofile));
        
        String line = null; 
        
        while((line = r.readLine()) != null)
        {
            if(h_qualify(line)) 
                w.write(line); w.newLine();                  
        }      
        
        r.close();
        w.close();
        
    }
    
    public boolean h_qualify(String s)
    {
        return true;
    }
}
