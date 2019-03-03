package archive;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Settings {
	
	public static ArrayList<Setting> settings = new ArrayList<>();
	
	
	public static String Get(String name) {
		
		for (Setting s : settings) {
			if (s.name.equals(name))
				return s.value;
		}
		
		return null;
		
	}
	
	
	public static void Load() {
		
		try {
			
			Scanner scanner1 = new Scanner(new FileReader("settings.txt"));
			
			while (scanner1.hasNextLine()) {

				int id = 0;
				
				String line = scanner1.nextLine();
				
				System.out.println(line);
				
				Scanner scanner2 = new Scanner(line);
				
				
				Setting set = new Setting();
				
				set.name = scanner2.next();
				set.type = scanner2.next();
				set.value = scanner2.next();
				
				settings.add(set);
				
				scanner2.close();
				
			}
			
			scanner1.close();
			
			
		}
		catch (Exception ex) {}
		
	}
	
	
	
	public static void Save() {
		
		try {
			
			PrintWriter pw = new PrintWriter("settings.txt");
			
			for (Setting s : settings) {
				pw.write(s.name + " " + s.type + " " + s.value + "\n");
			}
			
			pw.flush();
			pw.close();
			
			System.out.println("Settings Saved");
			System.out.println(settings.size());
			
		}
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	
	
	public static void Reset( ) {
		
		try {
			
			PrintWriter pw = new PrintWriter("settings.txt");
			
			pw.write("first_start boolean true\n");
			pw.write("avg_temp double 0.0\n");
			pw.write("avg_light double 0.0\n");
			
			
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			
			pw.write("last_date long " + (date.getTime() / 1000) + "\n");
			
			
			pw.flush();
			pw.close();
			
			System.out.println("Settings Reset");
			
		}
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
	}
	

	
	public static void newSetting(String name, String type, String value) {
		
		Setting set = new Setting();
		
		set.name = name;
		set.type = type;
		set.value = value;
		
		settings.add(set);
		
		System.out.println("New Setting Added: " + name + " " + type + " " + value);
		
	}
	
	
	
	public static void changeSetting(String name, String new_val) {
		
		for (int i = 0; i < settings.size(); i++) {
			
			if (settings.get(i).name.equals(name)) {
				
				System.out.println(settings.get(i).value);
				
				settings.get(i).value = new_val;
				
				System.out.println(settings.get(i).value);
				
				break;
				
			}
			
		}
		
	}
	


	public static void main(String[] args) {
		
		Reset();
		
	}
	

}
