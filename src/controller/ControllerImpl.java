package controller;

import dungeon.Arrow;
import dungeon.Direction;
import game.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import monster.MonsterImpl;


/**
 * Implement a console controller with Controller Interface.
 */
public class ControllerImpl implements Controller {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructor for the controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public ControllerImpl(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void playGame(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("The game could not be null");
    }
    boolean die = false;
    //Player player = game.getPlayer();
    ArrayList<MonsterImpl> mm = new ArrayList<>();
    while (!game.getPlayerCurrentLocation().equals(game.getPlayerEndLocation())) {
      try {
        out.append(game.getPlayer().reportDescription()).append("\n");
        out.append("you are in " + (game.getDungeon().isCave(game.getPlayer().getPlayerLocation())
            ? "cave"
            : "tunnel")).append("\n");
        out.append(game.showTheDirection()).append("\n");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      // check die
      mm = game.getDungeon().findMonster(game.getPlayer().getPlayerLocation());
      for (MonsterImpl m : mm) {
        if (m.getHealth() == 10) {
          die = true;
          break;
        } else if (m.getHealth() == 5) {
          // random 50%
          if (new Random().nextInt(2) == 0) {
            die = true;
            break;
          }
          try {
            out.append("Congrats! You run away!\n");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
      if (die) {
        try {
          out.append("player is died. \n");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        break;
      }
      boolean moved = false;

      try {
        moved = move(game);


      } catch (IOException ioe) {
        throw new IllegalStateException("Append failed", ioe);
      }
      if (moved) {
        try {
          out.append(game.getPlayer().reportDescription()).append("\n");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        ArrayList<Arrow> arrows;

        // pull arrows.
        if ((arrows = game.getDungeon().findArrow(game.getPlayer().getPlayerLocation())).size()
            > 0) {
          try {
            out.append("You find " + arrows.size() + " arrow here\n");

            out.append("Pick arrow?(y/n):");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          char cmd = scan.nextLine().toUpperCase(Locale.ROOT).charAt(0);
          if (cmd == 'Y') {
            game.getPlayer()
                .addArrow(game.getDungeon().pullArrow(game.getPlayer().getPlayerLocation()));
          }
        }
        // pull treasure
        if (game.getDungeon().find(game.getPlayer().getPlayerLocation()).size() > 0) {
          try {
            out.append("pull Treasure(y/n): ");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          char c1 = scan.nextLine().charAt(0);
          if (c1 == 'y' || c1 == 'Y') {
            game.pullTreasure();
          }
        }
        // due monster
        ArrayList<MonsterImpl> theMonster = game.monsterFromOneStep();
        try {
          out.append(game.showTheDirection()).append("\n");
          if (theMonster.size() > 0) {
            out.append(
                "You smell something terrible 1 steps from you\n" + "There are " + theMonster.size()
                    + " Otyugh.\n");
            out.append(Arrays.toString(theMonster.toArray())).append("\n");

            shootLoop(game);

          } else {
            theMonster = game.monsterFromTwoStep();
            if (theMonster.size() > 0) {
              out.append("You smell something terrible 2 steps from you\n" + "There are "
                  + theMonster.size() + " Otyugh.\n");
              out.append(Arrays.toString(theMonster.toArray())).append("\n");

              shootLoop(game);
            }
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

      } else {
        try {
          out.append("invalid step: \n");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    mm = game.getDungeon().findMonster(game.getPlayerEndLocation());
    for (MonsterImpl m : mm) {
      if (m.getLocation() == game.getPlayerEndLocation()) {
        if (m.getHealth() != 0) {
          die = true;
        }
      }
    }
    try {
      if (die) {
        out.append("player is died.\n");
      } else {
        out.append("You win!\n");
      }
      out.append("Game Finish!!\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void shootLoop(Game game) throws IOException {
    while (true) {
      MonsterImpl attackl = shoot(game);
      if (attackl == null) {
        out.append("shoot failed\n");
      } else {
        out.append("attack: ").append(attackl.toString()).append("\n");
      }
      out.append("continue shoot?(y/n):");
      char character = scan.nextLine().toUpperCase(Locale.ROOT).charAt(0);
      if (character != 'Y') {
        break;
      }
    }
  }

  private boolean move(Game game) throws IOException {
    out.append("Where to? ");
    boolean moved = false;
    char character = scan.nextLine().toUpperCase(Locale.ROOT).charAt(0);
    switch (character) {
      case 'W':
        moved = game.playerMove(Direction.WEST);
        break;
      case 'N':
        moved = game.playerMove(Direction.NORTH);
        break;
      case 'E':
        moved = game.playerMove(Direction.EAST);
        break;
      case 'S':
        moved = game.playerMove(Direction.SOUTH);
        break;
      default:
        out.append("Not a valid command!\n");
        break;
    }
    return moved;
  }

  private MonsterImpl shoot(Game game) throws IOException {
    out.append("Wanna shot?(y/n) ");
    char choice = scan.nextLine().toUpperCase(Locale.ROOT).charAt(0);
    switch (choice) {
      case 'N':
        break;
      case 'Y':
        out.append("shoot to? ");
        boolean moved = false;
        char character = scan.nextLine().toUpperCase(Locale.ROOT).charAt(0);
        out.append("distance(1-5)? ");
        int dis = Integer.parseInt(scan.nextLine());
        if (dis <= 0 || dis > 5) {

          return null;
        }
        switch (character) {
          case 'W':
            return game.shootArrow(Direction.WEST, dis);

          case 'N':
            return game.shootArrow(Direction.NORTH, dis);

          case 'E':
            return game.shootArrow(Direction.EAST, dis);

          case 'S':
            return game.shootArrow(Direction.SOUTH, dis);

          default:
            return null;
        }
      default:
        throw new IllegalStateException("Unexpected value: " + choice);
    }
    return null;
  }
}
