package player;

import dungeon.Arrow;
import java.util.ArrayList;
import location.Location;
import treasure.TreasureImpl;



/**
 * The interface for player to report the description and get current location.
 */
public interface Player {
  /**
   * Show the player's location and the treasure.
   *
   * @return player's location and the treasure.
   */
  String reportDescription();

  /**
   * Show the player's location.
   *
   * @return player's location
   */
  Location getPlayerLocation();

  /**
   * Add the treasure to the player.
   *
   * @param treasureImpl add the treasure.
   */
  void addTreasure(TreasureImpl treasureImpl);

  /**
   * Set the player location.
   *
   * @param location the location want to set.
   */
  void setPlayerLocation(Location location);

  /**
   * Add arrow to the arrow list to the player.
   *
   * @param arrows the player will pick.
   */
  void addArrow(ArrayList<Arrow> arrows);

  /**
   * Check if player have arrow to use.
   *
   * @return false if arrow is empty.
   */
  boolean useArrow();
}
