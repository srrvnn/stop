public class Trial
{
	public static void main(String[] args)
	{

		String line = new String("@attribute class {red,notred,ambiguousred,ambiguousnotred}");
		String[] classes = line.substring(line.indexOf("{")+1,line.indexOf("}")).split(",");

		for(String c : classes)
			System.out.println(c);
		
	}
}