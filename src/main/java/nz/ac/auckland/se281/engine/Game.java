package nz.ac.auckland.se281.engine;

import java.util.ArrayList;
import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.cli.MessageCli;
import nz.ac.auckland.se281.model.Colour;
import nz.ac.auckland.se281.players.AiPlayer;
import nz.ac.auckland.se281.players.Factory;
import nz.ac.auckland.se281.players.HumanPlayer;
import nz.ac.auckland.se281.players.Players;
import nz.ac.auckland.se281.strategies.AvoidLastStrategy;
import nz.ac.auckland.se281.strategies.LeastUsedStrategy;
import nz.ac.auckland.se281.strategies.RandomStrategy;
import nz.ac.auckland.se281.strategies.Strategies;

public class Game {

  private int thisRound;
  private int totalRounds;
  private boolean gameInProgress;

  private Players humanPlayer;
  private AiPlayer AiPlayer;

  private Colour powerColour;

  private Difficulty thisDifficulty;
  private Colour humanPreviousChoice;
  private ArrayList<Colour> history;
  private String usedStrategy;
  boolean pointsScored;

  public Game() {
    this.thisRound = 1;
    this.gameInProgress = false;
    this.humanPreviousChoice = null;
    this.history = new ArrayList<>();
    this.pointsScored = false;
  }

  public void newGame(Difficulty difficulty, int numRounds, String[] options) {
    this.humanPlayer = new HumanPlayer(options[0]);
    this.AiPlayer = Factory.aiConstructor(difficulty);

    MessageCli.WELCOME_PLAYER.printMessage(this.humanPlayer.getName());

    this.totalRounds = numRounds;
    this.thisRound = 1;
    this.gameInProgress = true;

    this.thisDifficulty = difficulty;
    this.humanPreviousChoice = null;
    this.history.clear();
    this.pointsScored = false;
    this.usedStrategy = "Random";
  }

  public void play() {

    if (!gameInProgress) {
      MessageCli.GAME_NOT_STARTED.printMessage();
      return;
    }

    MessageCli.START_ROUND.printMessage(
        String.valueOf(this.thisRound), String.valueOf(this.totalRounds));

    if (this.thisDifficulty == Difficulty.MEDIUM) {
      this.AiPlayer.getStrategy().setHumanPreviousChoice(this.humanPreviousChoice);
    }

    if (this.thisDifficulty == Difficulty.HARD) {
      if (this.thisRound == 1 || this.thisRound == 2) {
        AiPlayer.setStrategy(new RandomStrategy());
        usedStrategy = "Random";
      } else if (this.thisRound == 3) {
        AiPlayer.setStrategy(new LeastUsedStrategy());
        usedStrategy = "LeastUsed";
      } else {
        if (this.pointsScored == false) {
          if (usedStrategy.equals("LeastUsed")) {
            AiPlayer.setStrategy(new AvoidLastStrategy());
            usedStrategy = "AvoidLast";
          } else if (usedStrategy.equals("AvoidLast")) {
            AiPlayer.setStrategy(new LeastUsedStrategy());
            usedStrategy = "LeastUsed";
          }
        }
      }

      Strategies currentStrategy = this.AiPlayer.getStrategy();
      currentStrategy.setColourHistory(this.history);

      if (currentStrategy instanceof AvoidLastStrategy) {
        currentStrategy.setHumanPreviousChoice(this.humanPreviousChoice);
      }
    }

    this.humanPlayer.getChoices(this);
    this.AiPlayer.getChoices(this);

    Colour humanChoice = this.humanPlayer.chosenColour();
    Colour humanGuess = this.humanPlayer.guessedColour();

    this.history.add(humanChoice);

    Colour aiChoice = this.AiPlayer.chosenColour();
    Colour aiGuess = this.AiPlayer.guessedColour();

    MessageCli.PRINT_INFO_MOVE.printMessage(this.humanPlayer.getName(), humanChoice, humanGuess);

    MessageCli.PRINT_INFO_MOVE.printMessage(this.AiPlayer.getName(), aiChoice, aiGuess);

    if (this.thisRound % 3 == 0) {
      this.powerColour = Colour.getRandomColourForPowerColour();
      MessageCli.PRINT_POWER_COLOUR.printMessage(this.powerColour);
    } else {
      this.powerColour = null;
    }

    int humanScore = 0;
    int aiScore = 0;
    this.pointsScored = false;

    if (humanGuess == aiChoice) {
      humanScore += 1;
      if (humanGuess == this.powerColour) {
        humanScore += 2;
      }
    }

    if (aiGuess == humanChoice) {
      aiScore += 1;
      this.pointsScored = true;
      if (aiGuess == this.powerColour) {
        aiScore += 2;
      }
    }

    this.humanPlayer.addPoints(humanScore);
    this.AiPlayer.addPoints(aiScore);

    MessageCli.PRINT_OUTCOME_ROUND.printMessage(
        this.humanPlayer.getName(), String.valueOf(humanScore));

    MessageCli.PRINT_OUTCOME_ROUND.printMessage(this.AiPlayer.getName(), String.valueOf(aiScore));
    this.thisRound++;
    this.humanPreviousChoice = humanChoice;

    if (this.thisRound > this.totalRounds) {
      printEndingStats();
      this.gameInProgress = false;
      return;
    }
  }

  private void printEndingStats() {
    MessageCli.PRINT_END_GAME.printMessage();

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.humanPlayer.getName(), Integer.valueOf(this.humanPlayer.getScore()));

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.AiPlayer.getName(), Integer.valueOf(this.AiPlayer.getScore()));

    if (this.humanPlayer.getScore() == this.AiPlayer.getScore()) {
      MessageCli.PRINT_TIE_GAME.printMessage();
    } else if (this.humanPlayer.getScore() < this.AiPlayer.getScore()) {
      MessageCli.PRINT_WINNER_GAME.printMessage(this.AiPlayer.getName());
    } else {
      MessageCli.PRINT_WINNER_GAME.printMessage(this.humanPlayer.getName());
    }
  }

  public void showStats() {
    if (!gameInProgress) {
      MessageCli.GAME_NOT_STARTED.printMessage();
      return;
    }

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.humanPlayer.getName(), Integer.valueOf(this.humanPlayer.getScore()));

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.AiPlayer.getName(), Integer.valueOf(this.AiPlayer.getScore()));
  }
}
