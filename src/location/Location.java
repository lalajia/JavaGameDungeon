package location;

/**
 * The location interface to get the player's row and column locations. Set row and column
 * locations.
 */
public interface Location {
  int getPlayerX();

  int getPlayerY();

  int setPlayerX(int x);

  int setPlayerY(int y);
}
