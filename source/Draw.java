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

	void RebuildImages() throws FileNotFoundException, IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+name_source));
		String line;

		// do{
  //      		line = br.readLine();
  //      	}while(!line.equals("@data"));

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

       		ImageIO.write(output, "png", new File("C:\\Users\\esgee\\Desktop\\project-stop\\_predictions/"+"1O-"+details_image[0].substring(1)));
       	}

       	
	}

    
}
