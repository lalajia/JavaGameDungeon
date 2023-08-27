package dungeon;

import java.util.ArrayList;
import location.Location;
import monster.Monster;
import monster.MonsterImpl;
import treasure.TreasureImpl;



/**
 * Dungeon Interface for using Kruskal to build the dungeon. Set the treasure percent into the
 * dungeon.
 */
public interface Dungeon {

  /**
   * Get the wrapping type of the dungeon.
   *
   * @return the wrapping type string.
   */
  String getWrappingType();

  /**
   * Set TreasureImpl percent.
   *
   * @param percent x%
   */
  void setTreasurePercent(int percent);

  /**
   * Get the treasure set on the dungeon.
   *
   * @return the persent set for treasure in the dungeon.
   */
  int getTreasurePercent();

  /**
   * Assign treasure with percent.
   */
  void assignTreasure();

  /**
   * Show the cave location and what direction is available.
   *
   * @return the cave information with location and direction available.
   */
  ArrayList<String> getCaveInfo();

  /**
   * Set random route for player.
   *
   * @return startLoc and endLoc
   */
  Location[] randomRoute();

  /**
   * Get the treasure of the position.
   *
   * @param next target position
   * @return this position's treasures.
   */
  ArrayList<TreasureImpl> currentTreasure(Location next);

  /**
   * Show all the treasure in the dungeon.
   *
   * @return all the treasure in the dungeon.
   */
  String showTreasure();

  /**
   * Check if the current location has treasure.
   *
   * @param loc the current location
   * @return the treasure in that location.
   */
  ArrayList<TreasureImpl> find(Location loc);

  /**
   * Check if move valid.
   *
   * @param direction the direction want to check
   * @param cur       current loc
   * @return null is invalid, true: return new Loc
   */
  Location checkMove(Direction direction, Location cur);

  /**
   * Get the data which stores the available directions.
   *
   * @return the int data.
   */
  int[][] getData();

  /**
   * add the interconnectivity for the dungeon.
   *
   * @param interconnectivity the number of interconnectivity.
   */
  void addInterconnectivity(int interconnectivity);

  /**
   * Show the available direction of the current location.
   *
   * @param loc the location of the player.
   * @return the available direction.
   */
  ArrayList<Direction> getCanMove(Location loc);

  /**
   * Assign arrows to the dungeon.
   */
  void assignArrows();

  /**
   * Assign Monsters to the dungeon.
   */
  void assignMonsters();

  /**
   * Check if the current location is cave or tunnel.
   *
   * @param location the argument location
   * @return True if is cave, false if is tunnel
   */
  boolean isCave(Location location);

  /**
   * Set the number of monster in the dungeon.
   *
   * @param num number of monsters.
   */
  void setMonsterNum(int num);

  /**
   * Get the list of monsters from the dungeon.
   *
   * @return the list of monsters.
   */
  ArrayList<Monster> getMonsters();

  /**
   * get the list of arrows from the dungeon.
   *
   * @return the list of arrows from the dungeon
   */
  ArrayList<Arrow> getArrows();

  /**
   * Check if the location has arrows or not.
   *
   * @param location the desiired location.
   * @return the arrow the player carries
   */
  ArrayList<Arrow> findArrow(Location location);

  /**
   * Add the arrow to the player.
   *
   * @param location player current location.
   * @return the arrow the player carries
   */
  ArrayList<Arrow> pullArrow(Location location);

  /**
   * Check if the location has monsters or not.
   *
   * @param location the desired location.
   * @return the arrow the player carries
   */
  ArrayList<MonsterImpl> findMonster(Location location);

  /**
   * detect the monster from one-step away.
   *
   * @param location the desired location.
   * @return the monster from one-step away
   */
  ArrayList<MonsterImpl> monsterFromOneStep(Location location);

  /**
   * detect the monster from two-step away.
   *
   * @param location the desired location.
   * @return the monster from two-step away
   */
  ArrayList<MonsterImpl> monsterFromTwoStep(Location location);

  /**
   * Shoot the arrow to the direction and the distance.
   *
   * @param playerLocation player current location.
   * @param direction direction of arrow.
   * @param distance distance of arrow travel.
   * @return the monster got shoot.
   */
  MonsterImpl shootArrow(Location playerLocation, Direction direction, int distance);
}
