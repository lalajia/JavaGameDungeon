package driver;

import controller.ControllerImpl;
import dungeon.DungeonImpl;
import game.Game;
import game.GameImpl;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import player.Player;
import player.PlayerImpl;

import location.Location;

/**
 * Driver class for Dungeon Game.
 */
public class Driver {
  /**
   * Main method.
   *
   * @param args the user input.
   */
  public static void main(String[] args) throws IOException {
    System.out.println("Start Game");
    Scanner scanner = new Scanner(System.in);
    System.out.print("width(>6): ");
    int width = Integer.parseInt(scanner.nextLine());
    System.out.print("height(>6): ");
    int height = Integer.parseInt(scanner.nextLine());
    if (width < 6) {
      width = 6;
    }
    if (height < 6) {
      height = 6;
    }

    System.out.print("Please input Wrapping/Nonwrapping: ");
    String wrap = scanner.nextLine();
    System.out.print("your name:");
    String name = scanner.nextLine();
    Player player = new PlayerImpl(name);
    Game game = new GameImpl(player, width, height, wrap);
    System.out.print("treasure percent: ");
    int percent = Integer.parseInt(scanner.nextLine());
    game.getDungeon().setTreasurePercent(percent);
    game.getDungeon().assignTreasure();
    System.out.print("interconnectivity: ");
    int interconnectivity = Integer.parseInt(scanner.nextLine());
    game.getDungeon().addInterconnectivity(interconnectivity);
    System.out.print("number of monster(>1): ");
    game.getDungeon().setMonsterNum(Integer.parseInt(scanner.nextLine()));
    game.getDungeon().assignArrows();
    game.getDungeon().assignMonsters();
    System.out.println("StartLocation is at(" + game.getPlayerStartLocation().getPlayerX() + ", "
        + game.getPlayerStartLocation().getPlayerY() + ")");
    System.out.println("EndLocation is at(" + game.getPlayerEndLocation().getPlayerX() + ", "
        + game.getPlayerEndLocation().getPlayerY() + ")");
    DungeonImpl d = (DungeonImpl) game.getDungeon();
    System.out.println(Arrays.toString(d.getArrows().toArray()));
    System.out.println(Arrays.toString(d.getMonsters().toArray()));
    show(game);
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    new ControllerImpl(input, output).playGame(game);
  }

  private static void show(Game game) {
    int[][] data = game.getDungeon().getData();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < data.length; i++) {
      builder.setLength(0);
      for (int j = 0; j < data[0].length; j++) {
        if (checkIn(i, j, game.getPlayerCurrentLocation())) {
          System.out.print('p');
        } else if (checkIn(i, j, game.getPlayerStartLocation())) {
          System.out.print("s");
        } else if (checkIn(i, j, game.getPlayerEndLocation())) {
          System.out.print("e");
        } else {
          System.out.print('x');
        }

        if ((data[i][j] & (1 << 2)) > 0) {
          System.out.print(' ');
        } else {
          System.out.print('|');
        }
        if ((data[i][j] & (1 << 3)) > 0) {
          builder.append("  ");
        } else {
          builder.append("_ ");
        }
      }
      System.out.println();
      System.out.println(builder.toString());
    }
  }

  private static boolean checkIn(int i, int j, Location loc) {
    if (loc.getPlayerX() == j && loc.getPlayerY() == i) {
      return true;
    }
    return false;
  }

}
