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

	void RebuildImageswithA() throws FileNotFoundException, IOException {



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
       			if(line.contains("ambiguous"))
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

      void RebuildImages() throws FileNotFoundException, IOException {

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


      public void CompileResults(String fresults, String ppoints, String[] allresults) throws FileNotFoundException, IOException {

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

      public void buildComparison() throws IOException {

            BufferedReader br = new BufferedReader(new FileReader("../_logs/"+name_source));
            String line;      

            while(true)
            {                 
                  if((line = br.readLine()) == null) break;

                  String[] details_image = line.split(",");

                  String imgs = details_image[0].substring(1);
                  String imgl = imgs.substring(0,imgs.indexOf("."))+"l."+imgs.substring(imgs.indexOf(".")+1);

                  BufferedImage img = ImageIO.read(new File("../_labels/"+imgl));                   

                  BufferedImage predictions = new BufferedImage(Integer.parseInt(details_image[2]),Integer.parseInt(details_image[1]),BufferedImage.TYPE_INT_RGB);
                  BufferedImage errors = new BufferedImage(Integer.parseInt(details_image[2]),Integer.parseInt(details_image[1]),BufferedImage.TYPE_INT_RGB);

                  int height = Integer.parseInt(details_image[1]);
                  int width = Integer.parseInt(details_image[2]);                     
                  
                  for(int counter = 0; counter < height * width; counter++)
                  {
                        try{

                              line = br.readLine();

                              if(h_isWhite(h_getPixelData(img,counter/width,counter%width))) {

                                    predictions.setRGB(counter%width, counter/width, 16777215);      
                                    errors.setRGB(counter%width, counter/width, 16777215);
                              }

                              else {

                                    if(line.contains("ambiguousred")){

                                          predictions.setRGB(counter%width, counter/width, 11119017);      

                                          if(!h_isBlue(h_getPixelData(img,counter/width,counter%width))) 
                                              errors.setRGB(counter%width, counter/width, 15597806);  

                                          else errors.setRGB(counter%width, counter/width, 16777215);  
                                    }
                                    

                                    else if(line.contains("ambiguousnotred")){

                                          predictions.setRGB(counter%width, counter/width, 11119017); 

                                          if(!h_isGreen(h_getPixelData(img,counter/width,counter%width))) 
                                              errors.setRGB(counter%width, counter/width, 15597806);

                                          else errors.setRGB(counter%width, counter/width, 16777215); 
                                    }
                                          

                                    else if(line.contains("notred")){

                                          predictions.setRGB(counter%width, counter/width, 0); 

                                          if(!h_isYellow(h_getPixelData(img,counter/width,counter%width))) 
                                              errors.setRGB(counter%width, counter/width, 15597806); 

                                          else errors.setRGB(counter%width, counter/width, 16777215);
                                    }
                                          

                                    else if(line.contains("red")){

                                          predictions.setRGB(counter%width, counter/width, 16711680);            

                                          if(!h_isRed(h_getPixelData(img,counter/width,counter%width))) 
                                              errors.setRGB(counter%width, counter/width, 15597806); 

                                          else errors.setRGB(counter%width, counter/width, 16777215);
                                    }                                          
                              }
                        
                        }   
                        catch(Exception e) {}
                  }

                  ImageIO.write(predictions, "png", new File("../_predictions/"+"3O-"+details_image[0].substring(1)));
                  ImageIO.write(errors, "png", new File("../_predictions/"+"4O-"+details_image[0].substring(1)));
            }     
            
            
      }     

      private int[] h_getPixelData(BufferedImage img, int x, int y) 
      {

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

      public boolean h_isWhite(int[] rgb)
      {

            if((rgb[0] > 250) && (rgb[1] > 250) && (rgb[2] > 250)) return true;
            else return false;         
      }

      public boolean h_isRed(int[] rgb)
      {       
            if(rgb[0] == 255 && rgb[1] == 0 && rgb[2] == 0)      
                  return true;

            return false;
      }

      private boolean h_isYellow(int[] rgb)
      {

            if(rgb[0] == 255 && rgb[1] == 255 && rgb[2] == 0)      
                  return true;

            return false;

      }

      private boolean h_isBlue(int[] rgb)
      {

            if(rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 255)      
                  return true;

            return false;

      }

      private boolean h_isGreen(int[] rgb)
      {

            if(rgb[0] == 0 && rgb[1] == 255 && rgb[2] == 0)      
                  return true;

            return false;

      }
}
