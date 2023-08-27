package game;

import dungeon.Direction;
import dungeon.Dungeon;
import java.util.ArrayList;
import location.Location;
import monster.MonsterImpl;
import player.Player;

/**
 * The Game interface to put the player on the dungeon and handle the treasure during game, the
 * directions available during game.
 */
public interface Game {
  /**
   * Show the available dungeon.
   *
   * @return the available dungeon.
   */
  String showTheDirection();

  /**
   * Check if player move to the other direction.
   *
   * @param direction direction
   * @return when can not move,return false
   */
  boolean playerMove(Direction direction);

  /**
   * The player takes the treasure.
   */
  void pullTreasure();

  /**
   * Get the player's start location.
   *
   * @return player's loc when start
   */
  Location getPlayerStartLocation();

  /**
   * Get the player's end location.
   *
   * @return target loc
   */
  Location getPlayerEndLocation();

  /**
   * Get the player's current location.
   *
   * @return player's loc
   */
  Location getPlayerCurrentLocation();

  /**
   * Get the kruskal's dungeon.
   *
   * @return return the dungeon
   */
  Dungeon getDungeon();

  /**
   * Get the player from player class.
   *
   * @return return the player
   */
  Player getPlayer();

  /**
   * Shoot the arrow with direction and distance.
   *
   * @param direction the direction from direction enum
   * @param distance distance of arrow travelling
   * @return the monster got shot.
   */
  MonsterImpl shootArrow(Direction direction, int distance);

  /**
   * detect the monster from one-step away.
   *
   * @return the monster from one-step away
   */
  ArrayList<MonsterImpl> monsterFromOneStep();

  /**
   * detect the monster from two-step away.
   *
   * @return the monster from two-step away
   */
  ArrayList<MonsterImpl> monsterFromTwoStep();
}
