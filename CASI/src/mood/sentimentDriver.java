package mood;

import memory.MemoryCluster;
import static mood.weather.Weather.*;

public class sentimentDriver {
	
	public static double getBotSentiment() {
		
		//Gets the percent difference of the historic average and the current average for temperature and light level
		MemoryCluster avg = meanData();
		
		double temp_diff = percentDiff(avg, "avg_temp", "apparent temperature");
		
		double light_diff = percentDiff(avg, "avg_light", "cloud cover");
		
		double avg_diff;
		
		avg_diff = (temp_diff + light_diff) / 2;
		
		System.out.println(temp_diff + " " + light_diff + " " + avg_diff);
		
		System.out.println(2*avg_diff - 1);
		gui.FXMLDocumentController.bot_sent = 2*avg_diff - 1;
		
		return avg_diff;
		
	}

	public static void main(String[] args) {

		//Gets the percent difference of the historic average and the current average for temperature and light level
		getBotSentiment();
		
	}

}
