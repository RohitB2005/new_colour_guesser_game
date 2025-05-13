package nz.ac.auckland.se281.engine;

import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.cli.MessageCli;

public class Game {

  private int thisRound;
  private int totalRounds;
  private String namePlayer;
  private boolean gameInProgress;

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
  }

  public void play() {

    if (!gameInProgress) {
      MessageCli.GAME_NOT_STARTED.printMessage();
      return;
    }

    if (thisRound > totalRounds) {
      MessageCli.PRINT_END_GAME.printMessage();
    }

    thisRound++;
  }

  public void showStats() {}
}
