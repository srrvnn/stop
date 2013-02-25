public class Trial
{
	public static void main(String[] args)
	{
		Cluster c = new Cluster(); 
		c.SetSource("screener.arff");

		c.Run();
	}
}