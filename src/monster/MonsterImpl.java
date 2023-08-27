package monster;

import location.Location;

/**
 * Implement Monster health of Monster.
 */
public class MonsterImpl implements Monster {
  private int health = 10;
  private Location location;

  public MonsterImpl(Location location) {
    this.location = location;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void reduceHealth() {
    health -= 5;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public String toString() {
    return "M_" + health + location.toString();
  }
}
