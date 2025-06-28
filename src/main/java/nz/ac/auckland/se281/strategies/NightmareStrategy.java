package nz.ac.auckland.se281.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nz.ac.auckland.se281.model.Colour;

public class NightmareStrategy implements Strategies {
  private ArrayList<Colour> history;
  private ArrayList<Colour> guessHistory;
  private Map<Colour, Map<Colour, Integer>> transitionMap;

  public NightmareStrategy() {
    this.history = new ArrayList<>();
    this.guessHistory = new ArrayList<>();
    this.transitionMap = new HashMap<>();
    for (Colour thisColour : Colour.values()) {
      transitionMap.put(thisColour, new HashMap<>());
    }
  }

  @Override
  public Colour chosenColour() {
    if (guessHistory.isEmpty()) {
      return Colour.getRandomColourForAi();
    }

    Map<Colour, Integer> countGuesses = new HashMap<>();
    for (Colour thisColour : Colour.values()) {
      countGuesses.put(thisColour, 0);
    }

    for (Colour guess : guessHistory) {
      countGuesses.put(guess, countGuesses.get(guess) + 1);
    }

    int leastGuessed = 2147483647;
    for (Integer count : countGuesses.values()) {
      if (count < leastGuessed) {
        leastGuessed = count;
      }
    }

    for (Colour thisColour : Colour.values()) {
      if (countGuesses.get(thisColour) == leastGuessed) {
        return thisColour;
      }
    }
    return Colour.getRandomColourForAi();
  }

  @Override
  public void setHumanGuessHistory(ArrayList<Colour> guessHistory) {
    this.guessHistory = guessHistory;
  }

  @Override
  public void setColourHistory(ArrayList<Colour> history) {
    this.history = history;
    updateMap();
  }

  private void updateMap() {
    if (this.history.size() < 2) {
      return;
    }

    for (Colour thisColour : Colour.values()) {
      transitionMap.get(thisColour).clear();
    }

    for (int i = 0; i < history.size() - 1; i++) {
      Colour prevChoice = history.get(i);
      Colour nextChoice = history.get(i + 1);
      Map<Colour, Integer> transitions = transitionMap.get(prevChoice);
      transitions.put(nextChoice, transitions.getOrDefault(nextChoice, 0) + 1);
    }
  }

  @Override
  public Colour guessedColour() {
    if (history.isEmpty()) {
      return Colour.getRandomColourForAi();
    }

    Colour previousChoice = history.get(history.size() - 1);
    Map<Colour, Integer> likelyNextChoice = transitionMap.get(previousChoice);

    if (likelyNextChoice.isEmpty()) {
      return new LeastUsedStrategy(history).guessedColour();
    }

    Colour guessChoice = null;
    int maxCount = -1;
    for (Map.Entry<Colour, Integer> entry : likelyNextChoice.entrySet()) {
      if (entry.getValue() > maxCount) {
        maxCount = entry.getValue();
        guessChoice = entry.getKey();
      }
    }
    return guessChoice;
  }
}
