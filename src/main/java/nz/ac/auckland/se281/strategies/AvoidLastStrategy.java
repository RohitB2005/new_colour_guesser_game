package nz.ac.auckland.se281.strategies;

import nz.ac.auckland.se281.model.Colour;

public class AvoidLastStrategy implements Strategies {

  private Colour humanPreviousChoice;

  @Override
  public void setHumanPreviousChoice(Colour choice) {
    this.humanPreviousChoice = choice;
  }

  @Override
  public Colour chosenColour() {
    return Colour.getRandomColourForAi();
  }

  @Override
  public Colour guessedColour() {
    if (this.humanPreviousChoice != null) {
      return Colour.getRandomColourExcluding(this.humanPreviousChoice);
    } else {
      return Colour.getRandomColourForAi();
    }
  }
}
