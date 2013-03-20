// package imageresearch;

import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Breaker {
    
    ArrayList<String> list_images;    
    ArrayList<String> list_testimages;
    ArrayList<String> list_trainimages;
    
    Breaker(){

        list_images = null;
        list_testimages = null;
        list_trainimages = null;                
    }    

    // Collects all the images in the given folder into lists: list_testimages, and list_trainimages. 

    public void getImagesFromFolder(String nameFolder)
    {
        
        ArrayList<String> list_images = h_getFiles(new File("../"+nameFolder));         
        int no_images = list_images.size();

        System.out.format("%8d - images found in total. \n", no_images);       
                
        int no_testimages = no_images / 6; 
        
        list_trainimages = new ArrayList<String>();
        list_testimages = new ArrayList<String>(); 

        int count_testimages = 0;         
        Random r = new Random();     

        while(count_testimages < no_testimages){
            
            String s = list_images.get(r.nextInt(list_images.size()));            
                
            list_testimages.add(s);         
            list_images.remove(s);
            ++count_testimages;     
        }              

        list_trainimages.addAll(list_images);        
        
        System.out.format("%8d - images choosen for training. \n",list_trainimages.size());
        System.out.format("%8d - images choosen for testing. \n",list_testimages.size());        
    
    }
    
    public void collectPixelsToTrain(String name_file) throws IOException
    {
        
        BufferedImage img, imgl; 
        
        BufferedWriter fout = new BufferedWriter(new FileWriter("../_logs/"+name_file));
        
        long count_pixels = 0;
        
        for( String image : list_trainimages ){
        
            String t1 = image.substring(0,image.lastIndexOf("\\")+1);
            String t2 = image.substring(image.lastIndexOf("\\")+1,image.length());
            
            String IMG = image;
            String IMGL = t1+t2.substring(0,t2.indexOf("."))+"l."+t2.substring(t2.indexOf(".")+1);
            
            ArrayList<ArrayList<Integer>> IMGCLASS = new ArrayList<ArrayList<Integer>>();                                
            
            img = ImageIO.read(new File(IMG)); 
            imgl = ImageIO.read(new File(IMGL)); 
            int[] rgb;

            

            for(int i=0; i< imgl.getHeight(); i++){

                ArrayList row = new ArrayList();                                                     
                for(int j=0; j < imgl.getWidth(); j++){

                        rgb = h_getPixelData(imgl, i, j);                                                   

                        if(h_isRed(rgb)) row.add(0);
                        else if(h_isYellow(rgb)) row.add(1); 
                        else if(h_isBlue(rgb)) row.add(2);
                        else if(h_isGreen(rgb)) row.add(3);
                        else row.add(-1);                  
                }

                IMGCLASS.add(row);                    
            }
            
            int[] argb = {0,0,0};            
            int[] hsi = new int[3]; 
            int[] ycbcr =  new int[3];  
            
            int cn, i=0, j=0, k=0; 

            for(i=0; i< img.getHeight(); i++){
                for(j=0; j < img.getWidth(); j++){
                                      
                    rgb = h_getPixelData(img, i, j);                                                                                                                                         

                    if((IMGCLASS.get(i)).get(j) == -1){ continue; }

                    fout.write(rgb[0]+","+rgb[1]+","+rgb[2]);
                    count_pixels++;
                    
                    if((IMGCLASS.get(i)).get(j) == 0){ fout.write(",red"); fout.newLine(); }
                    else if((IMGCLASS.get(i)).get(j) == 1){ fout.write(",notred"); fout.newLine();}                   
                    else if((IMGCLASS.get(i)).get(j) == 2){ fout.write(",ambiguousred"); fout.newLine();}                   
                    else if((IMGCLASS.get(i)).get(j) == 3){ fout.write(",ambiguousnotred"); fout.newLine();}                                       
                }                
            }                
        
        }
        
        fout.close();
        
        System.out.format("%8d - pixels filed for training. \n",count_pixels);        
    }
    
    public void collectPixelsToTest(String name_file) throws IOException
    {
        
        BufferedImage img, imgl; 
        
        BufferedWriter fout = new BufferedWriter(new FileWriter("../_logs/"+name_file));
        
        long count_pixels = 0;
        
        for( String image : list_testimages ){           
        
            String t1 = image.substring(0,image.lastIndexOf("\\")+1);
            String t2 = image.substring(image.lastIndexOf("\\")+1,image.length());
            
            String IMG = image;
            String IMGL = t1+t2.substring(0,t2.indexOf("."))+"l."+t2.substring(t2.indexOf(".")+1);
            
            ArrayList<ArrayList<Integer>> IMGCLASS = new ArrayList<ArrayList<Integer>>();                                
            
            imgl = ImageIO.read(new File(IMGL)); 
            int[] rgb;

            fout.write("%"+t2+","+imgl.getHeight()+","+imgl.getWidth());
            fout.newLine();

            for(int i=0; i< imgl.getHeight(); i++){

                ArrayList row = new ArrayList();                                                     
                for(int j=0; j < imgl.getWidth(); j++){

                        rgb = h_getPixelData(imgl, i, j);                                                   

                        if(h_isRed(rgb)) row.add(0);
                        else if(h_isYellow(rgb)) row.add(1); 
                        else if(h_isBlue(rgb)) row.add(2);
                        else if(h_isGreen(rgb)) row.add(3); 
                        else row.add(-1);                                        
                }

                IMGCLASS.add(row);                    
            }

            img = ImageIO.read(new File(IMG)); 
           
            int[] argb = {0,0,0};
            
            int[] hsi = new int[3]; 
            int[] ycbcr =  new int[3];  
            
            int cn, i=0, j=0, k=0; 

            for(i=0; i< img.getHeight(); i++){
                for(j=0; j < img.getWidth(); j++){
                                      
                    rgb = h_getPixelData(img, i, j); 

                    // if((IMGCLASS.get(i)).get(j) == -1){ continue; }                                                                                                                      
                    
                    fout.write(rgb[0]+","+rgb[1]+","+rgb[2]);
                    count_pixels++;
                    
                    if((IMGCLASS.get(i)).get(j) == 0){ fout.write(",red"); fout.newLine(); }
                    else if((IMGCLASS.get(i)).get(j) == 1){ fout.write(",notred"); fout.newLine();}                   
                    else if((IMGCLASS.get(i)).get(j) == 2){ fout.write(",ambiguousred"); fout.newLine();}                   
                    else if((IMGCLASS.get(i)).get(j) == 3){ fout.write(",ambiguousnotred"); fout.newLine();}   
                    else if((IMGCLASS.get(i)).get(j) == -1){ fout.write(",notred"); fout.newLine(); }                                                                        
                }                
            }                
        
        }
        
        fout.close();
        
        System.out.format("%8d - pixels filed for testing. \n",count_pixels);        
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
        
        if(rgb[0] > 160) return true;
        else return false;         
        
    }
    
    public boolean h_isBlack(int[] rgb)
    {
        
        if(rgb[0] < 100) return true;
        else  return false;   
        
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
    
    public void show(String message)
    { 

        show(1,message);        
    }

    public void show(int newline, String message)
    {
        
        if(newline == 0){
            System.out.print(message);
        }
        else 
            System.out.println(message);      

    }
    
    public ArrayList<String> h_getFiles(final File folder_files)
    {
        
        ArrayList<String> list_files = new ArrayList<String>();
        
        for(final File tEntry : folder_files.listFiles()){
            
            if(tEntry.isDirectory()){
                
                list_files.addAll(h_getFiles(tEntry));
            }
            
            else if(tEntry.getName().indexOf("l")==-1){  
                
               list_files.add(folder_files.toString()+"\\"+tEntry.getName()); 
               
            }
        }
        
        return list_files;
        
    }
    
    
}

