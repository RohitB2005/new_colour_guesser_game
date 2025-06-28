package nz.ac.auckland.se281.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiApplication extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Load the FXML file which defines the layout of your GUI
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
    Parent root = loader.load();

    // Set up the scene
    Scene scene = new Scene(root, 800, 600); // Window size 800x600

    scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

    // Set up the main window (called a "Stage" in JavaFX)
    primaryStage.setTitle("Mind Game - Colour Guesser");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
