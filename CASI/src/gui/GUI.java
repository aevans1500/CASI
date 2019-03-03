package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {

	double default_x = 600;
    double default_y = 600;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        //Sets Background to Transparent
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(null);
        
        //Sets Window to Bottom Right of Screen
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMaxX() - default_x);
        primaryStage.setY(bounds.getMaxY() - default_y);
        
        //Makes Window Always on Top
        primaryStage.setAlwaysOnTop(true);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
	
}
