// package imageresearch;

import java.io.*;
import java.util.*;

public class Note {
    
    String name_fromfile;
    String name_tofile;
    
    String code_condition; 
    String[] list_features;

    boolean test;
    
    private HashMap attributetypes;
    
    Note()
    {
        name_fromfile = "";
        name_tofile = "";
        code_condition = "";          
        list_features = null;
        test = false;        
        
        attributetypes = new HashMap();

        attributetypes.put("R","numeric");
        attributetypes.put("G","numeric");
        attributetypes.put("B","numeric");
                       
    }
    
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
    
    public void SetFeatures(String[] s)
    {
        list_features = s; 
    }

    public void SetTest()
    {
        test = true;
    }   
    
    
    public void NoteFeatures(String name_relation) throws FileNotFoundException, IOException
    {
        
        BufferedReader r = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+name_fromfile));
        BufferedWriter w = new BufferedWriter(new FileWriter("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+name_tofile));
        
        w.write("@relation "+name_relation); w.newLine();        

        for(String feature : list_features){
            
              w.write("@attribute "+feature+" "+attributetypes.get(feature)); w.newLine();
        }

        w.write("@attribute class {red,notred,ambiguousred,ambiguousnotred}"); w.newLine();        
        w.write("@data"); 
        
        String line = null; 
        
        while((line = r.readLine()) != null)
        {
            if(h_qualify(line)){

                if(line.contains("%"))
                {
                    w.newLine();
                    w.write(line);
                    continue;
                }                    

                String[] items = line.split(",");
                StringBuilder sb = new StringBuilder();             

                for(String feature : list_features)
                {
                    if(!(sb.toString().equals("")))
                        sb.append(",");

                    sb.append(h_getfeature(items,feature));
                }

                if(test)                    
                    sb.append(","+items[3]);
                else 
                    sb.append(","+items[3]);

                w.newLine();                  
                w.write(sb.toString());     
            } 
                
        }      
        
        r.close();
        w.close();
        
    }
    
    public boolean h_qualify(String s)
    {
        if(code_condition.equals("")) return true;

        if(code_condition.equals("OnlyAmbiguous"))
        {
            if(s.contains("ambiguous"))
                return true;
            else 
                return false;          
        }

        return true;
    }

    public String h_getfeature(String[] i, String f)
    {
        switch (f)
        {
            case "R":
            return i[0];                        

            case "G":
            return i[1];                        

            case "B":
            return i[2];                        

            case "H":
            return h_hue(i);                        

            case "S":
            return h_sat(i);                        

            case "I":
            return h_inten(i);

            default:
            return "0";
        }

    }

    private String h_hue(String[] i)
    {
        return "0";
    }

    private String h_sat(String[] i)
    {
        return "0";
    }

    private String h_inten(String[] i)
    {
        return "0";
    }
    
}
