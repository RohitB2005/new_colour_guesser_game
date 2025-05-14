package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.strategies.AvoidLastStrategy;
import nz.ac.auckland.se281.strategies.RandomStrategy;

public class Factory {

  public static AiPlayer aiConstructor(Difficulty difficulty) {
    AiPlayer Ai = new AiPlayer();

    switch (difficulty) {
      case EASY:
        Ai.setStrategy(new RandomStrategy());
        break;

      case MEDIUM:
        Ai.setStrategy(new AvoidLastStrategy());
        break;
      default:
        break;
    }
    return Ai;
  }
}
