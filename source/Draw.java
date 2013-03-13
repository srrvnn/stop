//package imageresearch;

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Draw {

	String name_source;

	// static Hashtable color_map;
	// color_map.add("notred","0");
	// color_map.add("red","16711680");
	// color_map.add("ambiguous","11119017");


	void SetSource(String s)
	{
		name_source = new String(s);
	}

	void RebuildImageswithA() throws FileNotFoundException, IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("../_logs/"+name_source));
		String line;	

       	while(true)
       	{       		
       		if((line = br.readLine()) == null) break;

       		String[] details_image = line.split(",");
       		BufferedImage output = new BufferedImage(Integer.parseInt(details_image[2]),Integer.parseInt(details_image[1]),BufferedImage.TYPE_INT_RGB);

       		int height = Integer.parseInt(details_image[1]);
       		int width = Integer.parseInt(details_image[2]);       		  
       		
       		for(int counter = 0; counter < height * width; counter++)
       		{
       			try{
       				line = br.readLine();
       			if(line.contains("ambiguousred"))
       				output.setRGB(counter%width, counter/width, 11119017);	
       			else if(line.contains("ambiguousnotred"))
       				output.setRGB(counter%width, counter/width, 11119017);	
       			else if(line.contains("notred"))
       				output.setRGB(counter%width, counter/width, 0);	
       			else if(line.contains("red"))
       				output.setRGB(counter%width, counter/width, 16711680);		
       			}   
       			catch(Exception e)    			
       			{}
       		}

       		ImageIO.write(output, "png", new File("../_predictions/"+"1O-"+details_image[0].substring(1)));
       	}

       	
	}

      void RebuildImages() throws FileNotFoundException, IOException
      {
            BufferedReader br = new BufferedReader(new FileReader("../_logs/"+name_source));
            String line;      

            while(true)
            {                 
                  if((line = br.readLine()) == null) break;

                  String[] details_image = line.split(",");
                  BufferedImage output = new BufferedImage(Integer.parseInt(details_image[2]),Integer.parseInt(details_image[1]),BufferedImage.TYPE_INT_RGB);

                  int height = Integer.parseInt(details_image[1]);
                  int width = Integer.parseInt(details_image[2]);                     
                  
                  for(int counter = 0; counter < height * width; counter++)
                  {
                        try{
                              line = br.readLine();
                        if(line.contains("ambiguousred"))
                              output.setRGB(counter%width, counter/width, 16711680);      
                        else if(line.contains("ambiguousnotred"))
                              output.setRGB(counter%width, counter/width, 0);      
                        else if(line.contains("notred"))
                              output.setRGB(counter%width, counter/width, 0); 
                        else if(line.contains("red"))
                              output.setRGB(counter%width, counter/width, 16711680);            
                        }   
                        catch(Exception e)                  
                        {}
                  }

                  ImageIO.write(output, "png", new File("../_predictions/"+"2O-"+details_image[0].substring(1)));
            }

            
      }




      public void CompileResults(String fresults, String ppoints, String[] allresults) throws FileNotFoundException, IOException
      {

            BufferedReader reader1 = new BufferedReader(new FileReader("../_logs/"+ppoints));
            BufferedReader[] readern = { new BufferedReader(new FileReader("../_logs/"+allresults[0])), 
                                         new BufferedReader(new FileReader("../_logs/"+allresults[1])), 
                                         new BufferedReader(new FileReader("../_logs/"+allresults[2])), 
                                         new BufferedReader(new FileReader("../_logs/"+allresults[3])), };

            BufferedWriter writer1 = new BufferedWriter(new FileWriter("../_logs/"+fresults));
            String buffer1; 

            writer1.write(readern[3].readLine());
            writer1.newLine();

            while((buffer1 = reader1.readLine()) != null)
            {
                  try{                       
                        String towrite = new String(readern[Integer.parseInt(buffer1)].readLine());

                        writer1.write(towrite);
                        writer1.newLine();                     

                        if(towrite.indexOf("%") != -1)
                        {
                              writer1.write(readern[Integer.parseInt(buffer1)].readLine());
                              writer1.newLine();
                        }

                        if(!buffer1.equals("3"))
                        {
                              String toescape = new String(readern[3].readLine());
                              if(toescape.indexOf("%") != -1)
                              {
                                    writer1.write(toescape);
                                    writer1.newLine();
                              }
                                    

                        }
                              
                  }

                  catch(Exception e)                  
                  {
                        System.out.println(buffer1);
                  }
            }

            writer1.close();

      }    
}
