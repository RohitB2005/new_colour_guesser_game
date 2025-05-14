package nz.ac.auckland.se281.strategies;

import nz.ac.auckland.se281.model.Colour;

public interface Strategies {
  Colour chosenColour();

  Colour guessedColour();
}
