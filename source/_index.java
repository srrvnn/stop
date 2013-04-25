// package imageresearch;

import java.io.*;
import java.util.*;

public class _index {

    static String[] RGB = {"R","G","B"};
    static String[] RGBHSI = {"R","G","B","H","S","I"};

    static String[] c = {"red","notred","ambiguous"};
    static String[] call = {"red","notred","ambiguousred","ambiguousnotred"};

    public static void main(String[] args) throws IOException, FileNotFoundException, Exception {

        //-- TESTING BED

        // Clusterer tclusterer = new Clusterer();
        // tclusterer.assign("testpi.txt","clustercenters.txt","cluster-results.txt");

        // System.exit(1);

        //-- CLEAN ALL FILES THAT WERE CREATED IN THE LAST RUN

        Cleaner ocleaner = new Cleaner();

        ocleaner.deleteFilesFromFolder("_bin");               
        ocleaner.deleteFilesFromFolder("_logs");
        ocleaner.deleteFilesFromFolder("_predictions");

        //-- TRAINING MODULE TO BUILD MODELS         
        
        //--------------------------------  
        //-- Break all images to pixels and store a list of all pixels in files.

        Breaker obreaker = new Breaker(); 
        
        obreaker.getImagesFromFolder("_labels");
        obreaker.collectPixelsToTrain("trainpixels.txt");
        obreaker.collectPixelsToTest("testpixels.txt");    
  
        //--------------------------------
        //-- Make training .arff file with all the pixels in the train pixels file.        
        
        Noter onoter = new Noter();
        
        onoter.SetFromFile("trainpixels.txt");
        onoter.SetToFile("screener.arff");        
        onoter.SetFeatures(RGB);      
        onoter.setClasses(c); 
        onoter.setCondition("NoNotRed"); 
        onoter.setModify(true);
        onoter.noteFeatures("screener");        

        // onoter.SetFromFile("trainpixels.txt");
        // onoter.SetToFile("ambiguous.arff");        
        // onoter.SetFeatures(RGB);   
        // onoter.setClasses(call);
        // onoter.setModify(false); 
        // onoter.SetCondition("OnlyAmbiguous");     
        // onoter.noteFeatures("ambiguous");   

        // onoter.SetFromFile("testpixels.txt");
        // onoter.SetToFile("test.arff");        
        // onoter.SetFeatures(RGB);  
        // onoter.setClasses(c); 
        // onoter.setModify(true);
        // onoter.SetCondition("");     
        // onoter.noteFeatures("screener");        
        
        //--------------------------------
        //-- Build the screener model using the .arff file.

        // Tester otester = new Tester(); 
        
        // otester.setTrainFile("screener.arff","screener");        
        // otester.setTestFile("test.arff");        
        // otester.buildModel("J48");
        // otester.runTest();        

        // Draw d = new Draw();

        // d.SetSource("screener-results.txt");
        // d.RebuildImageswithA();        

        //--------------------------------
        //-- Cluster all pixels and store them in as many files.

        int k = 100;

        Clusterer oclusterer = new Clusterer(); 

        String[] train_clusters = {"clusters-train",".txt"};
        String[] test_clusters = {"clusters-test",".txt"};

        oclusterer.setSource("screener.arff");               
        oclusterer.setNumber(k);
        oclusterer.setResults(train_clusters,"clustercenters.txt",test_clusters);        
        oclusterer.run();          

        oclusterer.assign("testpixels.txt","clustercenters.txt","cluster-assignments.txt");

        Analyser oanalyser = new Analyser();        
        ArrayList<String> classes = new ArrayList<String>();

        oanalyser.setNumber(k);
        oanalyser.setFile(train_clusters);

        oanalyser.run();        

        // oanalyser.substituteClasses("cluster-assignments.txt","cluster-results.txt");
        // oanalyser.compareClasses("testpixels.txt","cluster-results.txt");    

        // oanalyser.printRadius("clustercenters.txt");    

        oanalyser.fileLogs();

        ColorCube ocube =  new ColorCube();
        ocube.draw(k,oanalyser.getClasses());

        // ColorCubeExample ocube = new ColorCubeExample(k);
        // ocube.draw(classes);        

        //--------------------------------
        //-- Make three seperate training files with the points the three clustered pixels files.        

        // onoter.SetFromFile("aclassifier.txt");
        // onoter.SetToFile("aclassifier.arff");
        // onoter.SetFeatures(RGB);
        // onoter.noteFeatures("aclassifier");

        // onoter.SetFromFile("bclassifier.txt");
        // onoter.SetToFile("bclassifier.arff");
        // onoter.SetFeatures(RGB);
        // onoter.noteFeatures("bclassifier");

        // onoter.SetFromFile("cclassifier.txt");
        // onoter.SetToFile("cclassifier.arff");
        // onoter.SetFeatures(RGB);
        // onoter.noteFeatures("cclassifier");                           

        //-- TESTING MODULE TO LABEL IMAGES

        //--------------------------------
        //-- Make test file with all the points in the test pixels file.           

        // Tester t =  new Tester();   
    
        // String[] s2 = {"atest.txt","btest.txt","ctest.txt"};

        // oclusterer.Assign("screener-results.txt","clustercenters.txt","cluster-results.txt",s2);

        // onoter.SetFromFile("atest.txt");
        // onoter.SetToFile("atest.arff");
        // onoter.SetFeatures(RGB);
        // onoter.noteFeatures("atest");

        // otester.setTrainFile("aclassifier.arff","aclassifier");        
        // otester.buildModel("J48"); 
        // otester.setTestFile("atest.arff");
        // otester.runTest();

        // onoter.SetFromFile("btest.txt");
        // onoter.SetToFile("btest.arff");
        // onoter.SetFeatures(RGB);
        // onoter.noteFeatures("btest");

        // otester.setTrainFile("bclassifier.arff","bclassifier");        
        // otester.buildModel("J48");
        // otester.setTestFile("btest.arff");
        // otester.runTest();

        // onoter.SetFromFile("ctest.txt");
        // onoter.SetToFile("ctest.arff");
        // onoter.SetFeatures(RGB);
        // onoter.noteFeatures("ctest");  

        // otester.setTrainFile("cclassifier.arff","cclassifier");        
        // otester.buildModel("J48");       
        // otester.setTestFile("ctest.arff");
        // otester.runTest();

        // // Draw d = new Draw();

        // String[] results = {"aclassifier-results.txt","bclassifier-results.txt","cclassifier-results.txt","screener-results.txt"};
        // d.CompileResults("final-results.txt","pointpositions.txt",results);

        // d.SetSource("screener-results.txt");
        // d.RebuildImageswithA();
        // d.buildComparison();

        // d.SetSource("final-results.txt");
        // d.RebuildImages();

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
    
    
}
