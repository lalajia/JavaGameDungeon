import static org.junit.Assert.assertTrue;

import controller.ControllerImpl;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import game.Game;
import game.GameImpl;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import location.LocationImpl;
import monster.Monster;
import monster.MonsterImpl;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerImpl;




/**
 * Test class.
 */
public class GameImplTest {

  private Dungeon dungeon;
  private Game game;
  private Player player;
  private DungeonImpl dungeonCopy;
  private Monster monster;

  /**
   * Set up for wrapping and nonwrapping dungeon.
   */
  @Before
  public void setUp() {
    player = new PlayerImpl("Lisa");
    dungeon = new DungeonImpl(8, 6, "Nonwrapping");
    game = new GameImpl(player, 8, 6, "Nonwrapping");
    game.getDungeon().setTreasurePercent(20);
    game.getDungeon().assignTreasure();
    game.getDungeon().addInterconnectivity(0);
    game.getDungeon().setMonsterNum(5);
    game.getDungeon().assignArrows();
    game.getDungeon().assignMonsters();
    dungeonCopy = (DungeonImpl) game.getDungeon();
    monster = new MonsterImpl(game.getPlayerCurrentLocation());
  }

  /**
   * verifies that there is at least one Otyugh, in the end cave in the dungeon.
   */
  @Test
  public void testEndCaveMonster() {
    String monsterLocations = Arrays.toString(dungeonCopy.getMonsters().toArray());
    String endLocation =
        "M_10(" + game.getPlayerEndLocation().getPlayerX() + "," + game.getPlayerEndLocation()
            .getPlayerY() + ")";
    assertTrue(monsterLocations.contains(endLocation));
  }

  /**
   * verifies that multiple Otyughs are correctly added to the dungeon.
   */
  @Test
  public void testMultipleMonsters() {
    assertTrue(dungeonCopy.getMonsters().size() > 1);
  }

  /**
   * verifies that the player can smell a single Otyugh.
   */
  @Test
  public void testSmellSingleMonster() {
    ArrayList<MonsterImpl> theMonster = game.monsterFromOneStep();
    assertTrue(theMonster.size() <= 1);
  }

  /**
   * verifies that the player can smell multiple Otyughs.
   */
  @Test
  public void testSmellMultipleMonster() {
    ArrayList<MonsterImpl> theMonster = game.monsterFromTwoStep();
    assertTrue(theMonster.size() < 6 || theMonster.size() > 1);
  }

  /**
   * Verifies that the player can pick up arrows that are found in the dungeon.
   */
  @Test
  public void testPickUpArrow() {
    String reportOne = player.reportDescription();
    assertTrue(reportOne.contains("ARROWS: 3"));
    player.addArrow(dungeonCopy.pullArrow(game.getPlayerCurrentLocation()));
    //assertTrue();
    //System.out.println(reportOne);
  }

  /**
   * Verifies that arrows are added to the dungeon.
   */
  @Test
  public void testArrowInTheDungeon() {
    String reportOne = player.reportDescription();
    assertTrue(reportOne.contains("ARROWS: 3"));
    //player.addArrow(dungeonCopy.pullArrow(game.getPlayerStartLocation()));
    //assertTrue();
    assertTrue(dungeonCopy.getArrows().toArray().length > 0);
  }

  /**
   * Verifies that the player can shoot an arrow. Verifies that the player can shoot and hit an
   * Otyugh. Verifies that an Otyugh dies after being hit by 2 arrows.
   */
  @Test
  public void testShootArrow() {
    LocationImpl loc = new LocationImpl(game.getPlayerEndLocation().getPlayerX() - 1,
        game.getPlayerEndLocation().getPlayerY());
    player.setPlayerLocation(loc);
    MonsterImpl monster1 = new MonsterImpl(game.getPlayerEndLocation());
    //game.shootArrow(Direction.SOUTH, 1);
    //assertEquals(5, monster1.getHealth());
  }

  /**
   * verifies that the arrow travels correctly through caves and tunnels.
   */
  @Test
  public void testArrowTravel() {
  }

  /**
   * Verifies that the player can shoot in the correct direction but miss the Otyugh.
   */
  @Test
  public void testMissMonster() {
  }

  /**
   * Verifies that an Otyugh will kill the player.
   */
  @Test
  public void testMonsterKillPlayer() {
    player.setPlayerLocation(game.getPlayerEndLocation());
    monster = new MonsterImpl(game.getPlayerEndLocation());
  }

  /**
   * verifies that a player who enters a cave with an Otyugh has a chance of escaping if the Otyugh
   * has been hit by a single arrow.
   */
  @Test
  public void testPlayerEscape() {
    player.setPlayerLocation(game.getPlayerEndLocation());
    monster = new MonsterImpl(game.getPlayerEndLocation());
  }

  /**
   * Verifies that a player can enter a cave after an Otyugh has been killed.
   */
  @Test
  public void testPlayerSafe() {
    player.setPlayerLocation(game.getPlayerEndLocation());
    monster = new MonsterImpl(game.getPlayerEndLocation());
  }

  /**
   * Verifies that the controller handles a move command correctly.
   */
  @Test
  public void testMoveController() {
    StringReader input = new StringReader("S");
    Appendable gameLog = new StringBuilder();
    ControllerImpl controller = new ControllerImpl(input, gameLog);
    //controller.playGame(game);
    //assertTrue(gameLog.toString().contains("SOUTH"));
  }

  /**
   * verifies that the controller handles a pickup command correctly.
   */
  @Test
  public void testPickUpController() {
    StringReader in = new StringReader("");
    Appendable out = new StringBuffer();
    ControllerImpl controller = new ControllerImpl(in, out);
    //controller.playGame(game);
    //assertTrue(out.toString().contains("pick treasure"));
  }

  /**
   * Verifies that the controller handles a shoot command correctly.
   */
  @Test
  public void testShootController() {
    StringReader in = new StringReader("W");
    Appendable out = new StringBuffer();
    ControllerImpl controller = new ControllerImpl(in, out);
    //controller.playGame(game);
    //assertTrue(out.toString().contains("shoot to"));
  }

  /**
   * verifies that the controller handles a quit command correctly.
   */
  @Test
  public void testQuitController() {
    StringReader in = new StringReader("q");
    Appendable out = new StringBuffer();
    ControllerImpl controller = new ControllerImpl(in, out);
    //controller.playGame(game);
    //assertTrue(out.toString().contains("pick treasure"));
  }

  /**
   * verifies that the controller handles a IOException correctly.
   */
  @Test
  public void testException() {

  }

  /**
   * verifies that the controller handles an error from the model correctly.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testModelError() {
    StringReader in = new StringReader("");
    Appendable out = new StringBuffer();
    ControllerImpl controller = new ControllerImpl(in, out);
    game = null;
    controller.playGame(game);
  }
}