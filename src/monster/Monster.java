package monster;

import location.Location;

/**
 * Monster interface for calculate the health of monster.
 */
public interface Monster {

  /**
   * The location of monsters.
   *
   * @return the monster location.
   */
  Location getLocation();

  /**
   * when monster has been shot, reduce the health.
   */
  void reduceHealth();

  /**
   * Get the health of monster.
   *
   * @return the monster's health.
   */
  int getHealth();
}
