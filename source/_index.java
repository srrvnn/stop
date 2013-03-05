// package imageresearch;

import java.io.*;

public class _index {

    public static void main(String[] args) throws IOException, FileNotFoundException, Exception {
               
        
        //-- TRAINING MODULE TO BUILD MODELS         
        
        //--------------------------------  
        //-- Break all images to pixels and store a list of all pixels in files.

        Break b = new Break(); 
        
        b.GetImages("_newlabels");
        b.WriteTrainToFile("trainpixels.txt");
        b.WriteTestToFile("testpixels.txt");

        //--------------------------------
        //-- Make training .arff file with all the pixels in the train pixels file.        
        
        Note n = new Note();
        
        String[] f = {"R","G","B"};
        
        n.SetFromFile("trainpixels.txt");
        n.SetToFile("screener.arff");        
        n.SetFeatures(f);        
        n.NoteFeatures("screener");

        n.SetFromFile("trainpixels.txt");
        n.SetToFile("ambiguous.arff");        
        n.SetFeatures(f);   
        n.SetCondition("OnlyAmbiguous");     
        n.NoteFeatures("ambiguous");   

        n.SetFromFile("testpixels.txt");
        n.SetToFile("test.arff");        
        n.SetTest();
        n.SetFeatures(f); 
        n.SetCondition("");     
        n.NoteFeatures("screener");   

        //--------------------------------
        //-- Build the screener model using the .arff file.

        Test t = new Test(); 
        
        t.SetTrainFile("screener.arff","screener");        
        t.SetTestFile("test.arff");        
        t.BuildModel("J48");
        t.Run();

        //--------------------------------
        //-- Cluster all pixels and store them in three files.

        Cluster c = new Cluster(); 

        c.SetSource("ambiguous.arff");
        String[] s = {"clustercenters.txt","aclassifier.txt","bclassifier.txt","cclassifier.txt"};
        c.SetClusters(s);        
        c.Run();

        //--------------------------------
        //-- Make training files with all the points in the three clustered pixels files.

        n.SetFromFile("aclassifier.txt");
        n.SetToFile("aclassifier.arff");
        n.SetFeatures(f);
        n.NoteFeatures("aclassifier");

        n.SetFromFile("bclassifier.txt");
        n.SetToFile("bclassifier.arff");
        n.SetFeatures(f);
        n.NoteFeatures("bclassifier");

        n.SetFromFile("cclassifier.txt");
        n.SetToFile("cclassifier.arff");
        n.SetFeatures(f);
        n.NoteFeatures("cclassifier");        

        //--------------------------------
        //-- Build the classifier models

        t.SetTrainFile("aclassifier.arff","aclassifier");        
        t.BuildModel("J48");

        t.SetTrainFile("bclassifier.arff","bclassifier");        
        t.BuildModel("J48");

        t.SetTrainFile("cclassifier.arff","cclassifier");        
        t.BuildModel("J48");

        //-- TESTING MODULE TO LABEL IMAGES

        //--------------------------------
        //-- Make test file with all the points in the test pixels file.
    
        String[] s2 = {"atest.txt","btest.txt","ctest.txt"};
        c.Assign("screener-results.txt","clustercenters.txt",s2);

        n.SetFromFile("atest.txt");
        n.SetToFile("atest.arff");
        n.SetFeatures(f);
        n.NoteFeatures("atest");

        n.SetFromFile("btest.txt");
        n.SetToFile("btest.arff");
        n.SetFeatures(f);
        n.NoteFeatures("btest");

        n.SetFromFile("ctest.txt");
        n.SetToFile("ctest.arff");
        n.SetFeatures(f);
        n.NoteFeatures("ctest");  

        // t.SetUseModel("screener");        
        // t.WriteResults("screenerresult.txt");        

        Draw d = new Draw();
        d.SetSource("screener-results.txt");
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
