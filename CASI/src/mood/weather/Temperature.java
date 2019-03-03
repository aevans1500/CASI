package mood.weather;

import memory.*;

public class Temperature {

	public static double getAvg(MemoryCluster weather) {
		
		double average = 0.0;
		int count = 0;
		
		String[] data = weather.pull("apparent temperature");
		
		for (String s : data) {
			System.out.println(s);
			average += Double.parseDouble(s);
			count++;
		}
		
		average /= count;
		
		return average;
		
	}

}
