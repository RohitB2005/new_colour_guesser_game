package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.strategies.AvoidLastStrategy;
import nz.ac.auckland.se281.strategies.LeastUsedStrategy;
import nz.ac.auckland.se281.strategies.RandomStrategy;

public class Factory {

  public static AiPlayer constructAi(Difficulty difficulty) {
    AiPlayer ai = new AiPlayer();

    switch (difficulty) {
      case EASY:
        ai.setStrategy(new RandomStrategy());
        break;

      case MEDIUM:
        ai.setStrategy(new AvoidLastStrategy());
        break;
      case HARD:
        ai.setStrategy(new LeastUsedStrategy());
        break;
      default:
        break;
    }
    return ai;
  }
}
