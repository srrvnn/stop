// package imageresearch;

import java.io.*;

public class _index {

    public static void main(String[] args) throws IOException, FileNotFoundException, Exception {

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
        
        String[] f = {"R","G","B"};
        String[] features = {"R","G","B","H","S","I"};

        String[] c = {"red","notred","ambiguous"};
        String[] call = {"red","notred","ambiguousred","ambiguousnotred"};
        
        onoter.SetFromFile("trainpixels.txt");
        onoter.SetToFile("screener.arff");        
        onoter.SetFeatures(f);      
        onoter.setClasses(c);   
        onoter.setModify(true);
        onoter.noteFeatures("screener");        

        onoter.SetFromFile("trainpixels.txt");
        onoter.SetToFile("ambiguous.arff");        
        onoter.SetFeatures(f);   
        onoter.setClasses(call);
        onoter.setModify(false); 
        onoter.SetCondition("OnlyAmbiguous");     
        onoter.noteFeatures("ambiguous");   

        onoter.SetFromFile("testpixels.txt");
        onoter.SetToFile("test.arff");        
        onoter.SetFeatures(f); 
        onoter.setClasses(c); 
        onoter.setModify(true);
        onoter.SetCondition("");     
        onoter.noteFeatures("screener");        
        
        //--------------------------------
        //-- Build the screener model using the .arff file.

        Tester otester = new Tester(); 
        
        otester.setTrainFile("screener.arff","screener");        
        otester.setTestFile("test.arff");        
        otester.buildModel("J48");
        otester.runTest();        

        Draw d = new Draw();

        d.SetSource("screener-results.txt");
        d.RebuildImageswithA();

        System.exit(1);

        //--------------------------------
        //-- Cluster all pixels and store them in three files.

        Clusterer oclusterer = new Clusterer(); 

        String[] clusterresults = {"clustercenters.txt","aclassifier.txt","bclassifier.txt","cclassifier.txt"};

        oclusterer.setSource("ambiguous.arff");        
        oclusterer.setClusters(clusterresults);        
        oclusterer.run();          

        //--------------------------------
        //-- Make training files with all the points in the three clustered pixels files.

        

        onoter.SetFromFile("aclassifier.txt");
        onoter.SetToFile("aclassifier.arff");
        onoter.SetFeatures(f);
        onoter.noteFeatures("aclassifier");

        onoter.SetFromFile("bclassifier.txt");
        onoter.SetToFile("bclassifier.arff");
        onoter.SetFeatures(f);
        onoter.noteFeatures("bclassifier");

        onoter.SetFromFile("cclassifier.txt");
        onoter.SetToFile("cclassifier.arff");
        onoter.SetFeatures(f);
        onoter.noteFeatures("cclassifier");                           

        //-- TESTING MODULE TO LABEL IMAGES

        //--------------------------------
        //-- Make test file with all the points in the test pixels file.           

        Tester t =  new Tester();   
    
        String[] s2 = {"atest.txt","btest.txt","ctest.txt"};

        oclusterer.Assign("screener-results.txt","clustercenters.txt","pointpositions.txt",s2);

        onoter.SetFromFile("atest.txt");
        onoter.SetToFile("atest.arff");
        onoter.SetFeatures(f);
        onoter.noteFeatures("atest");

        otester.setTrainFile("aclassifier.arff","aclassifier");        
        otester.buildModel("J48"); 
        otester.setTestFile("atest.arff");
        otester.runTest();

        onoter.SetFromFile("btest.txt");
        onoter.SetToFile("btest.arff");
        onoter.SetFeatures(f);
        onoter.noteFeatures("btest");

        otester.setTrainFile("bclassifier.arff","bclassifier");        
        otester.buildModel("J48");
        otester.setTestFile("btest.arff");
        otester.runTest();

        onoter.SetFromFile("ctest.txt");
        onoter.SetToFile("ctest.arff");
        onoter.SetFeatures(f);
        onoter.noteFeatures("ctest");  

        otester.setTrainFile("cclassifier.arff","cclassifier");        
        otester.buildModel("J48");       
        otester.setTestFile("ctest.arff");
        otester.runTest();

        // Draw d = new Draw();

        String[] results = {"aclassifier-results.txt","bclassifier-results.txt","cclassifier-results.txt","screener-results.txt"};
        d.CompileResults("final-results.txt","pointpositions.txt",results);

        d.SetSource("screener-results.txt");
        d.RebuildImageswithA();
        d.buildComparison();

        d.SetSource("final-results.txt");
        d.RebuildImages();

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
