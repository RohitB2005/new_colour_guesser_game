package nz.ac.auckland.se281.engine;

import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.cli.MessageCli;
import nz.ac.auckland.se281.cli.Utils;
import nz.ac.auckland.se281.model.Colour;

public class Game {

  private int thisRound;
  private int totalRounds;
  private String namePlayer;
  private boolean gameInProgress;

  private Colour chosenColour;
  private Colour guessedColour;
  private Colour halChosenColour;
  private Colour halGuessedColour;
  private Colour powerColour;

  public static String AI_NAME = "HAL-9000";

  public Game() {
    this.thisRound = 1;
    this.gameInProgress = false;
  }

  public void newGame(Difficulty difficulty, int numRounds, String[] options) {
    this.namePlayer = options[0];
    MessageCli.WELCOME_PLAYER.printMessage(this.namePlayer);
    this.totalRounds = numRounds;
    this.thisRound = 1;
    this.gameInProgress = true;
    this.chosenColour = null;
    this.guessedColour = null;
  }

  public void play() {

    if (!gameInProgress) {
      MessageCli.GAME_NOT_STARTED.printMessage();
      return;
    }

    if (this.thisRound > this.totalRounds) {
      MessageCli.PRINT_END_GAME.printMessage();
      return;
    }

    MessageCli.START_ROUND.printMessage(
        String.valueOf(this.thisRound), String.valueOf(this.totalRounds));

    while (true) {
      MessageCli.ASK_HUMAN_INPUT.printMessage();

      String input = Utils.scanner.nextLine();
      String[] colourInputs = input.trim().split(" ");

      if (colourInputs.length != 2) {
        MessageCli.INVALID_HUMAN_INPUT.printMessage();
        continue;
      }

      Colour chosenColour = Colour.fromInput(colourInputs[0]);
      Colour guessedColour = Colour.fromInput(colourInputs[1]);

      if (chosenColour == null || guessedColour == null) {
        MessageCli.INVALID_HUMAN_INPUT.printMessage();
        continue;
      }

      this.chosenColour = chosenColour;
      this.guessedColour = guessedColour;
      this.halChosenColour = Colour.getRandomColourForAi();
      this.halGuessedColour = Colour.getRandomColourForAi();

      MessageCli.PRINT_INFO_MOVE.printMessage(
          this.namePlayer, this.chosenColour, this.guessedColour);

      MessageCli.PRINT_INFO_MOVE.printMessage(AI_NAME, this.halChosenColour, this.halGuessedColour);

      if (this.thisRound % 3 == 0) {
        this.powerColour = Colour.getRandomColourForPowerColour();
        MessageCli.PRINT_POWER_COLOUR.printMessage(this.powerColour);
      }

      int playerScore = 0;
      int halScore = 0;

      if (this.guessedColour == halChosenColour) {
        playerScore += 1;
        if (this.guessedColour == this.powerColour) {
          playerScore += 2;
        }
      }

      if (halGuessedColour == this.chosenColour) {
        halScore += 1;
        if (halGuessedColour == this.powerColour) {
          halScore += 2;
        }
      }

      MessageCli.PRINT_OUTCOME_ROUND.printMessage(this.namePlayer, String.valueOf(playerScore));

      MessageCli.PRINT_OUTCOME_ROUND.printMessage(AI_NAME, String.valueOf(halScore));
      this.thisRound++;
      break;
    }
  }

  public void showStats() {}
}
