package nz.ac.auckland.se281;

import static nz.ac.auckland.se281.Main.Command.*;
import static nz.ac.auckland.se281.cli.MessageCli.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nz.ac.auckland.se281.cli.Utils;
import nz.ac.auckland.se281.model.Colour;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MainTest.Task1.class,
  MainTest.Task2.class,
  MainTest.Task3.class,
  MainTest.Task4.class,
  MainTest.Task5.class,
  MainTest.YourTests.class
})
public class MainTest {

  private static final String AI_NAME = "HAL-9000";

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task1 extends SysCliTest {

    public Task1() {
      super(Main.class);
    }

    @Test
    public void T1_01_welcome_message() throws Exception {
      runCommands(NEW_GAME + " EASY 5", "Valerio");
      assertContains(WELCOME_PLAYER.getMessage("Valerio"));
    }

    @Test
    public void T1_02_play_start_round() throws Exception {
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "R B");
      assertContains(START_ROUND.getMessage("1", "5"));
      assertDoesNotContain(START_ROUND.getMessage("0", "5"));
      assertDoesNotContain(START_ROUND.getMessage("2", "5"));
      assertDoesNotContain(START_ROUND.getMessage("1", "0"));
    }

    @Test
    public void T1_03_play_second_round() throws Exception {
      runCommands(
          NEW_GAME + " EASY 6",
          "Valerio", //
          PLAY,
          "RED GREEN", //
          PLAY,
          "BLUE YELLOW");
      assertContains(START_ROUND.getMessage("1", "6"));
      assertContains(START_ROUND.getMessage("2", "6"));
      assertDoesNotContain(START_ROUND.getMessage("3", "6"));
    }

    @Test
    public void T1_04_play_second_round() throws Exception {
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "RED GREEN", //
          PLAY,
          "BLUE YELLOW");
      assertContains(START_ROUND.getMessage("1", "2"));
      assertContains(START_ROUND.getMessage("2", "2"));
      assertDoesNotContain(START_ROUND.getMessage("0", "2"));
    }

    @Test
    public void T1_05_asks_input_when_playing() throws Exception {
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "R B");
      assertContains(ASK_HUMAN_INPUT.getMessage());
    }

    @Test
    public void T1_06_asks_input_when_playing_invalid_input() throws Exception {
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "rrr rrrr",
          "G B");
      assertContains(ASK_HUMAN_INPUT.getMessage());
    }

    @Test
    public void T1_07_invalid_input_then_valid() throws Exception {
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "This is not a valid input",
          "G Y");
      assertContains(INVALID_HUMAN_INPUT.getMessage());
      assertContains(START_ROUND.getMessage("1", "3"));
      assertDoesNotContain(START_ROUND.getMessage("2", "5"));
      assertContains(ASK_HUMAN_INPUT.getMessage());
    }

    @Test
    public void T1_08_prints_player_move_info() throws Exception {
      runCommands(
          NEW_GAME + " EASY 1",
          "Valerio", //
          PLAY,
          "RED GREEN");
      assertContains(PRINT_INFO_MOVE.getMessage("Valerio", "RED", "GREEN"));
    }

    @Test
    public void T1_09_input_is_case_insensitive() throws Exception {
      runCommands(
          NEW_GAME + " EASY 1",
          "Valerio", //
          PLAY,
          "g g");
      assertContains(PRINT_INFO_MOVE.getMessage("Valerio", "GREEN", "GREEN"));
    }

    @Test
    public void T1_10_invalid_input_then_valid_across_rounds() throws Exception {
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "INVALID INPUT",
          "G Y", //
          PLAY,
          "Y G");

      assertContainsAtRound(INVALID_HUMAN_INPUT.getMessage(), 1);
      assertDoesNotContainAtRound(INVALID_HUMAN_INPUT.getMessage(), 2);
    }

    @Test
    public void T1_11_input_with_three_colours_invalid() throws Exception {
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "RED GREEN BLUE",
          "RED GREEN");
      assertContainsAtRound(INVALID_HUMAN_INPUT.getMessage(), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "RED", "GREEN"), 1);
    }

    @Test
    public void T1_12_input_with_one_colour_invalid() throws Exception {
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "RED",
          "GREEN RED");
      assertContainsAtRound(INVALID_HUMAN_INPUT.getMessage(), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "GREEN", "RED"), 1);
    }

    @Test
    public void T1_13_input_with_invalid_colour_codes() throws Exception {
      runCommands(
          NEW_GAME + " EASY 1",
          "Valerio", //
          PLAY,
          "purple pink",
          "B G");
      assertContainsAtRound(INVALID_HUMAN_INPUT.getMessage(), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "BLUE", "GREEN"), 1);
    }

    @Test
    public void T1_14_announce_power_colour_on_third_round() throws Exception {
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio",
          PLAY,
          "RED GREEN",
          PLAY,
          "BLUE YELLOW",
          PLAY,
          "YELLOW RED");

      assertDoesNotContainAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 1);
      assertDoesNotContainAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 2);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 3);
      assertContains(START_ROUND.getMessage("3", "5"));
    }

    @Test
    public void T1_15_announce_power_colour_on_third_round_different_seed() throws Exception {
      Utils.randomPowerNumber = new Random(19000);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio",
          PLAY,
          "RED GREEN",
          PLAY,
          "BLUE YELLOW",
          PLAY,
          "YELLOW RED");

      assertDoesNotContainAtRound(PRINT_POWER_COLOUR.getMessage("GREEN"), 1);
      assertDoesNotContainAtRound(PRINT_POWER_COLOUR.getMessage("GREEN"), 2);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("GREEN"), 3);
      assertContains(START_ROUND.getMessage("3", "5"));
    }

    @Test
    public void T1_16_announce_power_colour_on_sixth_round() throws Exception {
      Utils.randomPowerNumber = new Random(19876);
      runCommands(
          NEW_GAME + " EASY 10",
          "Valerio",
          PLAY,
          "RED GREEN",
          PLAY,
          "BLUE YELLOW",
          PLAY,
          "YELLOW RED",
          PLAY,
          "RED GREEN",
          PLAY,
          "BLUE YELLOW",
          PLAY,
          "YELLOW RED");
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("GREEN"), 3);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 6);
      assertContains(START_ROUND.getMessage("6", "10"));
    }

    @Test
    public void T1_17_prints_player_move_info() throws Exception {
      runCommands(NEW_GAME + " EASY 3", "Valerio", PLAY, "R GREEN");

      assertContains(PRINT_INFO_MOVE.getMessage("Valerio", "RED", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Valerio", "R", "GREEN"));
    }

    @Test
    public void T1_18_prints_player_move_info_multiple_rounds() throws Exception {
      runCommands(NEW_GAME + " EASY 3", "Valerio", PLAY, "RED GREEN", PLAY, "YELLOW BLUE");

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "RED", "GREEN"), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "YELLOW", "BLUE"), 2);
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task2 extends SysCliTest {

    public Task2() {
      super(Main.class);
    }

    @Test
    public void T2_01_random_play() throws Exception {
      Utils.randomAi = new Random(1);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "R G");
      assertContains(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Valerio", "BLUE", "RED"));
    }

    @Test
    public void T2_02_random_play_different_seed() throws Exception {
      Utils.randomAi = new Random(2);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "R G");
      assertContains(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"));
    }

    @Test
    public void T2_03_random_play_different_rounds() throws Exception {
      Utils.randomAi = new Random(2);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "R G",
          PLAY,
          "Y Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 2);
    }

    @Test
    public void T2_04_human_1_point_ai_0_point() throws Exception {
      Utils.randomAi = new Random(2);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "R B");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1));
      assertContains(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0));
    }

    @Test
    public void T2_05_human_1_point_ai_1_point() throws Exception {
      Utils.randomAi = new Random(2);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "G B");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1));
      assertContains(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1));
    }

    @Test
    public void T2_06_human_0_point_ai_1_point() throws Exception {
      Utils.randomAi = new Random(2);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "G Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0));
      assertContains(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1));
    }

    @Test
    public void T2_07_human_1_1_point_ai_1_0_point_2_rounds() throws Exception {
      Utils.randomAi = new Random(2);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "G B",
          PLAY,
          "B Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 2);
    }

    @Test
    public void T2_08_power_colour_guessed_by_human() throws Exception {
      Utils.randomAi = new Random(2);
      Utils.randomPowerNumber = new Random(465477);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "G B",
          PLAY,
          "B Y",
          PLAY,
          "B G");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 2);

      assertContains(PRINT_POWER_COLOUR.getMessage("GREEN"));
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "YELLOW"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 3), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);
    }

    @Test
    public void T2_09_power_colour_guessed_by_ai() throws Exception {
      Utils.randomAi = new Random(45);
      Utils.randomPowerNumber = new Random(4574875);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "B B",
          PLAY,
          "B R",
          PLAY,
          "R Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "YELLOW"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      assertContains(PRINT_POWER_COLOUR.getMessage("RED"));
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 3), 3);
    }

    @Test
    public void T2_10_power_colour_not_guessed() throws Exception {
      Utils.randomAi = new Random(1);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "G R",
          PLAY,
          "G G",
          PLAY,
          "R Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      assertContains(PRINT_POWER_COLOUR.getMessage("BLUE"));
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 3);
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task3 extends SysCliTest {

    public Task3() {
      super(Main.class);
    }

    @Test
    public void T3_01_first_round_is_random() throws Exception {
      Utils.randomAi = new Random(5555);
      runCommands(
          NEW_GAME + " MEDIUM 5",
          "Valerio", //
          PLAY,
          "R G");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 1);
    }

    @Test
    public void T3_02_second_round_change_strategy() throws Exception {
      Utils.randomAi = new Random(5555);
      runCommands(
          NEW_GAME + " MEDIUM 6",
          "Valerio", //
          PLAY,
          "R G",
          PLAY, // in the second round the game should switch strategy
          "Y Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 1);
      // this is random should not be this
      assertDoesNotContainAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 2);
      // this is the correct on as it avoids the Last Played Colour stategy
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "GREEN"), 2);
    }

    @Test
    public void T3_03_three_rounds() throws Exception {
      Utils.randomAi = new Random(56533333);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " MEDIUM 6",
          "Valerio", //
          PLAY,
          "R R",
          PLAY, // in the second round the game should switch strategy
          "G B",
          PLAY,
          "B B");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "BLUE"), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 2);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"), 3);
    }

    @Test
    public void T3_04_four_rounds() throws Exception {
      Utils.randomAi = new Random(27890);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " MEDIUM 6",
          "Valerio", //
          PLAY,
          "R R",
          PLAY, // in the second round the game should switch strategy
          "G B",
          PLAY,
          "B B",
          PLAY,
          "Y B");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "BLUE"), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "YELLOW"), 2);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 3);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"), 4);
    }

    @Test
    public void T3_05_four_rounds_with_correct_outcome() throws Exception {
      Utils.randomAi = new Random(72309812);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " MEDIUM 6",
          "Valerio", //
          PLAY,
          "R R",
          PLAY, // in the second round the game should switch strategy
          "B B",
          PLAY,
          "R G",
          PLAY,
          "Y B");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "BLUE"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 2);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "YELLOW"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 4);
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task4 extends SysCliTest {

    public Task4() {
      super(Main.class);
    }

    @Test
    public void T4_01_the_First_two_rounds_should_be_random() throws Exception {
      Utils.randomAi = new Random(370001);
      runCommands(
          NEW_GAME + " HARD 6",
          "Valerio", //
          PLAY,
          "R G",
          PLAY,
          "Y Y");
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "BLUE"), 2);
    }

    @Test
    public void T4_02_three_rounds_change_strategy() throws Exception {
      Utils.randomAi = new Random(4098);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " HARD 6",
          "Valerio", //
          PLAY,
          "R Y",
          PLAY,
          "B B",
          PLAY, // in the third round the game should switch strategy to least used
          "G Y");
      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "BLUE"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 3);
    }

    @Test
    public void T4_03_six_rounds_switch_strategy() throws Exception {
      Utils.randomAi = new Random(4098);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " HARD 6",
          "Valerio", //
          PLAY,
          "R Y",
          PLAY,
          "B B",
          PLAY, // in the third round the game should switch strategy to least
          "G Y", // AI wins so keeps the strategy
          PLAY,
          "Y Y", // AI wins so keeps the strategy
          PLAY,
          "B G", // AI lost so change strategy to AvoidLast
          PLAY,
          "R R");
      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "BLUE"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 3);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 3);

      // Round 4
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "YELLOW"), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 4);

      // Round 5
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 5);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 5);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 5);

      // Round 6
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"), 6);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 6);
      // 3 points because is a power colour
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 3), 6);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("RED"), 6);
    }

    @Test
    public void T4_04_six_rounds_switch_strategy() throws Exception {
      Utils.randomAi = new Random(849549584);
      Utils.randomPowerNumber = new Random(353537);
      runCommands(
          NEW_GAME + " HARD 6",
          "Valerio",
          PLAY,
          "B Y", // Round 1
          PLAY,
          "B B", // Round 2
          PLAY,
          "Y Y", // Round 3 - change to least used strategy
          PLAY,
          "G Y", // Round 4 - switch to Avoid last as AI did not get any points previoud round
          PLAY,
          "G G", // Round 5 - do not change stategy
          PLAY,
          "R R" // Round 6 - change again, bexause it lost
          );
      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("YELLOW"), 3);

      // Round 4
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "GREEN"), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 4);

      // Round 5
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"), 5);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 5);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 5);

      // Round 6
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 6);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 6);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 6);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("GREEN"), 6);
    }

    @Test
    public void T4_05_eight_rounds_switch_strategy() throws Exception {
      Utils.randomAi = new Random(6644);
      Utils.randomPowerNumber = new Random(111);
      runCommands(
          NEW_GAME + " HARD 10",
          "Valerio",
          PLAY,
          "B B", // Round 1
          PLAY,
          "B B", // Round 2
          PLAY,
          "Y Y", // Round 3
          PLAY,
          "G G", // Round 4
          PLAY,
          "G R", // Round 5
          PLAY,
          "R B", // Round 6
          PLAY,
          "R B", // Round 7
          PLAY,
          "R B" // Round 8
          );
      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 2);

      // Round 3 - change from random to leastused strategy
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 3);

      // Round 4 - change strategy
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 4);

      // Round 5 - change strategy
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 5);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 5);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 5);

      // Round 6 - change strategy
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "YELLOW"), 6);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 6);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 6);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 6);

      // Round 7 - change strategy
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 7);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 7);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 7);

      // Round 8
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "YELLOW"), 8);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 8);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 8);
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task5 extends SysCliTest {

    public Task5() {
      super(Main.class);
    }

    @Test
    public void T5_01_game_not_started_show_stats() throws Exception {
      runCommands(SHOW_STATS);
      assertContains(GAME_NOT_STARTED.getMessage());
    }

    @Test
    public void T5_02_game_not_started_play() throws Exception {
      runCommands(PLAY);
      assertContains(GAME_NOT_STARTED.getMessage());
    }

    @Test
    public void T5_03_game_started_show_stats() throws Exception {
      runCommands(NEW_GAME + " HARD 10", "Valerio", SHOW_STATS);
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 0));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 0));
    }

    @Test
    public void T5_04_shows_stats_1_round() throws Exception {
      Utils.randomAi = new Random(3434343);
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "B Y",
          SHOW_STATS);
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 1));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 0));
    }

    @Test
    public void T5_05_shows_stats_3_rounds() throws Exception {
      Utils.randomAi = new Random(3434343);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 4",
          "Valerio",
          PLAY,
          "B Y", // Round 1
          PLAY,
          "B R", // Round 2
          PLAY,
          "Y G", // Round 3
          SHOW_STATS);

      // Check game started correctly
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());

      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);

      // Final scores
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 1));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 1));
    }

    @Test
    public void T5_06_end_game_3_rounds_tie() throws Exception {
      Utils.randomAi = new Random(3434343);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio",
          PLAY,
          "B Y", // Round 1
          PLAY,
          "B R", // Round 2
          PLAY,
          "Y G" // Round 3
          );

      // Check game started correctly
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());

      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);

      // even if I don't run shows-stats the game terminates and shows the statistics
      // Final scores
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 1));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 1));
      // Game end and tie
      assertContains(PRINT_END_GAME.getMessage());
      assertContains(PRINT_TIE_GAME.getMessage());
    }

    @Test
    public void T5_07_game_should_not_end() throws Exception {
      Utils.randomAi = new Random(3434343);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 4",
          "Valerio",
          PLAY,
          "B Y", // Round 1
          PLAY,
          "B R", // Round 2
          PLAY,
          "Y G" // Round 3
          );

      // Check game started correctly
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());

      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "GREEN"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);

      // game is not over and I did not run show-stats so no output should be given
      assertDoesNotContain(PRINT_PLAYER_POINTS.getMessage("Valerio", 1));
      assertDoesNotContain(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 1));
      assertDoesNotContain(PRINT_END_GAME.getMessage());
      assertDoesNotContain(PRINT_TIE_GAME.getMessage());
    }

    @Test
    public void T5_08_game_human_win() throws Exception {
      Utils.randomAi = new Random(94398473);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 4",
          "Valerio",
          PLAY,
          "B Y", // Round 1
          PLAY,
          "R R", // Round 2
          PLAY,
          "Y G", // Round 3
          PLAY,
          "B G" // Round 4
          );

      // Game starts
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());

      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "YELLOW"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);
      // Round 4
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 4);

      // Final scores and winner
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 1));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 0));
      assertContains(PRINT_END_GAME.getMessage());
      assertContains(PRINT_WINNER_GAME.getMessage("Valerio"));
    }

    @Test
    public void T5_09_game_ai_win() throws Exception {
      Utils.randomAi = new Random(4444);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 4",
          "Valerio",
          PLAY,
          "G Y", // Round 1
          PLAY,
          "G R", // Round 2
          PLAY,
          "Y R", // Round 3
          PLAY,
          "R G" // Round 4
          );

      // Game starts
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());

      // Round 1
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 1);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 1);

      // Round 2
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 2);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1), 2);

      // Round 3
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 3);

      // Round 4
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 4);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 4);

      // Final scores and winner
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 0));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 1));
      assertContains(PRINT_END_GAME.getMessage());
      assertContains(PRINT_WINNER_GAME.getMessage(AI_NAME));
    }

    @Test
    public void T5_10_reset_state_new_game() throws Exception {
      Utils.randomAi = new Random(454545);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "B Y",
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "B G",
          PLAY,
          "R B",
          SHOW_STATS);
      // Ensure game state reset successfully
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());

      // First game, round 1
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 1));
      assertContains(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1));

      // Second game, round 2
      assertContains(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"));
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0));
      assertContains(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 1));

      // Final stats for second game only
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 0));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 1));
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class YourTests extends SysCliTest {

    public YourTests() {
      super(Main.class);
    }

    @Test
    public void YT_01_test_very_long_player_name() throws Exception {
      String longName = "Player".repeat(20); // 100 characters
      runCommands(NEW_GAME + " EASY 3", longName, PLAY, "B Y");
      assertContains(WELCOME_PLAYER.getMessage(longName));
    }

    @Test
    public void YT_02_test_multiple_invalid_inputs() throws Exception {
      runCommands(NEW_GAME + " EASY 3", "Valerio", PLAY, "INVALID1", "INVALID2", "INVALID3", "B Y");
      assertContainsAtRound(INVALID_HUMAN_INPUT.getMessage(), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "BLUE", "YELLOW"), 1);
    }

    @Test
    public void YT_03_test_game_mode_case_sensitivity() throws Exception {
      runCommands(
          NEW_GAME + " easy 3",
          "Valerio",
          PLAY,
          "B Y",
          NEW_GAME + " MEDIUM 3",
          "Valerio",
          PLAY,
          "R G",
          NEW_GAME + " hard 3",
          "Valerio",
          PLAY,
          "Y B");
      assertDoesNotContain(GAME_NOT_STARTED.getMessage());
    }

    @Test
    public void YT_04_new_game_resets_everything() throws Exception {
      Utils.randomAi = new Random(1);
      runCommands(
          NEW_GAME + " MEDIUM 3",
          "Valerio",
          PLAY,
          "R G",
          PLAY,
          "B Y",
          NEW_GAME + " MEDIUM 2",
          "Valerio",
          PLAY,
          "R G");
      assertContains(START_ROUND.getMessage("1", "3"));
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Valerio", "RED", "GREEN"), 1);
    }

    @Test
    public void YT_05_show_stats_before_game() throws Exception {
      runCommands(SHOW_STATS);
      assertContains(GAME_NOT_STARTED.getMessage());
    }

    @Test
    public void YT_06_least_used_strategy_with_repeat_colour() throws Exception {
      Utils.randomAi = new Random(123);
      runCommands(
          NEW_GAME + " HARD 4",
          "Valerio",
          PLAY, // Round 1 - random
          "R R",
          PLAY, // Round 2 - random
          "R R",
          PLAY, // Round 3 - switches to LeastUsed
          "G G");
      // RED used twice, GREEN once -> AI should least likely pick GREEN
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 3);
    }

    @Test
    public void YT_07_power_colour_bonus_points() throws Exception {
      Utils.randomPowerNumber = new Random(1); // BLUE selected
      runCommands(
          NEW_GAME + " EASY 3", "Valerio", PLAY, "R G", PLAY, "G Y", PLAY, "B B" // Round 3
          );
      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 3), 3);
    }

    @Test
    public void TX_1_stats_cleared_newgame_no_moves() throws Exception {
      Utils.randomAi = new Random(3434343);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "B Y",
          NEW_GAME + " EASY 3",
          "Valerio", //
          SHOW_STATS);
      // check stats cleared
      assertContains(PRINT_PLAYER_POINTS.getMessage("Valerio", 0));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, 0));
      assertDoesNotContain(PRINT_PLAYER_POINTS.getMessage("Valerio", 1));
    }

    @Test
    public void TX_2_round_count_high() throws Exception {
      Utils.randomAi = new Random(3434343);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 20",
          "Valerio",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y",
          PLAY,
          "B Y");
      // check stats cleared
      assertContains(START_ROUND.getMessage("20", "20"));
      assertContains(PRINT_END_GAME.getMessage());
      assertDoesNotContain(START_ROUND.getMessage("21", "20"));
      assertDoesNotContain(START_ROUND.getMessage("0", "20"));
    }

    @Test
    public void TX_3_invalid_input_manytimes() throws Exception {
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "some invalid input",
          "some different invalid input",
          "another invalid",
          "R B",
          PLAY,
          "wrong input",
          "random",
          "B Y");
      assertContains(INVALID_HUMAN_INPUT.getMessage());
      assertDoesNotContain(COMMAND_NOT_FOUND.getMessage());
    }

    @Test
    public void TX_4_input_colour_whitespace_before() throws Exception {
      // check whitespace trimmed before input
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "    B Y");
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0));
      assertDoesNotContain(INVALID_HUMAN_INPUT.getMessage());
    }

    @Test
    public void TX_5_input_colour_whitespace_after() throws Exception {
      // check whitespace trimmed after input
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "B Y  ",
          "B Y");
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0));
      assertDoesNotContain(INVALID_HUMAN_INPUT.getMessage());
    }

    @Test
    public void TX_7_stats_cleared_endgame() throws Exception {
      // showstats should not work at end of game
      Utils.randomAi = new Random(3434343);
      Utils.randomPowerNumber = new Random(1);
      runCommands(
          NEW_GAME + " EASY 2",
          "Valerio", //
          PLAY,
          "B Y",
          PLAY,
          "R B",
          SHOW_STATS);
      assertContains(GAME_NOT_STARTED.getMessage());
    }

    @Test
    public void TX_8_play_after_endgame() throws Exception {
      // play should not work at end of game
      runCommands(NEW_GAME + " EASY 2", "Valerio", PLAY, "B Y", PLAY, "R B");
      runCommands(PLAY);

      assertContains(GAME_NOT_STARTED.getMessage());
      assertDoesNotContain(PRINT_PLAYER_POINTS.getMessage(3, 2));
    }

    @Test
    public void TX_9_power_colour_guessed_but_no_points() throws Exception {
      // ai and player should not earn any points here as they did not correctly guess the player
      // colour
      Utils.randomAi = new Random(45);
      Utils.randomPowerNumber = new Random(4574875);
      runCommands(
          NEW_GAME + " EASY 5",
          "Valerio", //
          PLAY,
          "B B",
          PLAY,
          "B R",
          PLAY,
          "B R");
      assertContains(PRINT_POWER_COLOUR.getMessage("RED"));
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage("Valerio", 0), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, 0), 3);
    }

    @Test
    public void TX_01_invalid_input_then_valid_fixed() throws Exception {
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "This is not a valid input",
          "G Y");
      assertContains(INVALID_HUMAN_INPUT.getMessage());
      assertContains(START_ROUND.getMessage("1", "3"));
      assertDoesNotContain(START_ROUND.getMessage("2", "3"));
      assertContains(ASK_HUMAN_INPUT.getMessage());
    }

    @Test
    public void TX_02_invalid_input_then_valid_bad_colors() throws Exception {
      runCommands(
          NEW_GAME + " EASY 3",
          "Valerio", //
          PLAY,
          "green purple",
          "G Y");
      assertContains(INVALID_HUMAN_INPUT.getMessage());
      assertContains(START_ROUND.getMessage("1", "3"));
      assertDoesNotContain(START_ROUND.getMessage("2", "3"));
      assertContains(ASK_HUMAN_INPUT.getMessage());
    }

    @Test
    public void TX_03_try_different_player_name() throws Exception {
      Utils.randomAi.setSeed(561);
      Utils.randomPowerNumber.setSeed(32);

      runCommands(
          NEW_GAME + " EASY 1",
          "Anatol",
          PLAY,
          "G B",
          NEW_GAME + " EASY 1",
          "Parmjot",
          PLAY,
          "R Y");

      assertContainsAtRound(WELCOME_PLAYER.getMessage("Anatol"), 0);
      assertDoesNotContainAtRound("Valerio", 0);
      assertDoesNotContainAtRound("Parmjot", 0);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "BLUE"), 1);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage("Anatol", 1), 1);

      assertContainsAtRound(PRINT_WINNER_GAME.getMessage("Anatol"), 1);
      assertDoesNotContainAtRound(PRINT_WINNER_GAME.getMessage("Parmjot"), 1);
      assertDoesNotContainAtRound("Valerio", 1);

      assertContainsAtRound(WELCOME_PLAYER.getMessage("Parmjot"), 1);
      assertDoesNotContainAtRound("Valerio", 1);
      assertDoesNotContainAtRound(WELCOME_PLAYER.getMessage("Anatol"), 1);

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Parmjot", "RED", "YELLOW"), 2);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage("Parmjot", 1), 2);

      assertContainsAtRound(PRINT_WINNER_GAME.getMessage("Parmjot"), 2);
      assertDoesNotContainAtRound("Anatol", 2);
      assertDoesNotContainAtRound("Valerio", 2);
    }

    @Test
    public void TX_04_play_many_rounds() throws Exception {
      final int NUM_ROUNDS = 2000;

      Utils.randomAi.setSeed(56);
      Utils.randomPowerNumber.setSeed(3);

      List<Object> commands = new ArrayList<>();
      commands.add(NEW_GAME + " HARD " + NUM_ROUNDS);
      commands.add("Anatol");

      Random random = new Random(12345);

      for (int i = 0; i < NUM_ROUNDS; i++) {
        Colour a = Colour.values()[random.nextInt(4)];
        Colour b = Colour.values()[random.nextInt(4)];

        commands.add(PLAY);
        commands.add(a.name() + " " + b.name());
      }

      runCommands(commands.toArray());

      assertContains(PRINT_PLAYER_POINTS.getMessage("Anatol", "613"));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "605"));
    }

    @Test
    public void TX_05_play_many_rounds() throws Exception {
      final int NUM_ROUNDS = 2000;

      Utils.randomAi.setSeed(9001);
      Utils.randomPowerNumber.setSeed(9002);

      List<Object> commands = new ArrayList<>();
      commands.add(NEW_GAME + " HARD " + NUM_ROUNDS);
      commands.add("Anatol");

      Random random = new Random(54321);

      for (int i = 0; i < NUM_ROUNDS; i++) {
        Colour a = Colour.values()[random.nextInt(4)];
        Colour b = Colour.values()[random.nextInt(4)];

        commands.add(PLAY);
        commands.add(a.name() + " " + b.name());
      }

      runCommands(commands.toArray());

      assertContains(PRINT_PLAYER_POINTS.getMessage("Anatol", "563"));
      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "554"));
    }

    @Test
    public void TX_06_ensure_properly_reset() throws Exception {
      Utils.randomAi.setSeed(11);
      Utils.randomPowerNumber.setSeed(31);

      // This test makes sure that all AI state is reset when a game is restarted
      runCommands(
          NEW_GAME + " HARD 20",
          "Anatol",

          // Play red blue and green a lot so that the least used will be yellow
          PLAY,
          "R R",
          PLAY,
          "B B",
          PLAY,
          "G G",
          PLAY,
          "R R",
          PLAY,
          "B B",
          PLAY,
          "G G",
          PLAY,
          "R R",
          PLAY,
          "B B",
          PLAY,
          "G G",
          SHOW_STATS,
          NEW_GAME + " HARD 5", // Start a new game while current game is ongoing
          "Anatol",
          SHOW_STATS, // Stats should have been reset
          PLAY,
          "B R",
          PLAY,
          "Y G",
          PLAY,
          "R B", // In this round, if the AI wasn't reset we would expect it to choose YELLOW
          PLAY,
          "B Y",
          PLAY,
          "Y Y");

      // Check that all of the stats get outputted correctly
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage("Anatol", "4"), 9);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "1"), 9);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage("Anatol", "0"), 9);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "0"), 9);

      // Check all of the second game
      assertContainsAtRound("1 of 5", 10);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "YELLOW"), 10);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Anatol", "BLUE", "RED"), 10);

      assertContainsAtRound("2 of 5", 11);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 11);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Anatol", "YELLOW", "GREEN"), 11);

      assertContainsAtRound("3 of 5", 12);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"), 12);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Anatol", "RED", "BLUE"), 12);

      assertContainsAtRound("4 of 5", 13);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"), 13);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Anatol", "BLUE", "YELLOW"), 13);

      assertContainsAtRound("5 of 5", 14);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"), 14);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage("Anatol", "YELLOW", "YELLOW"), 14);

      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "1"), 14);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage("Anatol", "1"), 14);

      assertContainsAtRound(PRINT_END_GAME.getMessage(), 14);
      assertContainsAtRound(PRINT_TIE_GAME.getMessage(), 14);
    }

    @Test
    public void TX_07_medium_ensure_no_red() throws Exception {
      final int NUM_ROUNDS = 100;

      Utils.randomAi.setSeed(19);
      Utils.randomPowerNumber.setSeed(71);

      List<Object> commands = new ArrayList<>();
      commands.add(NEW_GAME + " MEDIUM " + NUM_ROUNDS);
      commands.add("Anatol");
      for (int i = 0; i < NUM_ROUNDS; i++) {
        commands.add(PLAY);
        commands.add("R R");
      }

      runCommands(commands.toArray());

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "RED"));
    }

    @Test
    public void TX_08_medium_ensure_no_green() throws Exception {
      final int NUM_ROUNDS = 100;

      Utils.randomAi.setSeed(196);
      Utils.randomPowerNumber.setSeed(711);

      List<Object> commands = new ArrayList<>();
      commands.add(NEW_GAME + " MEDIUM " + NUM_ROUNDS);
      commands.add("Anatol");
      for (int i = 0; i < NUM_ROUNDS; i++) {
        commands.add(PLAY);
        commands.add("G G");
      }

      runCommands(commands.toArray());

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "GREEN"));
    }

    @Test
    public void TX_09_medium_ensure_no_blue() throws Exception {
      final int NUM_ROUNDS = 100;

      Utils.randomAi.setSeed(19118);
      Utils.randomPowerNumber.setSeed(7187);

      List<Object> commands = new ArrayList<>();
      commands.add(NEW_GAME + " MEDIUM " + NUM_ROUNDS);
      commands.add("Anatol");
      for (int i = 0; i < NUM_ROUNDS; i++) {
        commands.add(PLAY);
        commands.add("B B");
      }

      runCommands(commands.toArray());

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "BLUE"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "BLUE"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "BLUE"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "BLUE"));
    }

    @Test
    public void TX_10_medium_ensure_no_yellow() throws Exception {
      final int NUM_ROUNDS = 100;

      Utils.randomAi.setSeed(1914);
      Utils.randomPowerNumber.setSeed(187);

      List<Object> commands = new ArrayList<>();
      commands.add(NEW_GAME + " MEDIUM " + NUM_ROUNDS);
      commands.add("Anatol");
      for (int i = 0; i < NUM_ROUNDS; i++) {
        commands.add(PLAY);
        commands.add("Y Y");
      }

      runCommands(commands.toArray());

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "YELLOW"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "YELLOW"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "YELLOW"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage(AI_NAME, "YELLOW", "YELLOW"));
    }

    // Check the input sanitization that is done for you
    @Test
    public void TX_11_check_zero_game_length() throws Exception {
      runCommands(NEW_GAME + " HARD 0", "Anatol");

      assertContains(INVALID_ROUNDS.getMessage());
      assertDoesNotContain(WELCOME_PLAYER.getMessage("Anatol"));
    }

    @Test
    public void TX_12_check_negative_game_length() throws Exception {
      runCommands(NEW_GAME + " HARD -1", "Anatol");

      assertContains(INVALID_ROUNDS.getMessage());
      assertDoesNotContain(WELCOME_PLAYER.getMessage("Anatol"));
    }

    @Test
    public void TX_13_check_invalid_difficulty() throws Exception {
      runCommands(NEW_GAME + " SUPER_EASY 2", "Anatol");

      assertContains(INVALID_DIFFICULTY.getMessage());
      assertDoesNotContain(WELCOME_PLAYER.getMessage("Anatol"));
    }

    @Test
    public void TX_14_check_power_colour() throws Exception {
      // Makes sure power colour is only announced when required
      // And does it on a number of rounds not divisible by 3 (unlike other power colour tests)
      // Also throw some invalid inputs into the mix

      Utils.randomAi.setSeed(814253246);
      Utils.randomPowerNumber.setSeed(89723765);

      runCommands(
          NEW_GAME + " EASY 10",
          "Anatol",
          PLAY,
          "R B",
          PLAY,
          "R R",
          PLAY,
          "purple brown",
          "G Y",
          PLAY,
          "B Y",
          PLAY,
          "G B",
          PLAY,
          "blue G",
          PLAY,
          "yellow",
          "Y G",
          PLAY,
          "G Y",
          PLAY,
          "B B",
          PLAY,
          "R G");

      assertContains(INVALID_HUMAN_INPUT.getMessage());

      assertDoesNotContainAtRound("POWER", 1);
      assertDoesNotContainAtRound("POWER", 2);

      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("GREEN"), 3);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, "3"), 3);

      assertDoesNotContainAtRound("POWER", 4);
      assertDoesNotContainAtRound("POWER", 5);

      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("BLUE"), 6);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, "3"), 6);

      assertDoesNotContainAtRound("POWER", 7);
      assertDoesNotContainAtRound("POWER", 8);

      assertContainsAtRound(PRINT_POWER_COLOUR.getMessage("YELLOW"), 9);
      assertContainsAtRound(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, "0"), 9);

      assertDoesNotContainAtRound("POWER", 10);
    }

    @Test
    public void TX_15_mixed_case_colours() throws Exception {
      runCommands(NEW_GAME + " MEDIUM 7", "Anatol", PLAY, "gReEN YellOW");

      assertContains(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "YELLOW"));
    }

    @Test
    public void TX_17_game_ended_play_and_show_stats() throws Exception {
      // Tests what happens if you run show-stats or play after game ends
      runCommands(
          NEW_GAME + " HARD 3",
          "Anatol",
          PLAY,
          "r r",
          PLAY,
          "r b",
          PLAY,
          "y r",
          SHOW_STATS,
          PLAY,
          "g g");

      assertContains(PRINT_END_GAME.getMessage());
      assertContains(GAME_NOT_STARTED.getMessage());

      assertDoesNotContain(START_ROUND.getMessage("4", "3"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "GREEN"));
    }

    @Test
    public void TX_18_test_power_colour_30_rounds() throws Exception {
      runCommands(
          NEW_GAME + " EASY 30",
          "Anatol",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r",
          PLAY,
          "r r");

      assertContainsAtRound("POWER COLOUR", 3);
      assertContainsAtRound("POWER COLOUR", 6);
      assertContainsAtRound("POWER COLOUR", 9);
      assertContainsAtRound("POWER COLOUR", 12);
      assertContainsAtRound("POWER COLOUR", 15);
      assertContainsAtRound("POWER COLOUR", 18);
      assertContainsAtRound("POWER COLOUR", 21);
      assertContainsAtRound("POWER COLOUR", 24);
      assertContainsAtRound("POWER COLOUR", 27);
      assertContainsAtRound("POWER COLOUR", 30);
    }

    @Test
    public void TX_19_test_long_run_winning() throws Exception {
      // Tests that strategy switching works correctly with a run of ten rounds where the AI wins
      // and so shouldn't switch, then switch on the next round when it loses

      Utils.randomAi.setSeed(634643);
      Utils.randomPowerNumber.setSeed(72746);

      runCommands(
          NEW_GAME + " HARD 16",
          "Anatol",
          PLAY,
          "g b",
          PLAY,
          "b y",
          PLAY,
          "r b",
          PLAY,
          "y y",
          PLAY,
          "r g",
          PLAY,
          "g y",
          PLAY,
          "b b",
          PLAY,
          "y r",
          PLAY,
          "r r",
          PLAY,
          "g g",
          PLAY,
          "b r",
          PLAY,
          "y b", // AI Should win all ten of these
          PLAY,
          "y b", // AI will lose this one
          PLAY,
          "b b", // AI will win these ones (if it has switch to avoid last strategy)
          PLAY,
          "g r",
          PLAY,
          "b r");

      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"), 1);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 2);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "RED"), 3);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "YELLOW"), 4);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 5);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "GREEN"), 6);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "BLUE"), 7);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "YELLOW"), 8);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "RED"), 9);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 10);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "BLUE"), 11);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "YELLOW"), 12);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "RED", "RED"), 13);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "GREEN", "BLUE"), 14);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "GREEN"), 15);
      assertContainsAtRound(PRINT_INFO_MOVE.getMessage(AI_NAME, "BLUE", "BLUE"), 16);

      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "22"), 16);
      assertContainsAtRound(PRINT_PLAYER_POINTS.getMessage("Anatol", "2"), 16);
      assertContainsAtRound(PRINT_WINNER_GAME.getMessage(AI_NAME), 16);
      assertContainsAtRound(PRINT_END_GAME.getMessage(), 16);
    }

    @Test
    public void TX_20_test_many_invalid() throws Exception {
      Utils.randomAi.setSeed(13);
      Utils.randomPowerNumber.setSeed(2);

      runCommands(
          NEW_GAME + " EASY 1",
          "Anatol",
          PLAY,
          "x",
          "r r r",
          "rb",
          "reed ",
          " ",
          "purple purple",
          "orange orang",
          "blu",
          "blu blue",
          "bluee blue",
          "y",
          "a b",
          "c d",
          "e f g",
          "gray black",
          "white white",
          "yellow",
          "blue",
          "blue",
          "aaa aaa",
          "eulb eulb",
          "wolley yellow",
          "badabadbadabda a",
          "7246 472",
          "5$@64 614#@/';$",
          "*@6434",
          "green",
          "green",
          "green",
          "green",
          "gReen yELlow");

      assertContains(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "YELLOW"));

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "RED", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "RED", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "RED", "BLUE"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "RED", "YELLOW"));

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "GREEN", "BLUE"));

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "BLUE", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "BLUE", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "BLUE", "BLUE"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "BLUE", "YELLOW"));

      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "YELLOW", "RED"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "YELLOW", "GREEN"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "YELLOW", "BLUE"));
      assertDoesNotContain(PRINT_INFO_MOVE.getMessage("Anatol", "YELLOW", "YELLOW"));

      assertContains(PRINT_OUTCOME_ROUND.getMessage(AI_NAME, "1"));
      assertContains(PRINT_OUTCOME_ROUND.getMessage("Anatol", "0"));

      assertContains(PRINT_PLAYER_POINTS.getMessage(AI_NAME, "1"));
      assertContains(PRINT_PLAYER_POINTS.getMessage("Anatol", "0"));

      assertContains(PRINT_END_GAME.getMessage());

      assertContains(PRINT_WINNER_GAME.getMessage(AI_NAME));
    }
  }
}
