import java.io.*;


public class Trial
{
	public static void main(String[] args) throws FileNotFoundException, IOException
	{

			String[] string_points = point.split(",");

			int[] points = {0,0,0};

			points[0] = Integer.parseInt(string_points[0]);
			points[1] = Integer.parseInt(string_points[1]);
			points[2] = Integer.parseInt(string_points[2]);

			int[] distance = {0,0,0};

			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\esgee\\Desktop\\project-stop\\data/"+name_file_centers));

			distance[0] = h_distance(points,br.readLine());
			distance[1] = h_distance(points,br.readLine());
			distance[2] = h_distance(points,br.readLine());

			if(distance[0] < distance[1])
			{
				if(distance[0] < distance[2])
			        System.out.println(0);
				else System.out.println(2);
			}

			else 
			{
				if(distance[1] < distance[2])
			        System.out.println(1);
				else System.out.println(2); 
			}
	}

	private static int h_distance(int[] point, String center)
       {      
              String[] string_centers = center.split(",");

              float[] centers = {0,0,0};

              centers[0] = Float.parseFloat(string_centers[0]);
              centers[1] = Float.parseFloat(string_centers[1]);
              centers[2] = Float.parseFloat(string_centers[2]);

              int distance = 0;
              int counter = 0;

              for(int p : point)
              {
                     distance = distance + Math.abs(p-(int)centers[counter]);
                     counter++;
              }    

              return distance;
       }
}