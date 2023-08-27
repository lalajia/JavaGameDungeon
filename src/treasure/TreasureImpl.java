package treasure;

/**
 * The implementation for the type of the treasure.
 */
public class TreasureImpl implements Treasure {
  private TreasureType type;

  public TreasureImpl(TreasureType type) {
    this.type = type;
  }

  /**
   * The type of the treasure.
   *
   * @return the type of the treasure.
   */
  public TreasureType getType() {
    return type;
  }
}
