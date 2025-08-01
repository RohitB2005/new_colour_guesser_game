package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.cli.MessageCli;
import nz.ac.auckland.se281.cli.Utils;
import nz.ac.auckland.se281.engine.Game;
import nz.ac.auckland.se281.model.Colour;

public class HumanPlayer extends Players {

  public HumanPlayer(String name) {
    super(name);
  }

  // Method to get the choice and guess of the human player
  @Override
  public void getChoices(Game thisRound) {
    // Continually reprompt until inputs are valid.
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

      // Set reprompt to false and set the choices
      this.chosenColour = chosenColour;
      this.guessedColour = guessedColour;
      reprompt = false;
    }
  }
}
