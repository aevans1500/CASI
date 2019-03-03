package mood.weather;

import memory.*;
import archive.*;

public class Weather {
	
	
	//the content of the memory desired must be average
	private static double getAvg(MemoryCluster weather, String term) {
		
		double average = 0.0;
		int count = 0;
		
		String[] data = weather.pull(term);
		
		for (String s : data) {
			System.out.println(" " + s);
			average += Double.parseDouble(s);
			count++;
		}
		
		average /= count;
		
		return average;
		
	}
	
	
	
	public static MemoryCluster meanData() {
		
		boolean first_start = Boolean.parseBoolean(Settings.Get("first_start"));
		double temp_avg;
		double light_avg;
		
		if (first_start) {
			
			MemoryCluster location = Load.getLocationData();
			
			MemoryCluster weather = Load.getWeatherData(location.pull("location latitude")[0], location.pull("location longitude")[0], first_start, false);
			
			
			//mean data for apparent temperature
			temp_avg = getAvg(weather, "apparent temperature");
			
			//mean data for light level
			light_avg = getAvg(weather, "cloud coverage");
			
			
			Settings.changeSetting("first_start", "false");
			Settings.changeSetting("avg_temp", temp_avg + "");
			Settings.changeSetting("avg_light", light_avg + "");
			
		}
		else {
			
			temp_avg = Double.parseDouble(Settings.Get("avg_temp"));
			light_avg = Double.parseDouble(Settings.Get("avg_light"));
			
		}
		
		MemoryCluster avg = new MemoryCluster();
		
		String[] key_words0 = {"weather", "average", "temperature"};
		avg.add(
				key_words0,
				temp_avg + ""
			);
		
		
		String[] key_words1 = {"weather", "average", "light", "level", "cloud", "coverage"};
		avg.add(
				key_words1,
				light_avg + ""
			);
		
		return avg;
		
	}
	
	
	
	public static void update(String setting_name, String terms) {
		
		
		//retrieves current weather data
		MemoryCluster location = Load.getLocationData();
		
		MemoryCluster weather = Load.getWeatherData(location.pull("location latitude")[0], location.pull("location longitude")[0], false, true);
				
		double avg = getAvg(weather, terms);
		
		double old_avg = Double.parseDouble(Settings.Get(setting_name));
		
		double new_avg = (avg + old_avg) / 2;
		
		Settings.changeSetting(setting_name, new_avg + "");
		
		
		System.out.println("Setting Changed");
		
	}
	
	
	
	public static double percentDiff(MemoryCluster avg_data, String name, String terms) {
		
		double avg = Double.parseDouble(Settings.Get(name));
		
		//retrieves current weather data
		MemoryCluster location = Load.getLocationData();
		
		MemoryCluster weather = Load.getWeatherData(location.pull("location latitude")[0], location.pull("location longitude")[0], false, false);
		
		double current_avg = getAvg(weather, terms);
		
		double diff = (avg - current_avg) / avg;
		
		return diff;
		
		
	}
	
	

	public static void main(String[] args) {
		
		MemoryCluster avg = meanData();
		
		double diff = percentDiff(avg, "avg_temp", "apparent temperature");
		System.out.println(diff);
		
		diff = percentDiff(avg, "avg_light", "cloud cover");
		System.out.println(diff);
		
		
	}

}
