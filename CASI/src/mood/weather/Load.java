/*
 * Author:	Aidan Evans
 * Date:	3/2/2019
 */


package mood.weather;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import memory.*;


public class Load {
	
	private static final String ipKey = "0946d1ffb1f832625b4363c72f3e5cd2";
	
	private static final String weatherKey = "bbd834fe6843d8447a9c8941eed85c09";
	
	
	
	//pulls data from a url endpoint
	//pre-conditions:	url endpoint in form of String
	//post-conditions:	returns pulled data in form of string
	private static String pullData(String endpoint) {
		
		try {
			
			URL url = new URL(endpoint);
	
	        //gets the input stream through URL Connection
	        URLConnection con = url.openConnection();
	        InputStream is =con.getInputStream();
	        
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
	        String line = null;
	
	        //returns data
	        while ((line = br.readLine()) != null) {
	        	return line;
	        }
	        
        }
        catch (Exception e) {
        	
        	e.printStackTrace();
        }
		
		
		return "";
	}
	
	
	

	//gets location data of current location
	//pre-conditions:	none
	//post-conditions:	returns MemoryCluster of location data
	public static MemoryCluster getLocationData() {
		
		MemoryCluster location = new MemoryCluster();
		
		
		try {
		
			//gets public ip address
			String public_ip = pullData("http://bot.whatismyipaddress.com");
			
			
			
			//calls endpoint using ip address to retrieve location data
			String url_endpoint = "http://api.ipstack.com/" + public_ip + "?access_key=" + ipKey; 
			
			System.out.println(url_endpoint);
			
			
			String data_string = pullData(url_endpoint);
			
			System.out.println(data_string);
			System.out.println();
			
			
			
			//sorts returned json data
			JSONObject obj = new JSONObject(data_string);
	
			
			//latitude
			String[] key_words0 = {"location", "latitude"};
			location.add(
					key_words0, 
					obj.getDouble("latitude") + ""
				);
			
			//longitude
			String[] key_words1 = {"location", "longitude"};
			location.add(
					key_words1, 
					obj.getDouble("longitude") + ""
				);
			
			//city
			String[] key_words2 = {"location", "city", "town"};
			location.add(
					key_words2, 
					obj.getString("city")
				);
			
			//state
			String[] key_words3 = {"location", "state", "providence"};
			location.add(
					key_words3, 
					obj.getString("region_name")
				);
			
			//country
			String[] key_words4 = {"location", "country"};
			location.add(
					key_words4, 
					obj.getString("country_name")
				);
			
			//continent
			String[] key_words5 = {"location", "continent"};
			location.add(
					key_words5, 
					obj.getString("continent_name")
				);
			
			//zip code
			String[] key_words6 = {"location", "zip", "postal", "code"};
			location.add(
					key_words6, 
					obj.getString("zip")
				);
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}


		return location;
		
	}
	
	
	
	
	public static MemoryCluster getWeatherData(double lat, double lng, boolean first_start, boolean update) {
		
		MemoryCluster weather = new MemoryCluster();
		
		
		try {
			/*
			//Makes request to api endpoint for timezone based on latitude and longitude and returns timezone abbreviation
			String timezone_url_endpoint = "http://api.timezonedb.com/v2.1/get-time-zone?key=" + timezoneKey + "&format=json&by=position&lat=" + lat + "&lng=" + lng;
			
			System.out.println(timezone_url_endpoint);
			
			
			String timezone_data_string = pullData(timezone_url_endpoint);
			
			System.out.println(timezone_data_string);
			System.out.println();
			
			String timezone = (new JSONObject(timezone_data_string)).getString("abbreviation");
			*/
			
			
			//Makes request to api endpoint for weather one month ago and gets returned data
			int loop_count;
			long epoch = 0;
			
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			
			if (first_start) {
				
				epoch = date.getTime();
				epoch /= 1000;
				
				loop_count = 30;
				
			}
			else {
				
				loop_count = 1;
			}
					
			for (int i = 0; i < loop_count; i++) {
				String weather_url_endpoint = (first_start || update) ? "https://api.darksky.net/forecast/" + weatherKey + "/" + lat + "," + lng + "," + (epoch - 86400) : "https://api.darksky.net/forecast/" + weatherKey + "/" + lat + "," + lng;
				
				System.out.println(weather_url_endpoint);
				
				
				String weather_data_string = pullData(weather_url_endpoint);
				
				System.out.println(weather_data_string);
				System.out.println();
				

				long time;
				if (first_start) {
					epoch -= 86400;
					time = epoch;
				}
				else {
					time = date.getTime();
				}
				
				
				//Formats and sorts returns JSON data
				JSONObject obj = new JSONObject(weather_data_string);
				
				JSONObject data = (first_start || update) ? obj.getJSONObject("daily").getJSONArray("data").getJSONObject(0) : obj.getJSONObject("currently");
				
				if (first_start || update) {
					
					//Apparent Temperature High
					String[] key_words0 = {"weather", "apparent", "temperature", "high", time + ""};
					weather.add(
							key_words0, 
							data.getDouble("apparentTemperatureHigh") + ""
						);
					
					//Apparent Temperature Low
					String[] key_words1 = {"weather", "apparent", "temperature", "low", time + ""};
					weather.add(
							key_words1, 
							data.getDouble("apparentTemperatureLow") + ""
						);
					
				}
				else {
					
					//Apparent Temperature
					String[] key_words0 = {"weather", "apparent", "temperature", time + ""};
					weather.add(
							key_words0, 
							data.getDouble("apparentTemperature") + ""
						);
					
				}
				
				//Cloud Cover
				String[] key_words2 = {"weather", "cloud", "cover", time + ""};
				weather.add(
						key_words2, 
						data.getDouble("cloudCover") + ""
					);
				
				//Moon Phase
				try {
					String[] key_words3 = {"weather", "moon", "phase", time + ""};
					weather.add(
							key_words3, 
							data.getDouble("moonPhase") + ""
						);
				}
				catch (Exception ex) {}
				
				//Snow Accumulation
				try {
					String[] key_words4 = {"weather", "precipitation", "snow", "amount", time + ""};
					weather.add(
							key_words4, 
							data.getDouble("precipAccumulation") + ""
						);
				}
				catch (Exception ex) {}
				
				try {
					//Precipitation Intensity
					String[] key_words5 = {"weather", "precipitation", "rain", "liquid", "amount", time + ""};
					weather.add(
							key_words5, 
							data.getDouble("precipIntensity") + ""
						);
				}
				catch (Exception ex) {}
				
				try {
					//Precipitation Probability
					String[] key_words6 = {"weather", "precipitation", "probability", time + ""};
					weather.add(
							key_words6, 
							data.getDouble("precipProbability") + ""
						);
				}
				catch (Exception ex) {}
				
				//Precipitation Type
				try {
					String[] key_words7 = {"weather", "precipitation", "type", time + ""};
					weather.add(
							key_words7, 
							data.getString("precipType")
						);
				}
				catch (Exception ex) {}
				
				//Humidity
				String[] key_words8 = {"weather", "humidity", time + ""};
				weather.add(
						key_words8, 
						data.getDouble("humidity") + ""
					);
				
				//Pressure
				String[] key_words9 = {"weather", "pressure", time + ""};
				weather.add(
						key_words9, 
						data.getDouble("pressure") + ""
					);
				
				//UV Index
				String[] key_words10 = {"weather", "UV", "index", "ultraviolet", "radiation", time + ""};
				weather.add(
						key_words10, 
						data.getDouble("uvIndex") + ""
					);
				
				//Time
				String[] key_words11 = {"weather", "time", time + ""};
				weather.add(
						key_words11, 
						data.getInt("time") + ""
					);
				
			}

			
		}
		catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		return weather;
		
	}
	
	
	public static MemoryCluster getWeatherData(String lati, String longi, boolean first_start, boolean update) {
		
		double latitude = Double.parseDouble(lati);
		double longitude = Double.parseDouble(longi);
		
		return getWeatherData(latitude, longitude, first_start, update);
		
	}
	
	
	
	public static void main(String[] args) {
		
		MemoryCluster location = getLocationData();
		
		MemoryCluster weather = getWeatherData(location.pull("location latitude")[0], location.pull("location longitude")[0], false, false);
		
		for (Memory m : weather.pull_all())
			System.out.println(m.content);
		
	}

}
