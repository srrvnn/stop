// package imageresearch;

import java.io.*;
import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;

import weka.classifiers.evaluation.NominalPrediction;

public class Tester {
    
    String name_trainfile;
    String name_trainer;
    
    String name_testfile;
    
    String name_classifier;    
    String name_model;
    
    boolean usemodel;
    
    Tester()
    {
        usemodel = false; 
        name_classifier = new String("");
    }
            
            
    public void setTrainFile(String s, String str)
    {
        name_trainfile = new String(s);
        name_trainer = new String(str);
        name_model = new String(str);
    }
    
    public void setTestFile(String s)
    {
        name_testfile = new String(s);
    }
    
    public void setUseModel(String s)
    {        
        name_model = new String(s);
    }
    
    public void buildModel(String s) throws FileNotFoundException, IOException, Exception{

            BufferedReader f = new BufferedReader(new FileReader("../_logs/"+name_trainfile));
            Instances d = new Instances(f);
            d.setClassIndex(d.numAttributes()-1);
            f.close(); 

            Classifier cl = (Classifier) new NaiveBayes();

            if(s.equals("J48"))
                cl = (Classifier) new J48();

            cl.buildClassifier(d);
            
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../_logs/"+name_model+".model"));
            
            oos.writeObject(cl);
            oos.flush();
            oos.close();  
    }

    public void runTest() throws FileNotFoundException, IOException, Exception{

        BufferedReader f = new BufferedReader(new FileReader("../_logs/"+name_trainfile));
        Instances d = new Instances(f);
        d.setClassIndex(d.numAttributes()-1);
        f.close(); 

        Classifier cl = (Classifier) weka.core.SerializationHelper.read("../_logs/"+name_model+".model");
        Evaluation e = new Evaluation(d);

        BufferedReader tr = new BufferedReader(new FileReader("../_logs/"+name_testfile));
        Instances td = new Instances(tr);
        td.setClassIndex(td.numAttributes()-1);
        tr.close();          

        e.evaluateModel(cl, td);  

        // show("Evaluation Complete.");
        // show(e.toSummaryString());

        BufferedWriter w = new BufferedWriter(new FileWriter("../_logs/"+name_model+"-results.txt"));
        f = new BufferedReader(new FileReader("../_logs/"+name_testfile));

        String line; 
        int counter = 0;

        do{
            line = f.readLine();
        }while(!line.contains("class"));

        String[] classes = line.substring(line.indexOf("{")+1,line.indexOf("}")).split(",");

        line = f.readLine();

        int c1 = 0;

        while((line = f.readLine()) != null){

            c1++;

            if(line.contains("%"))
            {
                w.write(line); 
                w.newLine();
                continue;
            }                    

            try{
                NominalPrediction n = (NominalPrediction) e.predictions().elementAt(counter++);
                w.write(line.substring(0,line.lastIndexOf(",")+1));
                w.write(classes[(int)n.predicted()]);
                w.newLine();
            }
            catch(Exception ex){
                System.out.println(line);
            }
        }
                
        w.close();
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
