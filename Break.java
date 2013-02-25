// package imageresearch;

import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Break {
    
    ArrayList<String> list_images;    
    ArrayList<String> list_testimages;
    ArrayList<String> list_trainimages;
    
    Break()
    {
        list_images = null;
        list_testimages = null;
        list_trainimages = null;                
    }
    
    public void GetImages(String folder_images)
    {
        
        show(0,"Collecting all images. . .");        
        
         ArrayList<String> list_images = h_getFiles(new File("_labels"));         
         int no_images = list_images.size();
                    
         show(". . .Done.");        
        
         int no_testimages = no_images / 6; 
        
         list_trainimages = new ArrayList<String>();
         list_testimages = new ArrayList<String>(); 

         int count_testimages = 0;         
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

        
    //-- 1.6 All other images will be used to build classifiers.
        
        list_trainimages.addAll(list_images);        
        
        show("training images: "+list_trainimages.size());
        show("test images: "+list_testimages.size());        
        
    }
    
    public void WriteTrainToFile(String name_file) throws IOException{
        
        BufferedImage img, imgl; 
        
        BufferedWriter fout = new BufferedWriter(new FileWriter(name_file));
        
        long count_pixels = 0;
        
        for( String image : list_trainimages ){           
        
            String t1 = image.substring(0,image.lastIndexOf("\\")+1);
            String t2 = image.substring(image.lastIndexOf("\\")+1,image.length());
            
            String IMG = image;
            String IMGL = t1+"l-"+t2;
            
            ArrayList<ArrayList<Integer>> IMGCLASS = new ArrayList<ArrayList<Integer>>();                                
            
            imgl = ImageIO.read(new File(IMGL)); 
            int[] rgb;

            
            for(int i=0; i< imgl.getHeight(); i++){

                ArrayList row = new ArrayList();                                                     
                for(int j=0; j < imgl.getWidth(); j++){

                        rgb = h_getPixelData(imgl, i, j);                                                   

                        if(h_isWhite(rgb)) row.add(1);
                        else if(h_isBlack(rgb)) row.add(-1); 
                        else row.add(0);                                    
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
                    
                    fout.write(rgb[0]+","+rgb[1]+","+rgb[2]);
                    count_pixels++;
                    
                    if((IMGCLASS.get(i)).get(j) == 1){ fout.write(",red"); fout.newLine(); }
                    else if((IMGCLASS.get(i)).get(j) == -1){ fout.write(",notred"); fout.newLine();}                   
                    else{ fout.write(",ambiguousred"); fout.newLine(); }                                           
                }                
            }                
        
        }
        
        fout.close();
        
        show(count_pixels+" train pixels filed.");
        
    }
    
    public void WriteTestToFile(String name_file) throws IOException{
        
        BufferedImage img, imgl; 
        
        BufferedWriter fout = new BufferedWriter(new FileWriter(name_file));
        
        long count_pixels = 0;
        
        for( String image : list_testimages ){           
        
            String t1 = image.substring(0,image.lastIndexOf("\\")+1);
            String t2 = image.substring(image.lastIndexOf("\\")+1,image.length());
            
            String IMG = image;
            String IMGL = t1+"l-"+t2;
            
            ArrayList<ArrayList<Integer>> IMGCLASS = new ArrayList<ArrayList<Integer>>();                                
            
            imgl = ImageIO.read(new File(IMGL)); 
            int[] rgb;

            
            for(int i=0; i< imgl.getHeight(); i++){

                ArrayList row = new ArrayList();                                                     
                for(int j=0; j < imgl.getWidth(); j++){

                        rgb = h_getPixelData(imgl, i, j);                                                   

                        if(h_isWhite(rgb)) row.add(1);
                        else if(h_isBlack(rgb)) row.add(-1); 
                        else row.add(0);                                    
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
                    
                    fout.write(rgb[0]+","+rgb[1]+","+rgb[2]);
                    count_pixels++;
                    
                    if((IMGCLASS.get(i)).get(j) == 1){ fout.write(",red"); fout.newLine(); }
                    else if((IMGCLASS.get(i)).get(j) == -1){ fout.write(",notred"); fout.newLine();}                   
                    else{ fout.write(",ambiguousred"); fout.newLine(); }       
                                    
                }                
            }                
        
        }
        
        fout.close();
        
        show(count_pixels+" test pixels filed.");
        
    }

    public static void main(String[] args)
    {
        System.out.println("Hey!");        
    }
    
    private static int[] h_getPixelData(BufferedImage img, int x, int y) {
        
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
    
    public static boolean h_isWhite(int[] rgb){
        
        if(rgb[0] > 160) return true;
        else return false;
          
        
    }
    
    public static boolean h_isBlack(int[] rgb){
        
        if(rgb[0] < 100) return true;
        else  return false;
        
        
    }
    
    public static boolean h_isRed(int[] rgb){
        
        if(rgb[0] >= 120 && rgb[1] <=30 && rgb[2] <=30) return true; 
        else return false; 
              
    }
    
    private static void show(String message){
        
        show(1,message);        
    }

    private static void show(int newline, String message){
        
        if(newline == 0){
            System.out.print(message);
        }
        else 
            System.out.println(message);
        
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
