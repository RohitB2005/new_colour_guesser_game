package nz.ac.auckland.se281.strategies;

import nz.ac.auckland.se281.model.Colour;

public class RandomStrategy implements Strategies {
  @Override
  public Colour chosenColour() {
    return Colour.getRandomColourForAi();
  }

  @Override
  public Colour guessedColour() {
    return Colour.getRandomColourForAi();
  }
}
