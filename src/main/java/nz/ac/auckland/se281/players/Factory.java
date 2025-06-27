package nz.ac.auckland.se281.players;

import nz.ac.auckland.se281.Main.Difficulty;
import nz.ac.auckland.se281.strategies.AvoidLastStrategy;
import nz.ac.auckland.se281.strategies.LeastUsedStrategy;
import nz.ac.auckland.se281.strategies.NightmareStrategy;
import nz.ac.auckland.se281.strategies.RandomStrategy;

public class Factory {

  // Method to create an Ai Player to set strategy based on difficulty chosen
  public static AiPlayer constructAi(Difficulty difficulty) {
    // Initialise new Ai Player
    AiPlayer ai = new AiPlayer();

    // Switch statement sets AI strategy with different difficulties.
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
      case NIGHTMARE:
        ai.setStrategy(new NightmareStrategy());
        break;
      default:
        break;
    }
    return ai;
  }
}
