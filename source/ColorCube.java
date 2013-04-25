// package plot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.math.plot.Plot3DPanel;

public class ColorCube {

	ArrayList<double[][]> clustersArrayList = new ArrayList<double[][]>();
	Plot3DPanel plot = new Plot3DPanel();

	ArrayList<String> classes; 
	
	public ColorCube(){
		
	}

	public void draw(int k, ArrayList<String> c) {

		System.out.format("%8d - Drawing clusters...",k);

		classes = c;

		for(int i = 0; i < k; i++){			

				double[][] pc1 = ColorCube.getPointsFromClusterFile("clusters-train" + i +".txt");			
				add(pc1);						
		}				
		
		boolean showFrame = true;
		show(showFrame);
	}
	
	private void addCubeFrame()
	{
		double[][] bottom = { {0,0,0}, {255,0,0}, {255,255,0}, {0,255,0}, {0,0,0}};
		double[][] top = { {0,0,255}, {255,0,255}, {255,255,255}, {0,255,255}, {0,0,255}};
		double[][] front = { {255,0,0}, {255,255,0}, {255,255,255}, {255,255,0}, {255,0,0}};
		double[][] back = { {0,0,0}, {0,255,0}, {0,255,255}, {0,0,255}, {0,0,0}};
		double[][] left = { {0,0,0}, {255,0,0}, {255,0,255}, {0,0,255}, {0,0,0}};
				
		
		//add cube to the draw set
		plot.addLinePlot("",Color.red, bottom);
		plot.addLinePlot("",Color.red, top);
		plot.addLinePlot("",Color.red, front);
		plot.addLinePlot("",Color.red, back);
		plot.addLinePlot("",Color.red, left);
	}
	
	
	public void show(boolean showFrame) {
		
		if(showFrame)
			addCubeFrame();
		
		plot.addLegend("SOUTH");
		
		// add clusters to draw set
		for(int i = 0; i < clustersArrayList.size(); ++i)
		{
			plot.addScatterPlot(i+"-"+classes.get(i), clustersArrayList.get(i));
		}
		
		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
	
	public void add(double[][] points)
	{
		clustersArrayList.add(points);
	}
	
	
	public static double[][] getPointsFromClusterFile(String filename)
	{

		// System.out.println("Cluster Filing Check");

		BufferedReader br;
		ArrayList<Point3D> pointsArrayList = new ArrayList<Point3D>();
		try{
			 br = new BufferedReader(new FileReader("../_logs/"+filename));
			 String line;			 
			
			 while((line = br.readLine()) != null)
			 {
				
				//get the last part substring
				 String[] tokens = line.split(","); 
				
				 // parse r, g,b 
				 int r = Integer.parseInt(tokens[0]);
				 int g = Integer.parseInt(tokens[1]);
				 int b = Integer.parseInt(tokens[2]);
				 // add to arraylist
				 pointsArrayList.add(new Point3D(r,g,b));
				
			 }		
			 br.close();

		}
		catch(Exception e)
		{
			System.err.print("../_logs/" + filename + " file not found\n");
			System.exit(-1);
		}
		
		double[][] points = new double[pointsArrayList.size()][3];
		for(int i = 0; i < pointsArrayList.size(); ++i)
		{
			points[i][0] = pointsArrayList.get(i).x;
			points[i][1] = pointsArrayList.get(i).y;
			points[i][2] = pointsArrayList.get(i).z;
		}
		return points;
	}
	

}
