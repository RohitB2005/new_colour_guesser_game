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
  }

  public void showStats() {}

  String inputLine = Utils.scanner.nextLine();
}
