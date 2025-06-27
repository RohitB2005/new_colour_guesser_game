package nz.ac.auckland.se281.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se281.engine.Game; // Import your existing Game engine

public class GuiController {

  @FXML private Label infoLabel;

  @FXML private Button startButton;

  // This holds the instance of your game engine
  private Game game;

  @FXML
  public void initialize() {
    // This method is called automatically when the FXML is loaded.
    // Create an instance of your game engine.
    this.game = new Game();
    System.out.println("GUI Initialized. Game engine created.");
  }

  @FXML
  private void handleStartButton() {
    // This is where you connect the GUI to your game's logic.
    // For now, let's just update the label.
    infoLabel.setText("New game started! Waiting for player input...");
    startButton.setDisable(true); // Disable the button after starting

    // In the future, you would call a method on your 'game' object here.
    // For example:
    // game.newGame(Difficulty.NIGHTMARE, 10, new String[]{"Player1"});
  }
}
