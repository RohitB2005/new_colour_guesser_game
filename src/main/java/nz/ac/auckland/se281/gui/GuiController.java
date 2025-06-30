package nz.ac.auckland.se281.gui;

import java.util.Arrays;
import java.util.List;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.engine.Game;
import nz.ac.auckland.se281.engine.RoundResult;
import nz.ac.auckland.se281.model.Colour;

public class GuiController {

  // FXML UI elements
  @FXML private Label roundLabel, playerScoreLabel, aiScoreLabel, playerNameLabel, aiNameLabel;
  @FXML private HBox choiceBox, guessBox, playerIndicatorBox, aiIndicatorBox;
  @FXML private Button submitButton, playAgainButton, exitButton, nextRoundButton;
  @FXML private TextArea gameLogArea;
  @FXML private RadioButton easyRadio, mediumRadio, hardRadio, nightmareRadio;
  @FXML private TextField roundsTextField, playerNameTextField;
  @FXML private ImageView playerImageView, aiImageView;

  // Game state and helper fields
  private ToggleGroup difficultyGroup;
  private Game game;
  private Colour playerChoice;
  private Colour playerGuess;
  private final List<String> selectionStyles =
      Arrays.asList("red-selected", "green-selected", "blue-selected", "yellow-selected");

  // Animation fields for BOTH characters
  private Image playerIdleSheet, playerWinRoundSheet, playerLoseRoundSheet, playerLoseGameSheet;
  private Image aiIdleSheet, aiWinRoundSheet, aiLoseRoundSheet, aiLoseGameSheet;
  private Animation playerIdleAnimation,
      playerWinRoundAnimation,
      playerLoseRoundAnimation,
      playerLoseGameAnimation;
  private Animation aiIdleAnimation, aiWinRoundAnimation, aiLoseRoundAnimation, aiLoseGameAnimation;
  private Rectangle2D defaultPlayerViewport, defaultAiViewport;

  @FXML
  public void initialize() {
    this.game = new Game();
    difficultyGroup = new ToggleGroup();
    easyRadio.setToggleGroup(difficultyGroup);
    mediumRadio.setToggleGroup(difficultyGroup);
    hardRadio.setToggleGroup(difficultyGroup);
    nightmareRadio.setToggleGroup(difficultyGroup);

    loadAssetsAndCreateAnimations();
    resetToInitialState();
  }

  private void loadAssetsAndCreateAnimations() {
    try {
      final int FRAME_WIDTH = 128;
      final int FRAME_HEIGHT = 128;

      // Load Player Assets
      playerIdleSheet = new Image(getClass().getResourceAsStream("/images/PlayerIdle.png"));
      playerWinRoundSheet = new Image(getClass().getResourceAsStream("/images/Attack_1.png"));
      playerLoseRoundSheet = new Image(getClass().getResourceAsStream("/images/PlayerHurt.png"));
      playerLoseGameSheet = new Image(getClass().getResourceAsStream("/images/PlayerDead.png"));
      playerImageView.setImage(playerIdleSheet);
      defaultPlayerViewport = new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      playerImageView.setViewport(defaultPlayerViewport);

      playerIdleAnimation =
          new LeftToRightSpriteAnimation(
              playerImageView, Duration.millis(800), 5, 5, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      playerIdleAnimation.setCycleCount(Animation.INDEFINITE);
      playerWinRoundAnimation =
          new LeftToRightSpriteAnimation(
              playerImageView, Duration.millis(500), 4, 4, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      playerWinRoundAnimation.setCycleCount(Animation.INDEFINITE);
      playerLoseRoundAnimation =
          new LeftToRightSpriteAnimation(
              playerImageView, Duration.millis(400), 3, 3, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      playerLoseRoundAnimation.setCycleCount(Animation.INDEFINITE);
      playerLoseGameAnimation =
          new LeftToRightSpriteAnimation(
              playerImageView, Duration.millis(600), 4, 4, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      playerLoseGameAnimation.setCycleCount(1); // Game over animation plays only once

      // Load AI Assets
      aiIdleSheet = new Image(getClass().getResourceAsStream("/images/Idle.png"));
      aiWinRoundSheet = new Image(getClass().getResourceAsStream("/images/Shot_2.png"));
      aiLoseRoundSheet = new Image(getClass().getResourceAsStream("/images/Hurt.png"));
      aiLoseGameSheet = new Image(getClass().getResourceAsStream("/images/Dead.png"));
      aiImageView.setImage(aiIdleSheet);
      defaultAiViewport = new Rectangle2D(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      aiImageView.setViewport(defaultAiViewport);

      // Create AI Animations
      aiIdleAnimation =
          new SpriteAnimation(
              aiImageView, Duration.millis(1000), 5, 5, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      aiIdleAnimation.setCycleCount(Animation.INDEFINITE);
      aiWinRoundAnimation =
          new SpriteAnimation(
              aiImageView, Duration.millis(800), 8, 8, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      aiWinRoundAnimation.setCycleCount(Animation.INDEFINITE);
      aiLoseRoundAnimation =
          new SpriteAnimation(
              aiImageView, Duration.millis(500), 3, 3, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      aiLoseRoundAnimation.setCycleCount(Animation.INDEFINITE);
      aiLoseGameAnimation =
          new SpriteAnimation(
              aiImageView, Duration.millis(1200), 7, 7, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      aiLoseGameAnimation.setCycleCount(1); // Game over animation plays only once

    } catch (Exception e) {
      gameLogArea.appendText(
          "SYSTEM: Could not load character images. Check file names in"
              + " 'src/main/resources/images/'.\n");
      e.printStackTrace();
    }
  }

  @FXML
  public void handleSubmitMove() {
    if (playerChoice == null || playerGuess == null) {
      gameLogArea.appendText(
          "SYSTEM: Please select your colour AND your guess before submitting.\n");
      return;
    }

    setGameControlsDisabled(true);

    RoundResult result = game.playRound(playerChoice, playerGuess);
    if (result == null) return;

    updateScoreLabels();
    playerIndicatorBox
        .getChildren()
        .setAll(
            createColorIndicator(playerChoice, "Choice"),
            createColorIndicator(playerGuess, "Guess"));
    aiIndicatorBox
        .getChildren()
        .setAll(
            createColorIndicator(result.getAiChoice(), "Choice"),
            createColorIndicator(result.getAiGuess(), "Guess"));

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

    playRoundEndAnimations(result);
  }

  private void playRoundEndAnimations(RoundResult result) {
    playerIdleAnimation.stop();
    aiIdleAnimation.stop();

    if (result.getHumanPoints() > result.getAiPoints()) {
      playerImageView.setImage(playerWinRoundSheet);
      aiImageView.setImage(aiLoseRoundSheet);
      playerWinRoundAnimation.play();
      aiLoseRoundAnimation.play();
    } else if (result.getAiPoints() > result.getHumanPoints()) {
      playerImageView.setImage(playerLoseRoundSheet);
      aiImageView.setImage(aiWinRoundSheet);
      playerLoseRoundAnimation.play();
      aiWinRoundAnimation.play();
    }

    transitionToNextState();
  }

  private void transitionToNextState() {
    submitButton.setVisible(false);
    nextRoundButton.setVisible(true);
  }

  @FXML
  public void handleNextRound(ActionEvent event) {

    // Stop all possible looping animations before proceeding.
    playerWinRoundAnimation.stop();
    playerLoseRoundAnimation.stop();
    aiWinRoundAnimation.stop();
    aiLoseRoundAnimation.stop();

    // Restore both characters to their idle state.
    if (playerIdleAnimation != null) {
      playerImageView.setImage(playerIdleSheet);
      playerImageView.setViewport(defaultPlayerViewport);
      playerIdleAnimation.play();
    }
    if (aiIdleAnimation != null) {
      aiImageView.setImage(aiIdleSheet);
      aiImageView.setViewport(defaultAiViewport);
      aiIdleAnimation.play();
    }

    if (game.isGameOver()) {
      endGame();
    } else {
      resetForNextRound();
      setGameControlsDisabled(false);
    }
  }

  private void endGame() {
    int pScore = game.getPlayerScore();
    int aScore = game.getAiScore();

    gameLogArea.appendText("==================\n  GAME OVER\n==================\n");
    if (pScore > aScore) {
      gameLogArea.appendText("Congratulations, you win!\n");
      playerIdleAnimation.stop();
      if (aiLoseGameAnimation != null) {
        aiIdleAnimation.stop();
        aiImageView.setImage(aiLoseGameSheet);
        aiLoseGameAnimation.play();
      }
    } else if (aScore > pScore) {
      gameLogArea.appendText("HAL-9000 wins!\n");
      aiIdleAnimation.stop();
      if (playerLoseGameAnimation != null) {
        playerIdleAnimation.stop();
        playerImageView.setImage(playerLoseGameSheet);
        playerLoseGameAnimation.play();
      }
    } else {
      gameLogArea.appendText("It's a tie!\n");
    }

    setGameControlsDisabled(true);
    setSetupControlsDisabled(false);
    playAgainButton.setVisible(true);
    nextRoundButton.setVisible(false);
    exitButton.setDisable(false);
  }

  private void startNewGame() {
    RadioButton selectedRadio = (RadioButton) difficultyGroup.getSelectedToggle();
    if (selectedRadio == null) {
      nightmareRadio.setSelected(true);
      selectedRadio = nightmareRadio;
    }
    Difficulty difficulty = Difficulty.valueOf(selectedRadio.getText().toUpperCase());
    int numRounds;
    try {
      numRounds = Integer.parseInt(roundsTextField.getText());
      if (numRounds <= 0) throw new NumberFormatException();
    } catch (NumberFormatException e) {
      numRounds = 10;
      roundsTextField.setText("10");
    }

    String playerName = playerNameTextField.getText();
    if (playerName == null || playerName.trim().isEmpty()) {
      playerName = "Player";
    }

    game.newGame(difficulty, numRounds, new String[] {playerName});

    resetToInitialState();
    setGameControlsDisabled(false);
    setSetupControlsDisabled(true);
    exitButton.setDisable(false);
    playAgainButton.setVisible(false);
    submitButton.setVisible(true);
    gameLogArea.clear();
    gameLogArea.appendText(
        "New game started. Difficulty: "
            + difficulty
            + ". Rounds: "
            + numRounds
            + ". Good luck, "
            + playerName
            + "!\n");
    playerNameLabel.setText(game.getPlayerName());

    resetForNextRound();
  }

  private void resetToInitialState() {
    setGameControlsDisabled(true);
    setSetupControlsDisabled(false);
    playAgainButton.setText("Start Game");
    playAgainButton.setVisible(true);
    submitButton.setVisible(false);
    nextRoundButton.setVisible(false);
    exitButton.setDisable(true);
    roundLabel.setText("Round: 0 / 0");
    playerScoreLabel.setText("Your Score: 0");
    aiScoreLabel.setText("HAL-9000 Score: 0");
    playerIndicatorBox.getChildren().clear();
    aiIndicatorBox.getChildren().clear();

    if (playerIdleAnimation != null) {
      playerWinRoundAnimation.stop();
      playerLoseRoundAnimation.stop();
      playerLoseGameAnimation.stop();
      playerImageView.setImage(playerIdleSheet);
      playerImageView.setViewport(defaultPlayerViewport);
      playerIdleAnimation.play();
    }
    if (aiIdleAnimation != null) {
      aiWinRoundAnimation.stop();
      aiLoseRoundAnimation.stop();
      aiLoseGameAnimation.stop();
      aiImageView.setImage(aiIdleSheet);
      aiImageView.setViewport(defaultAiViewport);
      aiIdleAnimation.play();
    }
  }

  private void resetForNextRound() {
    playerChoice = null;
    playerGuess = null;
    for (Node node : choiceBox.getChildren()) {
      node.getStyleClass().removeAll(selectionStyles);
    }
    for (Node node : guessBox.getChildren()) {
      node.getStyleClass().removeAll(selectionStyles);
    }
    playerIndicatorBox.getChildren().clear();
    aiIndicatorBox.getChildren().clear();
    updateRoundLabel();
    submitButton.setVisible(true);
    nextRoundButton.setVisible(false);
  }

  @FXML
  public void handlePlayAgain(ActionEvent event) {
    startNewGame();
  }

  @FXML
  public void handleExitGame(ActionEvent event) {
    resetToInitialState();
  }

  @FXML
  public void handleColorChoice(ActionEvent event) {
    updateSelection(choiceBox, (Button) event.getSource());
    this.playerChoice = Colour.valueOf(((Button) event.getSource()).getText());
  }

  @FXML
  public void handleGuessChoice(ActionEvent event) {
    updateSelection(guessBox, (Button) event.getSource());
    this.playerGuess = Colour.valueOf(((Button) event.getSource()).getText());
  }

  private void updateSelection(HBox buttonGroup, Button clickedButton) {
    for (Node node : buttonGroup.getChildren()) {
      node.getStyleClass().removeAll(selectionStyles);
    }
    String colorName = clickedButton.getText().toLowerCase();
    clickedButton.getStyleClass().add(colorName + "-selected");
  }

  private VBox createColorIndicator(Colour color, String labelText) {
    Rectangle rect = new Rectangle(50, 30);
    rect.setArcWidth(10);
    rect.setArcHeight(10);
    rect.setFill(Color.valueOf(color.name()));
    rect.setStroke(Color.BLACK);
    rect.setStrokeWidth(1.5);
    Label label = new Label(labelText);
    VBox container = new VBox(5, rect, label);
    container.setAlignment(javafx.geometry.Pos.CENTER);
    return container;
  }

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
    playerNameTextField.setDisable(isDisabled);
  }
}
