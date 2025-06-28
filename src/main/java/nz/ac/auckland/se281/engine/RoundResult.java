package nz.ac.auckland.se281.engine;

import nz.ac.auckland.se281.model.Colour;

public class RoundResult {
  private final Colour aiChoice;
  private final Colour aiGuess;
  private final int humanPoints;
  private final int aiPoints;
  private final int humanTotalScore;
  private final int aiTotalScore;

  public RoundResult(
      Colour aiChoice,
      Colour aiGuess,
      int humanPoints,
      int aiPoints,
      int humanTotalScore,
      int aiTotalScore) {
    this.aiChoice = aiChoice;
    this.aiGuess = aiGuess;
    this.humanPoints = humanPoints;
    this.aiPoints = aiPoints;
    this.humanTotalScore = humanTotalScore;
    this.aiTotalScore = aiTotalScore;
  }

  // Getters for all fields
  public Colour getAiChoice() {
    return aiChoice;
  }

  public Colour getAiGuess() {
    return aiGuess;
  }

  public int getHumanPoints() {
    return humanPoints;
  }

  public int getAiPoints() {
    return aiPoints;
  }

  public int getHumanTotalScore() {
    return humanTotalScore;
  }

  public int getAiTotalScore() {
    return aiTotalScore;
  }
}
