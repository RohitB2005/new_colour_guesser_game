package nz.ac.auckland.se281.gui;

import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.engine.Game;
import nz.ac.auckland.se281.engine.RoundResult;
import nz.ac.auckland.se281.model.Colour;

public class GuiController {

  // FXML fields - no changes needed here
  @FXML private Label roundLabel, playerScoreLabel, aiScoreLabel;
  @FXML private HBox choiceBox, guessBox;
  @FXML private Button submitButton, playAgainButton;
  @FXML private TextArea gameLogArea;
  @FXML private RadioButton easyRadio, mediumRadio, hardRadio, nightmareRadio;
  @FXML private TextField roundsTextField;

  private ToggleGroup difficultyGroup;
  private Game game;
  private Colour playerChoice;
  private Colour playerGuess;

  // A list of our selection style classes for easy removal
  private final List<String> selectionStyles =
      Arrays.asList("red-selected", "green-selected", "blue-selected", "yellow-selected");

  @FXML
  public void initialize() {
    this.game = new Game();
    difficultyGroup = new ToggleGroup();
    easyRadio.setToggleGroup(difficultyGroup);
    mediumRadio.setToggleGroup(difficultyGroup);
    hardRadio.setToggleGroup(difficultyGroup);
    nightmareRadio.setToggleGroup(difficultyGroup);
    setGameControlsDisabled(true);
    playAgainButton.setText("Start Game");
    playAgainButton.setVisible(true);
  }

  @FXML
  private void handleColorChoice(ActionEvent event) {
    // This handler now ONLY affects the choiceBox
    updateSelection(choiceBox, (Button) event.getSource());
    this.playerChoice = Colour.valueOf(((Button) event.getSource()).getText());
  }

  @FXML
  private void handleGuessChoice(ActionEvent event) {
    // This handler now ONLY affects the guessBox
    updateSelection(guessBox, (Button) event.getSource());
    this.playerGuess = Colour.valueOf(((Button) event.getSource()).getText());
  }

  // A helper method to avoid repeating code for button selection
  private void updateSelection(HBox buttonGroup, Button clickedButton) {
    // First, remove any selection style from all buttons in this group
    for (Node node : buttonGroup.getChildren()) {
      node.getStyleClass().removeAll(selectionStyles);
    }
    // Then, add the correct color-specific style to the clicked button
    String colorName = clickedButton.getText().toLowerCase();
    clickedButton.getStyleClass().add(colorName + "-selected");
  }

  @FXML
  private void handleSubmitMove() {
    if (playerChoice == null || playerGuess == null) {
      gameLogArea.appendText(
          "SYSTEM: Please select your colour AND your guess before submitting.\n");
      return;
    }
    // ... (rest of the method is the same as before)
    RoundResult result = game.playRound(playerChoice, playerGuess);
    if (result == null) return;

    gameLogArea.appendText("\n--- Round " + (game.getCurrentRound() - 1) + " ---\n");
    if (game.getPowerColour() != null) {
      gameLogArea.appendText("Power Colour is " + game.getPowerColour() + "!\n");
    }
    gameLogArea.appendText("You played " + playerChoice + " and guessed " + playerGuess + ".\n");
    gameLogArea.appendText(
        "HAL-9000 played " + result.getAiChoice() + " and guessed " + result.getAiGuess() + ".\n");
    gameLogArea.appendText(
        "You scored "
            + result.getHumanPoints()
            + " points. HAL-9000 scored "
            + result.getAiPoints()
            + " points.\n");

    updateScoreLabels();

    if (game.isGameOver()) {
      endGame();
    } else {
      resetForNextRound();
    }
  }

  @FXML
  private void handlePlayAgain(ActionEvent event) {
    startNewGame();
  }

  private void startNewGame() {
    // ... (this method is the same as before)
    RadioButton selectedRadio = (RadioButton) difficultyGroup.getSelectedToggle();
    Difficulty difficulty = Difficulty.valueOf(selectedRadio.getText().toUpperCase());
    int numRounds;
    try {
      numRounds = Integer.parseInt(roundsTextField.getText());
      if (numRounds <= 0) throw new NumberFormatException();
    } catch (NumberFormatException e) {
      gameLogArea.appendText("SYSTEM: Invalid number of rounds. Defaulting to 10.\n");
      numRounds = 10;
      roundsTextField.setText("10");
    }
    game.newGame(difficulty, numRounds, new String[] {"Rohit"});
    setGameControlsDisabled(false);
    setSetupControlsDisabled(true);
    playAgainButton.setVisible(false);
    gameLogArea.clear();
    gameLogArea.appendText(
        "New game started. Difficulty: "
            + difficulty
            + ". Rounds: "
            + numRounds
            + ". Good luck.\n");
    resetForNextRound();
  }

  private void resetForNextRound() {
    playerChoice = null;
    playerGuess = null;
    // Clear selection styles from both groups
    for (Node node : choiceBox.getChildren()) {
      node.getStyleClass().removeAll(selectionStyles);
    }
    for (Node node : guessBox.getChildren()) {
      node.getStyleClass().removeAll(selectionStyles);
    }
    updateRoundLabel();
  }

  private void endGame() {
    // ... (this method is the same as before)
    gameLogArea.appendText("==================\n  GAME OVER\n==================\n");
    int pScore = game.getPlayerScore();
    int aScore = game.getAiScore();
    if (pScore > aScore) {
      gameLogArea.appendText("Congratulations, you win!\n");
    } else if (aScore > pScore) {
      gameLogArea.appendText("HAL-9000 wins!\n");
    } else {
      gameLogArea.appendText("It's a tie!\n");
    }
    setGameControlsDisabled(true);
    setSetupControlsDisabled(false);
    playAgainButton.setVisible(true);
  }

  // The helper methods below are the same as before
  private void updateRoundLabel() {
    roundLabel.setText("Round: " + game.getCurrentRound() + " / " + game.getTotalRounds());
  }

  private void updateScoreLabels() {
    playerScoreLabel.setText("Your Score: " + game.getPlayerScore());
    aiScoreLabel.setText("HAL-9000 Score: " + game.getAiScore());
  }

  private void setGameControlsDisabled(boolean isDisabled) {
    choiceBox.setDisable(isDisabled);
    guessBox.setDisable(isDisabled);
    submitButton.setDisable(isDisabled);
  }

  private void setSetupControlsDisabled(boolean isDisabled) {
    easyRadio.setDisable(isDisabled);
    mediumRadio.setDisable(isDisabled);
    hardRadio.setDisable(isDisabled);
    nightmareRadio.setDisable(isDisabled);
    roundsTextField.setDisable(isDisabled);
  }
}
