package game;

import dungeon.Direction;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import java.util.ArrayList;
import location.Location;
import monster.MonsterImpl;
import player.Player;
import treasure.TreasureImpl;


/**
 * The Game implementation to put the player on the dungeon and handle the treasure during game the
 * directions available during game.
 */
public class GameImpl implements Game {
  private Dungeon dungeon;
  private Player player;
  private Location startLocation;
  private Location endLocation;

  /**
   * Construct the game with width height and player.
   *
   * @param player player
   * @param width  dungeon width
   * @param height dungeon height
   * @param wrap   wrapping type
   */
  public GameImpl(Player player, int width, int height, String wrap) {
    this.player = player;
    dungeon = new DungeonImpl(width, height, wrap);
    Location[] route = dungeon.randomRoute();
    startLocation = route[0];
    endLocation = route[1];
    this.player.getPlayerLocation().setPlayerX(startLocation.getPlayerX());
    this.player.getPlayerLocation().setPlayerY(startLocation.getPlayerY());
  }

  @Override
  public String showTheDirection() {
    ArrayList<Direction> list = dungeon.getCanMove(getPlayerCurrentLocation());
    StringBuilder builder = new StringBuilder("you can move: ");
    for (Direction dir : list) {
      builder.append(dir.name()).append(' ');
    }
    return builder.toString();
  }

  @Override
  public boolean playerMove(Direction direction) {
    Location next = dungeon.checkMove(direction, getPlayerCurrentLocation());
    if (next == null) {
      // can't move
      return false;
    } else {
      player.getPlayerLocation().setPlayerX(next.getPlayerX());
      player.getPlayerLocation().setPlayerY(next.getPlayerY());
      return true;
    }
  }

  @Override
  public void pullTreasure() {
    ArrayList<TreasureImpl> res = dungeon.currentTreasure(getPlayerCurrentLocation());
    res.forEach(s -> player.addTreasure(s));

  }

  @Override
  public Location getPlayerStartLocation() {
    return startLocation;
  }

  @Override
  public Location getPlayerEndLocation() {
    return endLocation;
  }

  @Override
  public Location getPlayerCurrentLocation() {
    return player.getPlayerLocation();
  }

  @Override
  public Dungeon getDungeon() {
    return dungeon;
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public MonsterImpl shootArrow(Direction direction, int distance) {
    boolean res = player.useArrow();
    if (!res) {
      return null;
    }
    return dungeon.shootArrow(player.getPlayerLocation(), direction, distance);
  }

  @Override
  public ArrayList<MonsterImpl> monsterFromOneStep() {
    return dungeon.monsterFromOneStep(player.getPlayerLocation());
  }

  @Override
  public ArrayList<MonsterImpl> monsterFromTwoStep() {
    return dungeon.monsterFromTwoStep(player.getPlayerLocation());
  }
}
