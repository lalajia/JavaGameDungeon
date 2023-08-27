import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dungeon.Arrow;
import game.Game;
import game.GameImpl;
import java.util.ArrayList;
import location.Location;
import location.LocationImpl;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerImpl;

/**
 * Test class for player.
 */
public class PlayerImplTest {
  private Location loc;
  private Player player;
  private ArrayList<Arrow> arrows;
  private Game game;

  /**
   * set up for player.
   */
  @Before
  public void setUp() {
    loc = new LocationImpl(2, 3);
    player = new PlayerImpl("Lino");
    arrows = new ArrayList<>(3);
    game = new GameImpl(player, 6, 6, "Nonwrapping");
  }

  @Test
  public void setPlayerLocation() {
    player.setPlayerLocation(loc);
    assertEquals(2, player.getPlayerLocation().getPlayerX());
    assertEquals(3, player.getPlayerLocation().getPlayerY());
  }

  @Test
  public void useArrow() {
    player.addArrow(arrows);
    arrows.add(Arrow.ARROW);
    assertEquals(1, arrows.size());
    assertTrue(player.useArrow());
  }
}