package player;

import dungeon.Arrow;
import java.util.ArrayList;
import location.Location;
import location.LocationImpl;
import treasure.TreasureImpl;
import treasure.TreasureType;


/**
 * The implementation for player to report the description and get current location.
 */
public class PlayerImpl implements Player {
  private ArrayList<TreasureImpl> treasureImpls;
  private Location playerLocation;
  private String name;
  private int arrows;

  /**
   * The constructor of the player with location.
   *
   * @param name name of the player
   */
  public PlayerImpl(String name) {
    this.name = name;
    this.playerLocation = new LocationImpl(0, 0);
    treasureImpls = new ArrayList<>();
    arrows = 3;
  }

  @Override
  public String reportDescription() {
    int i = 0;
    int j = 0;
    int k = 0;
    for (TreasureImpl t : treasureImpls) {
      switch (t.getType()) {
        case DIAMOND:
          i++;
          break;
        case SAPPHIRE:
          j++;
          break;
        case RUBY:
          k++;
          break;
        default:
          break;
      }
    }
    return name + "(" + playerLocation.getPlayerX() + "," + playerLocation.getPlayerY() + ") "
        + TreasureType.DIAMOND + " : " + i + ", " + TreasureType.SAPPHIRE + " : " + j + ", "
        + TreasureType.RUBY + " : " + k + " ARROWS: " + arrows;
  }

  @Override
  public Location getPlayerLocation() {
    return this.playerLocation;
  }

  @Override
  public void addTreasure(TreasureImpl treasureImpl) {
    treasureImpls.add(treasureImpl);
  }

  @Override
  public void setPlayerLocation(Location location) {
    this.playerLocation = location;
  }

  @Override
  public void addArrow(ArrayList<Arrow> arrows) {
    this.arrows += arrows.size();
  }

  @Override
  public boolean useArrow() {
    if (arrows > 0) {
      arrows--;
      return true;
    }
    return false;
  }
}
