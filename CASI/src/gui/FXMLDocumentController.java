
package gui;


import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.atan;
import static java.lang.Math.abs;

import java.awt.MouseInfo;
import java.awt.Point;

import static java.lang.Math.PI;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import archive.Settings;
import mood.weather.Weather;
import mood.sentimentDriver;



public class FXMLDocumentController implements Initializable {
    
    double x, y, window_size, sign;
    int x_int = 0, y_int = 0, x_int_old, y_int_old;
    
    
    int main_content_hidden = 0;
    
    
    double canvas_background = 0.5;
    
    double default_x = 600, default_y = 600;
    
    double wave_amplitude= 1.0;
    double wave_amplitude_change = 0.01;
    public static boolean talking = false;
    
	public static double bot_sent;
    
    
    
    @FXML
    private Label max_label;
    
    @FXML
    private Canvas canvas;
    
    @FXML
    private AnchorPane main_background;
    
    @FXML
    private HBox bottom_bar;
    
    
    
    
    @FXML
    void handle_request(MouseEvent event) {

    	Thread request = new Thread(new RequestHandler());
		request.start();
    	
    }
    
    
    //Allows Window to be moved
    @FXML
    void dragged(MouseEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setX(event.getScreenX() - x);
        primaryStage.setY(event.getScreenY() - y);
    }

    
    @FXML
    void title_pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    
    
    
    //Allows for Double-click on Canvas to go through screen
    @FXML
    void canvas_pressed(MouseEvent event) {
    	
            x_int_old = x_int;
            y_int_old = y_int;
            PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
            x = event.getSceneX();
            y = event.getSceneY();
            x_int = (int) x;
            y_int = (int) y-105;
            System.out.println(x_int + " " + y_int);
            pixelWriter.setColor(x_int, y_int, Color.rgb(0, 100, 100, 0.0));
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        pixelWriter.setColor(x_int, y_int, Color.rgb(0, 100, 100, 0.5));
                        pixelWriter.setColor(x_int_old, y_int_old, Color.rgb(0, 100, 100, 0.5));
                    });

                }
            }, 200);
        
    }
    
    
    
    
    
    //Closes Window
    @FXML
    void close(MouseEvent event) {
    	
    	
    	Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		long epoch = date.getTime();
		epoch /= 1000;
		
		Settings.changeSetting("last_date", epoch + "");
    	
		Settings.Save();
		
    	
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.close();
    }
    
    
    
    
    
    
    
    //Minimises Window
    @FXML
    void min(MouseEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setIconified(true);
    }
    
    
    
    
    
    //On Start
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	bot_sent = 0;
    	
    	Settings.Load();
    	boolean first_start = Boolean.parseBoolean(Settings.Get("first_start"));
    	
    	if (first_start) {
    		sentimentDriver.getBotSentiment();
    	}
    	else {
	    	//updates settings
	    	
	    	Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			long epoch = date.getTime();
			epoch /= 1000;
			
			long last_date = Long.parseLong(Settings.Get("last_date"));
			last_date /= 1000;
			
			if (abs(epoch - last_date) > 86400) {
				
				Weather.update("avg_temp", "apparent temperature");
				Weather.update("avg_light", "cloud cover");
				
			}
			
			sentimentDriver.getBotSentiment();
			
    	}
		
    	
        //Sets Initial Color Scheme
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        for (int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++) {
                pixelWriter.setColor(i, j, Color.rgb(0, 100, 100, 0.5));
            }
        }
        
        
        
        
        
        
        
        //graphs left and right eye
        for (double i = 0; i < 2*PI; i += 0.001) {
        	
        	int[] lcoord = left_eye_equation(i);
        	pixelWriter.setColor(lcoord[0], lcoord[1] - 350, Color.BLACK);
        	
        	int[] rcoord = right_eye_equation(i);
        	pixelWriter.setColor(rcoord[0], rcoord[1] - 350, Color.BLACK);
        	
        }
        
        
        //Creates and starts timer to graphics    	
    	Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        int x_bound = (int) bounds.getMaxX();
        
        
        //graphs smile
        /*
        for (double i = 0; i < PI; i += 0.001) {
        	
        	smile_coord = smile_equation(i, 1);
        	pixelWriter.setColor(smile_coord[0], smile_coord[1] + 250, Color.BLACK);
        	
        	smile_coord = smile_equation(i, -1);
        	pixelWriter.setColor(smile_coord[0], smile_coord[1] + 250, Color.BLACK);
        	
		}
        */
        
        
        Timer waveTimer = new Timer(true);
        waveTimer.schedule(new TimerTask(){
        	@Override
        	public void run() {
        		Platform.runLater(() -> {
        			if (talking) {
	        			for (double i = 0; i < PI; i += 0.005) {
	        				
	        				int[] smile_coord1 = smile_equation(i, wave_amplitude+(sign * wave_amplitude_change * -1));
	        	        	pixelWriter.setColor(smile_coord1[0], smile_coord1[1] + 250, Color.rgb(0, 100, 100, canvas_background));
	        	   
	        				
	        				int[] smile_coord2 = smile_equation(i, wave_amplitude);
	        	        	pixelWriter.setColor(smile_coord2[0], smile_coord2[1] + 250, Color.BLACK);
	        	        	
	        	        	
	        	        	
	        	        	int[] smile_coord3 = smile_equation(i, (-wave_amplitude+(sign * wave_amplitude_change) + bot_sent)/2);
	        	        	pixelWriter.setColor(smile_coord3[0], smile_coord3[1] + 250, Color.rgb(0, 100, 100, canvas_background));
	        	   
	        				
	        				int[] smile_coord4 = smile_equation(i, (-wave_amplitude + bot_sent)/2);
	        	        	pixelWriter.setColor(smile_coord4[0], smile_coord4[1] + 250, Color.BLACK);
	        	        	
	        	        	
	        	        }
        			
	        			
        			
						if ((int) wave_amplitude == 1) {
	                        sign = -1;
	                    } else if ((int) wave_amplitude == -1) {
	                        sign = 1;
	                    }
	                    
	                    wave_amplitude = wave_amplitude + (sign *  wave_amplitude_change);
        			}
        			else {
        				for (double i = 0; i < PI; i += 0.005) {
	        				int[] smile_coord = smile_equation(i, bot_sent);
	        	        	pixelWriter.setColor(smile_coord[0], smile_coord[1] + 250, Color.BLACK);
        				}
        				
        			}
        			
        		});  
        	}
        }, 0, 100);
        
        
        
        
        
        //graphs nose
        
        
    }
    
    
    
    private int[] smile_equation(double input, double factor) {
    	int[] coord = new int[2];
    	
    	int x = (int) (200 * cos(input) + 300);
    	int y = (int) (100 * factor * sin(input) + 150);
    	
    	coord[0] = x;
    	coord[1] = y;
    	
    	return coord;
    }
    
    
    private int[] left_eye_equation(double input) {
    	int[] coord = new int[2];
    	
    	int x = (int) (25 * cos(input) + 150);
    	int y = (int) (50 * sin(input) + 500);
    	
    	coord[0] = x;
    	coord[1] = y;
    	
    	return coord;
    }
    
    private int[] right_eye_equation(double input) {
    	int[] coord = new int[2];
    	
    	int x = (int) (25 * cos(input) + 450);
    	int y = (int) (50 * sin(input) + 500);
    	
    	coord[0] = x;
    	coord[1] = y;
    	
    	return coord;
    }
    
    
    
    
    
    
}
