package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.cli.MessageCli;
import nz.ac.auckland.se281.cli.Utils;
import nz.ac.auckland.se281.engine.Game;
import nz.ac.auckland.se281.model.Colour;

public class HumanPlayer extends Players {

  public HumanPlayer(String name) {
    super(name);
  }

  @Override
  public void getChoices(Game thisRound) {
    boolean reprompt = true;
    while (reprompt) {
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
      reprompt = false;
    }
  }
}
