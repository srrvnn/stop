import java.io.*;
import java.util.*;

public class Noter {
    
    String name_fromfile;
    String name_tofile;
    
    String code_condition; 
    String[] list_features;
    String[] list_classes;

    boolean test;
    
    private HashMap attributetypes;
    
    Noter()
    {
        name_fromfile = "";
        name_tofile = "";
        code_condition = "";          
        list_features = null;
        list_classes = null;
        test = false;    


        
        attributetypes = new HashMap();

        attributetypes.put("R","numeric");
        attributetypes.put("G","numeric");
        attributetypes.put("B","numeric");
        attributetypes.put("H","numeric");
        attributetypes.put("S","numeric");
        attributetypes.put("I","numeric");
                       
    }
    
    public void SetFromFile(String s)
    {
        name_fromfile = new String(s);
    }
    
    public void SetToFile(String s)
    {
        name_tofile = new String(s);
    }
    
    public void setCondition(String s)
    {
        code_condition = new String(s);
    }
    
    public void SetFeatures(String[] s)
    {
        list_features = s; 
    }

    public void setModify(boolean b)
    {
        test = b;
    } 

    public void setClasses(String[] s){

        list_classes = s;
    }  
    
    
    public void noteFeatures(String name_relation) throws FileNotFoundException, IOException
    {
        
        BufferedReader r = new BufferedReader(new FileReader("../_logs/"+name_fromfile));
        BufferedWriter w = new BufferedWriter(new FileWriter("../_logs/"+name_tofile));

        int count = 0;
        
        w.write("@relation "+name_relation); w.newLine();        

        for(String feature : list_features){
            
              w.write("@attribute "+feature+" "+attributetypes.get(feature)); w.newLine();
        }

        w.write("@attribute class {");

        boolean firstpassed = false;

        for(String s : list_classes){

            if(!firstpassed){

                firstpassed = true;                 
            }
            else
                w.write(",");    

            w.write(s);      
        }

        w.write("}"); w.newLine();        
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

                if(test){

                    if(items[items.length-1].contains("ambiguous"))
                        sb.append(","+"ambiguous");

                    else 
                        sb.append(","+items[items.length-1]);
                }                    
                    
                else 
                    sb.append(","+items[items.length-1]);

                w.newLine();                  
                w.write(sb.toString());     

                count++;

            } 
                
        }      
        
        r.close();
        w.close();

        System.out.format("%8d - tuples in %s. \n",count,name_tofile);
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

        if(code_condition.equals("NoNotRed"))
        {
            if(s.contains(",notred"))
                return false; 
            else 
                return true;
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
        int r = Integer.parseInt(i[0]);
        int g = Integer.parseInt(i[1]);
        int b = Integer.parseInt(i[2]);

        int min, max, delMax; 
        
        if (r > g) { min = g; max = r; }
        else { min = r; max = g; }
        if (b > max) max = b;
        if (b < min) min = b;
                                
        delMax = max - min;
     
        float H = 0;       
        float L, V;      
          
        if ( delMax == 0 ) { H = 0;}
        else {                                               
            if ( r == max ) 
                H = (((g - b)/(float)delMax)%6)*60;
            else if ( g == max ) 
                H = ( 2 +  (b - r)/(float)delMax)*60;
            else if ( b == max ) 
                H = ( 4 +  (r - g)/(float)delMax)*60;   
        }

        if(H<0) H = 360+H;

        return Integer.toString((int)H);
    }

    private String h_sat(String[] i)
    {
        int r = Integer.parseInt(i[0]);
        int g = Integer.parseInt(i[1]);
        int b = Integer.parseInt(i[2]);

        int min, max, delMax; 

        float I = (r+g+b)/3;  

        if (r > g) { min = g; max = r; }
        else { min = r; max = g; }
        if (b > max) max = b;
        if (b < min) min = b;
                                
        delMax = max - min;
     
        float H = 0, S;

        if ( delMax == 0 ) { S = 0; }
        else {                                   
            S = 1 - (min/(float)I);           
        }

        S = S * 100; 
        
       return Integer.toString((int)S);      
    }

    private String h_inten(String[] i)
    {
        int r = Integer.parseInt(i[0]);
        int g = Integer.parseInt(i[1]);
        int b = Integer.parseInt(i[2]);        

        float I = (r+g+b)/3;   
        
        return Integer.toString((int)I);          
    }
}
