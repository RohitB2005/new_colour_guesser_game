package nz.ac.auckland.se281.strategies;

import nz.ac.auckland.se281.model.Colour;

public interface Strategies {

  default void setHumanPreviousChoice(Colour choice) {}

  Colour chosenColour();

  Colour guessedColour();
}
