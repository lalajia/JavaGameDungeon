## 1. About/Overview.

The world for our game consists of a dungeon, a network of tunnels and caves that are interconnected so that player can
explore the entire world by travelling from cave to cave through the tunnels that connect them.

## 2. List of features.

The project provides following features:

* both wrapping and non-wrapping dungeons to be created with different degrees of interconnectivity
* provide support for at least three types of treasure: diamonds, rubies, and sapphires
* treasure to be added to a specified percentage of caves.
* monster to be added with a specified number.
* a player to enter the dungeon at the start.
* provide a description of the player that, at a minimum, includes a description of what treasure the player has
  collected
* provide a description of the player's location that at the minimum includes a description of treasure in the room and
  the possible moves (north, east, south, west) that the player can make from their current location
* a player to move from their current location
* a player to pick up treasure that is located in their same location
* a player to pick up treasure that is located in their same location
* a player can shoot arrow with direction to kill a monster
* a player can escape from a monster or die
* a monster will locate at the end cave
* One cave can have multiple monster

## 3. How To Run.

### How to run the jar file

To run the jar file use the following command in the terminal.

```
java -jar <jarName> 
```

The jar file added in the res folder is name dungeon.jar.

### Run 1 -- Example1.txt:
* an Otyugh at the end cave in one of the example runs
* detect the smell of a Otyugh that is at least 2 positions from the player's current location
* detect the smell of a Otyugh that is 1 positions from the player's current location(smell increase)
* detect multiple Otyughs with the number and locations
* player have 3 arrows when they enter the dungeon
* additional arrows to be found in the dungeon
* player pick up arrows that are found in the dungeon 
* player shoot an arrow
* hitting an Otyugh with an arrow
* missing an Otyugh with an arrow
* winning by killing the Otyugh and entering the end cave

### Run 2 -- Example2.txt:
* player reach the end cave with healthy Otyugh and died.
* detect the smell of a Otyugh that is at least 2 positions from the player's current location
* detect the smell of a Otyugh that is 1 positions from the player's current location(smell increase)
* detect multiple Otyughs with the number and locations
* player have 3 arrows when they enter the dungeon
* additional arrows to be found in the dungeon
* player pick up arrows that are found in the dungeon
* player shoot an arrow

## 4. Design/Model Changes.

Add more method in DungeonImpl for detect Monster and shoot Arrows.
See res folder: project4before.png. project4after.png.

## 5. Assumptions.

* The width and height of the dungeon need to be over 6.
* In the driver class, type (W/N/E/S) for (west/north/east/south)
* The treasure percent is calculated by the number of treasures. So if the cave have two treasures, it counts two.

## 6. Limitations.

* The user input is case-sensitive, "Wrapping" and "Nonwrapping" are the only correct input.

## 7. Citations.

1. https://docs.google.com/presentation/d/1o_r7Dyo1SW49TZv5BQhVk2z4ZZtzr0u9dtmrIsQvAAU/edit#slide=id.g178eaf4b4f8_0_124
2. https://cp-algorithms.com/graph/mst_kruskal.html