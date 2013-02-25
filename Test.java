// package imageresearch;

import java.io.*;
import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;

public class Test {
    
    String name_trainfile;
    String name_trainer;
    
    String name_testfile;
    
    String name_classifier;    
    String name_model;
    
    boolean usemodel;
    
    Test()
    {
        usemodel = false; 
        name_classifier = new String("");
    }
            
            
    public void SetTrainFile(String s, String str)
    {
        name_trainfile = new String(s);
        name_trainer = new String(str);
    }
    
    public void SetTestFile(String s)
    {
        name_testfile = new String(s);
    }
    
    // public void SetUseModel(String s)
    // {
    //     usemodel = true; 
    //     name_model = new String(s);
    // }
    
    public void BuildModel(String s) throws FileNotFoundException, IOException, Exception
    {

            BufferedReader f = new BufferedReader(new FileReader(name_trainfile));
            Instances d = new Instances(f);
            d.setClassIndex(d.numAttributes()-1);
            f.close(); 

            Classifier cl = (Classifier) new NaiveBayes();

            if(s.equals("J48"))
                cl = (Classifier) new J48();

            cl.buildClassifier(d);
            
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(name_trainer+".model"));
            
            oos.writeObject(cl);
            oos.flush();
            oos.close();         

    }

    public void Run() throws FileNotFoundException, IOException, Exception
    {

            BufferedReader f = new BufferedReader(new FileReader(name_trainfile));
            Instances d = new Instances(f);
            d.setClassIndex(d.numAttributes()-1);
            f.close(); 

            Classifier cl = (Classifier) weka.core.SerializationHelper.read(name_trainer+".model");
            Evaluation e = new Evaluation(d);

            BufferedReader tr = new BufferedReader(new FileReader(name_testfile));
            Instances td = new Instances(tr);
            td.setClassIndex(td.numAttributes()-1);
            tr.close();          

            e.evaluateModel(cl, td);            
            show("Evaluation Complete.");
            show(e.toSummaryString());
        
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
