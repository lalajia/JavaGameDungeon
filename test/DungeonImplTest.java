import static org.junit.Assert.assertEquals;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import game.Game;
import game.GameImpl;
import location.Location;
import location.LocationImpl;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerImpl;

/**
 * Test class for Dungeon.
 */
public class DungeonImplTest {
  private Dungeon dungeonOne;
  private Dungeon dungeonTwo;
  private Game gameOne;
  private Game gameTwo;
  private int interconnectivity;

  private Player playerOne;

  /**
   * Set up for wrapping and nonwrapping dungeon.
   */
  @Before
  public void setUp() {
    playerOne = new PlayerImpl("Lisa");
    dungeonOne = new DungeonImpl(8, 6, "Nonwrapping");
    dungeonOne.setTreasurePercent(20);
    dungeonOne.assignTreasure();
    gameOne = new GameImpl(playerOne, 8, 6, "Nonwrapping");
    dungeonTwo = new DungeonImpl(8, 6, "Wrapping");
    gameTwo = new GameImpl(playerOne, 8, 6, "Wrapping");

  }

  private static void show(Game game) {
    int[][] data = game.getDungeon().getData();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < data.length; i++) {
      builder.setLength(0);
      for (int j = 0; j < data[0].length; j++) {
        if (checkIn(i, j, game.getPlayerCurrentLocation())) {
          System.out.print('p');
        } else if (checkIn(i, j, game.getPlayerStartLocation())) {
          System.out.print("s");
        } else if (checkIn(i, j, game.getPlayerEndLocation())) {
          System.out.print("e");
        } else {
          System.out.print('x');
        }

        if ((data[i][j] & (1 << 2)) > 0) {
          System.out.print(' ');
        } else {
          System.out.print('|');
        }
        if ((data[i][j] & (1 << 3)) > 0) {
          builder.append("  ");
        } else {
          builder.append("_ ");
        }
      }
      System.out.println();
      System.out.println(builder.toString());
    }
  }

  private static boolean checkIn(int i, int j, Location loc) {
    if (loc.getPlayerX() == j && loc.getPlayerY() == i) {
      return true;
    }
    return false;
  }

  @Test
  public void testNonWrapping() {
    assertEquals("Nonwrapping", dungeonOne.getWrappingType());
    show(gameOne);
  }

  @Test
  public void testWrapping() {
    assertEquals("Wrapping", dungeonTwo.getWrappingType());
    show(gameTwo);

  }

  @Test
  public void testReachAllCave() {
    System.out.println(dungeonOne.getCaveInfo());
  }

  @Test
  public void testPathFive() {
    show(gameOne);
  }

  @Test
  public void testTreasurePercent() {
    assertEquals(20, dungeonOne.getTreasurePercent());
    System.out.println(dungeonOne.showTreasure());
  }

  @Test
  public void testStartLocation() {
    gameOne.getPlayerEndLocation().setPlayerX(3);
    gameOne.getPlayerEndLocation().setPlayerY(5);
    assertEquals(3, gameOne.getPlayerEndLocation().getPlayerX());
    assertEquals(5, gameOne.getPlayerEndLocation().getPlayerY());
  }

  @Test
  public void testEndLocation() {
    gameOne.getPlayerEndLocation().setPlayerX(1);
    gameOne.getPlayerEndLocation().setPlayerY(3);
    assertEquals(1, gameOne.getPlayerEndLocation().getPlayerX());
    assertEquals(3, gameOne.getPlayerEndLocation().getPlayerY());
  }

  @Test
  public void testPlayerDescription() {
    System.out.println(playerOne.reportDescription());
  }

  /**
   * getCaveInfo shows the location of the cave and the four direction around the cave. 1 means
   * available edge, 0 means no edge.
   */
  @Test
  public void testPlayerMove() {
    System.out.println(gameOne.showTheDirection());
    System.out.println(dungeonOne.getCaveInfo());
  }

  @Test
  public void testPickTreasure() {
    Location loc = new LocationImpl(1, 3);
    System.out.println(dungeonOne.getCaveInfo());
    System.out.println(dungeonOne.getCanMove(loc));
  }
}