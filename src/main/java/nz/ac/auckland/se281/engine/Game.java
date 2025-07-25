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
  private AiPlayer aiPlayer;

  private Colour powerColour;

  private Difficulty thisDifficulty;
  private Colour humanPreviousChoice;
  private ArrayList<Colour> history;
  private ArrayList<Colour> guessHistory;
  private String usedStrategy;
  private boolean pointsScored;

  public Game() {
    this.thisRound = 1;
    this.gameInProgress = false;
    this.humanPreviousChoice = null;
    this.history = new ArrayList<>();
    this.guessHistory = new ArrayList<>();
    this.pointsScored = false;
  }

  // newGame uses the factory AI consstructor to create the AI with the chosen difficulty.
  public void newGame(Difficulty difficulty, int numRounds, String[] options) {
    // Construct instances of the AI and human player, welcoming the player
    this.humanPlayer = new HumanPlayer(options[0]);
    this.aiPlayer = Factory.constructAi(difficulty);

    MessageCli.WELCOME_PLAYER.printMessage(this.humanPlayer.getName());

    // Initialise all fields needed for AI and human at the start of a game
    this.totalRounds = numRounds;
    this.thisRound = 1;
    this.gameInProgress = true;

    this.thisDifficulty = difficulty;
    this.humanPreviousChoice = null;
    this.history.clear();
    this.guessHistory.clear();
    this.pointsScored = false;
    this.usedStrategy = "Random";
  }

  public void play() {

    if (!gameInProgress) {
      MessageCli.GAME_NOT_STARTED.printMessage();
      return;
    }

    // Keeps track of round number
    MessageCli.START_ROUND.printMessage(
        String.valueOf(this.thisRound), String.valueOf(this.totalRounds));

    // Simply sets the human's last choice, and gets the MEDIUM strategy.
    if (this.thisDifficulty == Difficulty.MEDIUM) {
      this.aiPlayer.getStrategy().setHumanPreviousChoice(this.humanPreviousChoice);
    }

    // Added many checks for hard difficulty, determining which strategy to use based on the
    // conditions of the game.
    if (this.thisDifficulty == Difficulty.HARD) {
      if (this.thisRound == 1 || this.thisRound == 2) {
        aiPlayer.setStrategy(new RandomStrategy());
        usedStrategy = "Random";
      } else if (this.thisRound == 3) {
        aiPlayer.setStrategy(new LeastUsedStrategy());
        usedStrategy = "LeastUsed";
      } else {
        if (this.pointsScored == false) {
          if (usedStrategy.equals("LeastUsed")) {
            aiPlayer.setStrategy(new AvoidLastStrategy());
            usedStrategy = "AvoidLast";
          } else if (usedStrategy.equals("AvoidLast")) {
            aiPlayer.setStrategy(new LeastUsedStrategy());
            usedStrategy = "LeastUsed";
          }
        }
      }

      // Sets the move history of the player
      Strategies currentStrategy = this.aiPlayer.getStrategy();
      currentStrategy.setColourHistory(this.history);
      currentStrategy.setHumanGuessHistory(this.guessHistory);

      currentStrategy.setHumanPreviousChoice(this.humanPreviousChoice);
    }

    if (this.thisDifficulty == Difficulty.NIGHTMARE) {
      Strategies currentStrategy = this.aiPlayer.getStrategy();
      currentStrategy.setHumanGuessHistory(this.guessHistory);
      currentStrategy.setColourHistory(this.history);
    }

    // Get choices and guesses for both players and print this information.
    this.humanPlayer.getChoices(this);
    this.aiPlayer.getChoices(this);

    Colour humanChoice = this.humanPlayer.chosenColour();
    Colour humanGuess = this.humanPlayer.guessedColour();

    this.history.add(humanChoice);
    this.guessHistory.add(humanGuess);

    Colour aiChoice = this.aiPlayer.chosenColour();
    Colour aiGuess = this.aiPlayer.guessedColour();

    MessageCli.PRINT_INFO_MOVE.printMessage(this.humanPlayer.getName(), humanChoice, humanGuess);

    MessageCli.PRINT_INFO_MOVE.printMessage(this.aiPlayer.getName(), aiChoice, aiGuess);

    // Power colour mechanic, used in round multiples of 3.
    if (this.thisRound % 3 == 0) {
      this.powerColour = Colour.getRandomColourForPowerColour();
      MessageCli.PRINT_POWER_COLOUR.printMessage(this.powerColour);
    } else {
      this.powerColour = null;
    }

    // Handling point scoring, adding differing number of points for different cases
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
    this.aiPlayer.addPoints(aiScore);

    // Print the outcomes and increment/set required values needed for the next cycle.
    MessageCli.PRINT_OUTCOME_ROUND.printMessage(
        this.humanPlayer.getName(), String.valueOf(humanScore));

    MessageCli.PRINT_OUTCOME_ROUND.printMessage(this.aiPlayer.getName(), String.valueOf(aiScore));
    this.thisRound++;
    this.humanPreviousChoice = humanChoice;

    if (this.thisRound > this.totalRounds) {
      printEndingStats();
      this.gameInProgress = false;
    }
  }

  // This helper method prints the total player points of each player then determines which player
  // won the game, printing this
  private void printEndingStats() {
    // Print end game message and points of each player
    MessageCli.PRINT_END_GAME.printMessage();

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.humanPlayer.getName(), Integer.valueOf(this.humanPlayer.getScore()));

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.aiPlayer.getName(), Integer.valueOf(this.aiPlayer.getScore()));

    // Handles logic for determining which player won, or a tie
    if (this.humanPlayer.getScore() == this.aiPlayer.getScore()) {
      MessageCli.PRINT_TIE_GAME.printMessage();
    } else if (this.humanPlayer.getScore() < this.aiPlayer.getScore()) {
      MessageCli.PRINT_WINNER_GAME.printMessage(this.aiPlayer.getName());
    } else {
      MessageCli.PRINT_WINNER_GAME.printMessage(this.humanPlayer.getName());
    }
  }

  // Showstats simply shows the points of each player when called.
  public void showStats() {
    // Check to deny command use with no active game.
    if (!gameInProgress) {
      MessageCli.GAME_NOT_STARTED.printMessage();
      return;
    }

    // Print points of each player using MessageCli
    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.humanPlayer.getName(), Integer.valueOf(this.humanPlayer.getScore()));

    MessageCli.PRINT_PLAYER_POINTS.printMessage(
        this.aiPlayer.getName(), Integer.valueOf(this.aiPlayer.getScore()));
  }

  public RoundResult playRound(Colour humanChoice, Colour humanGuess) {

    if (!gameInProgress) {
      return null;
    }

    // Simply sets the human's last choice, and gets the MEDIUM strategy.
    if (this.thisDifficulty == Difficulty.MEDIUM) {
      this.aiPlayer.getStrategy().setHumanPreviousChoice(this.humanPreviousChoice);
    }

    // Added many checks for hard difficulty, determining which strategy to use based on the
    // conditions of the game.
    if (this.thisDifficulty == Difficulty.HARD) {
      if (this.thisRound == 1 || this.thisRound == 2) {
        aiPlayer.setStrategy(new RandomStrategy());
        usedStrategy = "Random";
      } else if (this.thisRound == 3) {
        aiPlayer.setStrategy(new LeastUsedStrategy());
        usedStrategy = "LeastUsed";
      } else {
        if (this.pointsScored == false) {
          if (usedStrategy.equals("LeastUsed")) {
            aiPlayer.setStrategy(new AvoidLastStrategy());
            usedStrategy = "AvoidLast";
          } else if (usedStrategy.equals("AvoidLast")) {
            aiPlayer.setStrategy(new LeastUsedStrategy());
            usedStrategy = "LeastUsed";
          }
        }
      }

      // Sets the move history of the player
      Strategies currentStrategy = this.aiPlayer.getStrategy();
      currentStrategy.setColourHistory(this.history);
      currentStrategy.setHumanGuessHistory(this.guessHistory);

      currentStrategy.setHumanPreviousChoice(this.humanPreviousChoice);
    }

    if (this.thisDifficulty == Difficulty.NIGHTMARE) {
      Strategies currentStrategy = this.aiPlayer.getStrategy();
      currentStrategy.setHumanGuessHistory(this.guessHistory);
      currentStrategy.setColourHistory(this.history);
    }

    Colour aiChoice = this.aiPlayer.getStrategy().chosenColour();
    Colour aiGuess = this.aiPlayer.getStrategy().guessedColour();

    this.history.add(humanChoice);
    this.guessHistory.add(humanGuess);

    // Power colour mechanic, used in round multiples of 3.
    if (this.thisRound % 3 == 0) {
      this.powerColour = Colour.getRandomColourForPowerColour();
    } else {
      this.powerColour = null;
    }

    // Handling point scoring, adding differing number of points for different cases
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
    this.aiPlayer.addPoints(aiScore);
    this.thisRound++;
    this.humanPreviousChoice = humanChoice;

    return new RoundResult(aiChoice, aiGuess, humanScore, aiScore, getPlayerScore(), getAiScore());
  }

  public boolean isGameOver() {
    return !gameInProgress || this.thisRound > this.totalRounds;
  }

  public boolean isGameInProgress() {
    return this.gameInProgress;
  }

  public int getCurrentRound() {
    return this.thisRound;
  }

  public int getTotalRounds() {
    return this.totalRounds;
  }

  public int getPlayerScore() {
    return this.humanPlayer.getScore();
  }

  public int getAiScore() {
    return this.aiPlayer.getScore();
  }

  public Colour getPowerColour() {
    return this.powerColour;
  }

  public String getPlayerName() {
    return this.humanPlayer.getName();
  }
}
