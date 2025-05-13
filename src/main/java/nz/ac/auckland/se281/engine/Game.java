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
    this.thisRound++;

    MessageCli.ASK_HUMAN_INPUT.printMessage();

    String input = Utils.scanner.nextLine();
    String[] colourInputs = input.trim().split(" ");

    if (input.length() != 2) {
      MessageCli.INVALID_HUMAN_INPUT.printMessage();
    }

    Colour chosenColour = Colour.fromInput(colourInputs[0]);
    Colour guessedColour = Colour.fromInput(colourInputs[1]);

    if (chosenColour == null || guessedColour == null) {
      MessageCli.INVALID_HUMAN_INPUT.printMessage();
    }

    this.chosenColour = chosenColour;
    this.guessedColour = guessedColour;

    MessageCli.PRINT_INFO_MOVE.printMessage(this.namePlayer, this.chosenColour, this.guessedColour);
  }

  public void showStats() {}
}
