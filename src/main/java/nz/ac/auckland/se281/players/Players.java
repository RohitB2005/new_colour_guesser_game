package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.engine.Game;
import nz.ac.auckland.se281.model.Colour;

public abstract class Players {
  protected String name;
  protected int score;

  protected Colour chosenColour;
  protected Colour guessedColour;

  public Players(String name) {
    this.name = name;
    this.score = 0;
  }

  public String getName() {
    return this.name;
  }

  public int getScore() {
    return this.score;
  }

  public void addPoints(int points) {
    this.score += points;
  }

  public Colour chosenColour() {
    return this.chosenColour;
  }

  public Colour guessedColour() {
    return this.guessedColour;
  }

  public abstract void getChoices(Game thisRound);

  public void resetChoices() {
    this.chosenColour = null;
    this.guessedColour = null;
  }
}
