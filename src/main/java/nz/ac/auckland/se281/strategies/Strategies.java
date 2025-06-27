package nz.ac.auckland.se281.strategies;

import java.util.ArrayList;
import nz.ac.auckland.se281.model.Colour;

public interface Strategies {

  default void setHumanPreviousChoice(Colour choice) {}

  default void setColourHistory(ArrayList<Colour> history) {}

  default void setHumanGuessHistory(ArrayList<Colour> guessHistory) {}

  Colour chosenColour();

  Colour guessedColour();
}
