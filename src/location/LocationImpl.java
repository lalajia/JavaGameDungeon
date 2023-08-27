package location;

import java.util.Objects;

/**
 * The location implementation to get the player's row and column locations. Set row and column
 * locations.
 */
public class LocationImpl implements Location {
  private int row;
  private int column;

  public LocationImpl(int x, int y) {
    this.row = x;
    this.column = y;
  }

  @Override
  public int getPlayerX() {
    return row;
  }

  @Override
  public int getPlayerY() {
    return column;
  }

  @Override
  public int setPlayerX(int x) {
    this.row = x;
    return this.row;
  }

  @Override
  public int setPlayerY(int y) {
    this.column = y;
    return this.column;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationImpl location = (LocationImpl) o;
    return row == location.row && column == location.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }

  @Override
  public String toString() {
    return "(" + row + "," + column + ")";
  }
}
