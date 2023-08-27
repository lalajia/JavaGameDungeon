import static org.junit.Assert.assertEquals;

import location.Location;
import location.LocationImpl;
import monster.Monster;
import monster.MonsterImpl;
import org.junit.Before;
import org.junit.Test;



/**
 * Test class for monster.
 */
public class MonsterImplTest {
  private Location loc;
  private Monster monster;

  /**
   * Set up for monster.
   */
  @Before
  public void setUp() {
    loc = new LocationImpl(2, 3);
    monster = new MonsterImpl(loc);
  }

  @Test
  public void getLocation() {
    assertEquals(2, monster.getLocation().getPlayerX());
    assertEquals(3, monster.getLocation().getPlayerY());
  }

  @Test
  public void reduceHealth() {
    monster.reduceHealth();
    assertEquals(5, monster.getHealth());
  }

  @Test
  public void testToString() {
    String str = "M_10(2,3)";
    assertEquals(str, monster.toString());
  }
}