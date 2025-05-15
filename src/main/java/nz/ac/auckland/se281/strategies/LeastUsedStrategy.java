package nz.ac.auckland.se281.strategies;

import java.util.ArrayList;
import nz.ac.auckland.se281.model.Colour;

public class LeastUsedStrategy implements Strategies {
  private ArrayList<Colour> history;

  public LeastUsedStrategy() {
    this.history = new ArrayList<>();
  }

  @Override
  public void setColourHistory(ArrayList<Colour> history) {
    this.history = history;
  }

  @Override
  public Colour chosenColour() {
    return Colour.getRandomColourForAi();
  }

  @Override
  public Colour guessedColour() {
    int countRed = 0;
    int countBlue = 0;
    int countGreen = 0;
    int countYellow = 0;

    for (Colour choice : this.history) {
      switch (choice) {
        case RED:
          countRed++;
          break;
        case BLUE:
          countBlue++;
          break;
        case GREEN:
          countGreen++;
          break;
        case YELLOW:
          countYellow++;
          break;
      }
    }

    int leastUsed = countRed;
    leastUsed = Math.min(leastUsed, countBlue);
    leastUsed = Math.min(leastUsed, countGreen);
    leastUsed = Math.min(leastUsed, countYellow);

    if (leastUsed == countRed) {
      return Colour.RED;
    } else if (leastUsed == countBlue) {
      return Colour.BLUE;
    } else if (leastUsed == countGreen) {
      return Colour.GREEN;
    } else {
      return Colour.YELLOW;
    }
  }

  public void updateHistory(Colour humanChoice) {
    history.add(humanChoice);
  }
}
