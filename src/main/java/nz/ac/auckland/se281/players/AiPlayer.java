package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.engine.Game;
import nz.ac.auckland.se281.strategies.Strategies;

public class AiPlayer extends Players {
  public static final String AI_NAME = "HAL-9000";
  private Strategies usedStrategy;

  public AiPlayer() {
    super(AI_NAME);
  }

  public void setStrategy(Strategies thisStrategy) {
    this.usedStrategy = thisStrategy;
  }

  public Strategies getStrategy() {
    return usedStrategy;
  }

  @Override
  public void getChoices(Game thisRound) {
    this.chosenColour = usedStrategy.chosenColour();
    this.guessedColour = usedStrategy.guessedColour();
  }
}
