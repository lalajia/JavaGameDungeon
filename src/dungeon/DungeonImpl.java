package dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import location.Location;
import location.LocationImpl;
import monster.Monster;
import monster.MonsterImpl;
import treasure.TreasureImpl;
import treasure.TreasureType;


/**
 * Dungeon Implementation for using Kruskal to build the dungeon randomly. Set the treasure percent
 * into the dungeon.
 */
public class DungeonImpl implements Dungeon {
  private static final String WRAPPING = "Wrapping";
  private static final String NON_WRAPPING = "Nonwrapping";
  private Random random;
  private String wrappingType;

  private int percent;
  private boolean wrap;
  private HashMap<Integer, ArrayList<TreasureImpl>> treasureMap;
  private final HashMap<Integer, ArrayList<Arrow>> arrowMap;
  private final HashMap<Integer, ArrayList<MonsterImpl>> monsterMap;
  //1111 if north, east, south, west is available
  private final int[][] data;
  private int[] par;

  private ArrayList<Edge> leftEdges;
  private int numOfMonster;
  private Location[] res;

  /**
   * init data random route with Kruskal.
   *
   * @param width        the width
   * @param height       the height
   * @param wrappingType is wrapping
   */
  public DungeonImpl(int width, int height, String wrappingType) {
    data = new int[height][width];
    wrap = WRAPPING.equals(wrappingType);
    this.wrappingType = wrappingType;
    this.random = new Random();
    this.treasureMap = new HashMap<>();
    this.leftEdges = new ArrayList<>();
    initKruskal();
    arrowMap = new HashMap<>();
    monsterMap = new HashMap<>();

  }

  private class Edge {
    private int verticeOne;
    private int verticeTwo;
    private Direction direction;

    private Edge(int a, Direction dir) {
      this.verticeOne = a;
      this.direction = dir;
      int x = a % data[0].length;
      int y = a / data[0].length;
      if (direction == Direction.EAST) {
        if (x + 1 == data[0].length) {
          verticeTwo = y * data[0].length;
        } else {
          verticeTwo = y * data[0].length + x + 1;
        }

      } else if (direction == Direction.SOUTH) {
        if (y + 1 == data.length) {
          verticeTwo = x;
        } else {
          verticeTwo = (y + 1) * data[0].length + x;
        }

      } else {
        throw new RuntimeException("error");
      }
    }

    void addToData() {
      int x = this.verticeOne % data[0].length;
      int y = this.verticeOne / data[0].length;
      int x1 = this.verticeTwo % data[0].length;
      int y1 = this.verticeTwo / data[0].length;
      if (this.direction == Direction.EAST) {
        data[y][x] = data[y][x] | (1 << 2);
        data[y1][x1] = data[y1][x1] | (1);
      } else if (this.direction == Direction.SOUTH) {
        data[y][x] = data[y][x] | (1 << 3);
        data[y1][x1] = data[y1][x1] | (1 << 1);
      }
    }
  }

  private void initKruskal() {
    // init
    ArrayList<Edge> usedList = new ArrayList<>();
    par = new int[data.length * data[0].length];
    for (int i = 0; i < par.length; i++) {
      par[i] = -1;
    }
    if (wrap) {
      for (int i = 0; i < data[0].length; i++) {
        // top line link bottom line
        int top = i;
        int bottom = (data.length - 1) * data[0].length + i;
        Edge edge = new Edge(bottom, Direction.SOUTH);
        if (find(top) != find(bottom)) {
          union(top, bottom);
          usedList.add(edge);
        }
      }
      for (int i = 0; i < data.length; i++) {
        // left line link right line
        int left = i * data[0].length;
        int right = left + data[0].length - 1;
        Edge edge = new Edge(right, Direction.EAST);
        if (find(left) != find(right)) {
          union(left, right);
          usedList.add(edge);
        }
      }
    }
    // init all edge;
    ArrayList<Edge> sources = new ArrayList<>();
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        if (i != data.length - 1) {
          sources.add(new Edge(i * data[0].length + j, Direction.SOUTH));
        }
        if (j != data[0].length - 1) {
          sources.add(new Edge(i * data[0].length + j, Direction.EAST));
        }

      }
    }
    while (find(0) != -par.length && sources.size() > 0) {
      Edge cur = sources.get(random.nextInt(sources.size()));
      sources.remove(cur);
      if (find(cur.verticeOne) != find(cur.verticeTwo)) {
        union(cur.verticeOne, cur.verticeTwo);
        usedList.add(cur);
      } else {
        leftEdges.add(cur);
      }
    }
    leftEdges.addAll(sources);
    // get useful edges;
    for (Edge edge : usedList) {
      edge.addToData();
    }
  }

  //union two vertices
  private void union(int a, int b) {
    int ra = find(a);
    int rb = find(b);
    int num = par[ra] + par[rb];
    par[rb] = ra;
    par[ra] = num;

  }

  @Override
  public String getWrappingType() {
    return this.wrappingType;
  }

  @Override
  public void setTreasurePercent(int percent) {
    this.percent = percent;
  }

  @Override
  public int getTreasurePercent() {
    return percent;
  }

  @Override
  public void assignTreasure() {
    ArrayList<int[]> caves = getCaves();
    int count = data.length * data[0].length * percent / 100;
    for (int i = 0; i < count; i++) {
      int[] cave = caves.get(random.nextInt(caves.size()));
      int type = random.nextInt(3);
      TreasureImpl treasureImpl;
      switch (type) {
        case 1:
          treasureImpl = new TreasureImpl(TreasureType.RUBY);
          break;
        case 2:
          treasureImpl = new TreasureImpl(TreasureType.DIAMOND);
          break;
        default:
          treasureImpl = new TreasureImpl(TreasureType.SAPPHIRE);
          break;
      }
      int key = cave[0] * data[0].length + cave[1];
      ArrayList<TreasureImpl> val = treasureMap.get(key);
      if (val == null) {
        val = new ArrayList<>();
        treasureMap.put(key, val);
      }
      val.add(treasureImpl);
    }
  }

  private int getEntrances(int val) {
    int count = 0;
    for (int i = 0; i < 4; i++) {
      if ((val & (1 << i)) > 0) {
        count++;
      }
    }
    return count;
  }

  private String bl(int val, int len) {
    StringBuilder bs = new StringBuilder(Integer.toBinaryString(val));
    while (bs.length() < len) {
      bs.insert(0, "0");
    }
    return bs.toString();
  }

  private ArrayList<int[]> getCaves() {
    ArrayList<int[]> res = new ArrayList<>();
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        if (getEntrances(data[i][j]) != 2) {
          res.add(new int[] { i, j });
        }
      }
    }
    return res;
  }

  @Override
  public ArrayList<String> getCaveInfo() {
    ArrayList<int[]> caves = getCaves();
    ArrayList<String> caveInfos = new ArrayList<>();
    for (int[] cave : caves) {
      caveInfos.add("(" + cave[0] + "," + cave[1] + ") " + bl(data[cave[0]][cave[1]], 4));
    }
    return caveInfos;
  }

  @Override
  public Location[] randomRoute() {
    res = new Location[2];

    ArrayList<int[]> tmp = getCaves();
    int[] startI = tmp.get(random.nextInt(tmp.size()));

    res[0] = new LocationImpl(startI[1], startI[0]);
    Location start = res[0];
    boolean[][] visited = new boolean[data.length][data[0].length];
    ArrayList<Location> curs = new ArrayList<>();
    curs.add(start);
    visited[start.getPlayerY()][start.getPlayerX()] = true;
    int min = 5;
    int max = data.length * data[0].length / 2;
    int lineLen = min;
    if (max > min) {
      lineLen = random.nextInt(max - min) + min;
    }

    for (int i = 0; i < lineLen; i++) {
      ArrayList<Location> nexts = new ArrayList<>();
      for (int j = 0; j < curs.size(); j++) {
        Location loc = curs.get(j);
        //West
        if ((data[loc.getPlayerY()][loc.getPlayerX()] & 1) > 0) {
          Location next = new LocationImpl(loc.getPlayerX() - 1, loc.getPlayerY());
          if (next.getPlayerX() < 0) {
            next.setPlayerX(data[0].length - 1);
          }
          if (next.getPlayerX() >= data[0].length) {
            next.setPlayerX(0);
          }
          if (!visited[next.getPlayerY()][next.getPlayerX()]) {
            nexts.add(next);
            visited[next.getPlayerY()][next.getPlayerX()] = true;
          }
        }
        //North
        if ((data[loc.getPlayerY()][loc.getPlayerX()] & (1 << 1)) > 0) {
          Location next = new LocationImpl(loc.getPlayerX(), loc.getPlayerY() - 1);
          if (next.getPlayerY() < 0) {
            next.setPlayerY(data.length - 1);
          }
          if (next.getPlayerY() >= data.length) {
            next.setPlayerY(0);
          }
          if (!visited[next.getPlayerY()][next.getPlayerX()]) {
            nexts.add(next);
            visited[next.getPlayerY()][next.getPlayerX()] = true;
          }
        }
        //east
        if ((data[loc.getPlayerY()][loc.getPlayerX()] & (1 << 2)) > 0) {
          Location next = new LocationImpl(loc.getPlayerX() + 1, loc.getPlayerY());
          if (next.getPlayerX() < 0) {
            next.setPlayerX(data[0].length - 1);
          }
          if (next.getPlayerX() >= data[0].length) {
            next.setPlayerX(0);
          }
          if (!visited[next.getPlayerY()][next.getPlayerX()]) {
            nexts.add(next);
            visited[next.getPlayerY()][next.getPlayerX()] = true;
          }
        }
        //south
        if ((data[loc.getPlayerY()][loc.getPlayerX()] & (1 << 3)) > 0) {
          Location next = new LocationImpl(loc.getPlayerX(), loc.getPlayerY() + 1);
          if (next.getPlayerY() < 0) {
            next.setPlayerY(data.length - 1);
          }
          if (next.getPlayerY() >= data.length) {
            next.setPlayerY(0);
          }
          if (!visited[next.getPlayerY()][next.getPlayerX()]) {
            nexts.add(next);
            visited[next.getPlayerY()][next.getPlayerX()] = true;
          }
        }
      }

      curs = nexts;
    }
    ArrayList<Location> canvas = new ArrayList<>();
    ArrayList<int[]> cv = getCaves();
    for (int i = 0; i < cv.size(); i++) {
      canvas.add(new LocationImpl(cv.get(i)[1], cv.get(i)[0]));
    }
    ArrayList<Location> filter = new ArrayList<>();
    for (Location lo : curs) {
      if (canvas.contains(lo)) {
        filter.add(lo);
      }
    }
    if (filter.size() == 0) {
      return randomRoute();
    }
    res[1] = filter.get(random.nextInt(filter.size()));
    return res;
  }

  @Override
  public ArrayList<TreasureImpl> currentTreasure(Location next) {
    int key = next.getPlayerY() * data[0].length + next.getPlayerX();
    ArrayList<TreasureImpl> list = treasureMap.get(key);
    treasureMap.put(key, null);
    return list == null ? new ArrayList<>() : list;
  }

  @Override
  public String showTreasure() {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<Integer, ArrayList<TreasureImpl>> entry : treasureMap.entrySet()) {
      if (entry.getValue() == null) {
        continue;
      }
      builder.append("(").append(entry.getKey() % data[0].length).append(',')
          .append(entry.getKey() / data[0].length).append(")");
      for (TreasureImpl treasureImpl : entry.getValue()) {
        builder.append(' ').append(treasureImpl.getType().name());
      }
      builder.append('\n');
    }
    return builder.toString();
  }

  private int find(int a) {
    int root = a;
    while (par[root] >= 0) {
      root = par[root];
    }
    // zip route
    int k = a;
    while (k != root) {
      int tmp = par[k];
      par[k] = root;
      k = tmp;
    }
    return root;
  }

  @Override
  public ArrayList<TreasureImpl> find(Location loc) {
    int key = loc.getPlayerY() * data[0].length + loc.getPlayerX();
    ArrayList<TreasureImpl> list = treasureMap.get(key);
    return list == null ? new ArrayList<>() : list;
  }

  @Override
  public Location checkMove(Direction direction, Location cur) {
    Location location = cur;
    int x = location.getPlayerX();
    int y = location.getPlayerY();
    switch (direction) {
      case WEST:
        if ((data[y][x] & 1) == 0) {
          return null;
        }
        x--;
        break;
      case NORTH:
        if ((data[y][x] & (1 << 1)) == 0) {
          return null;
        }
        y--;
        break;
      case EAST:
        if ((data[y][x] & (1 << 2)) == 0) {
          return null;
        }
        x++;
        break;
      case SOUTH:

        if ((data[y][x] & (1 << 3)) == 0) {
          return null;
        }
        y++;
        break;
      default:
        break;
    }
    if (x < 0) {
      x = data[0].length - 1;
    }
    if (x >= data[0].length) {
      x = 0;
    }
    if (y < 0) {
      y = data.length - 1;
    }
    if (y >= data.length) {
      y = 0;
    }
    return new LocationImpl(x, y);

  }

  @Override
  public int[][] getData() {
    return data;
  }

  @Override
  public void addInterconnectivity(int interconnectivity) {
    for (int i = 0; i < interconnectivity; i++) {
      if (leftEdges.size() > 0) {
        int index = random.nextInt(leftEdges.size());
        Edge edge = leftEdges.get(index);
        edge.addToData();
        leftEdges.remove(index);
      }
    }
  }

  @Override
  public ArrayList<Direction> getCanMove(Location loc) {
    int val = data[loc.getPlayerY()][loc.getPlayerX()];
    ArrayList<Direction> directions = new ArrayList<>();
    if ((val & 1) > 0) {
      directions.add(Direction.WEST);
    }
    if ((val & (1 << 1)) > 0) {
      directions.add(Direction.NORTH);
    }
    if ((val & (1 << 2)) > 0) {
      directions.add(Direction.EAST);
    }
    if ((val & (1 << 3)) > 0) {
      directions.add(Direction.SOUTH);
    }
    return directions;
  }

  @Override
  public void assignArrows() {
    ArrayList<int[]> caves = getCaves();
    for (int i = 0; i < numOfMonster * 3; i++) {
      int[] cave = caves.get(random.nextInt(caves.size()));
      int key = cave[0] * data[0].length + cave[1];
      ArrayList<Arrow> val = arrowMap.get(key);
      if (val == null) {
        val = new ArrayList<>();
        arrowMap.put(key, val);
      }
      val.add(Arrow.ARROW);
    }
  }

  @Override
  public void assignMonsters() {
    ArrayList<int[]> caves = getCaves();
    // must in end
    Location end = res[1];
    Location start = res[0];
    int key1 = end.getPlayerY() * data[0].length + end.getPlayerX();
    ArrayList<MonsterImpl> val1 = monsterMap.get(key1);
    if (val1 == null) {
      val1 = new ArrayList<>();
      monsterMap.put(key1, val1);
    }
    val1.add(new MonsterImpl(end));
    for (int i = 0; i < numOfMonster - 1; i++) {
      int[] cave = caves.get(random.nextInt(caves.size()));
      if (cave[0] == start.getPlayerY() && cave[1] == start.getPlayerX()) {
        i--;
        continue;
      }
      int key = cave[0] * data[0].length + cave[1];
      ArrayList<MonsterImpl> val = monsterMap.get(key);
      if (val == null) {
        val = new ArrayList<>();
        monsterMap.put(key, val);
      }
      val.add(new MonsterImpl(new LocationImpl(cave[1], cave[0])));
    }
  }

  @Override
  public boolean isCave(Location location) {
    return getEntrances(data[location.getPlayerY()][location.getPlayerX()]) != 2;
  }

  @Override
  public void setMonsterNum(int num) {
    this.numOfMonster = num;
  }

  @Override
  public ArrayList<Monster> getMonsters() {
    ArrayList<Monster> monsters = new ArrayList<>();
    for (ArrayList<MonsterImpl> ms : monsterMap.values()) {
      monsters.addAll(ms);
    }
    return monsters;
  }

  @Override
  public ArrayList<Arrow> getArrows() {
    ArrayList<Arrow> arrows = new ArrayList<>();
    for (ArrayList<Arrow> ms : arrowMap.values()) {
      arrows.addAll(ms);
    }
    return arrows;
  }

  @Override
  public ArrayList<Arrow> findArrow(Location location) {
    int key = location.getPlayerY() * data[0].length + location.getPlayerX();
    ArrayList<Arrow> list = arrowMap.get(key);

    return list == null ? new ArrayList<>() : list;
  }

  @Override
  public ArrayList<Arrow> pullArrow(Location location) {
    int key = location.getPlayerY() * data[0].length + location.getPlayerX();
    ArrayList<Arrow> list = arrowMap.get(key);
    arrowMap.put(key, null);
    return list == null ? new ArrayList<>() : list;
  }

  @Override
  public ArrayList<MonsterImpl> findMonster(Location location) {
    int key = location.getPlayerY() * data[0].length + location.getPlayerX();
    ArrayList<MonsterImpl> list = monsterMap.get(key);
    return list == null ? new ArrayList<>() : list;
  }

  @Override
  public ArrayList<MonsterImpl> monsterFromOneStep(Location location) {
    ArrayList<MonsterImpl> list = new ArrayList<>();
    dfs(location, 1, list, new ArrayList<>());
    return list;
  }

  private void dfs(Location location, int maxdeep, ArrayList<MonsterImpl> list,
      ArrayList<Location> locations) {
    if (locations.contains(location)) {
      return;
    }
    if (!valid(location)) {
      return;
    }
    if (maxdeep == 0) {
      list.addAll(findMonster(location));
      return;
    }
    locations.add(location);
    for (Direction dir : getCanMove(location)) {
      switch (dir) {
        case WEST:
          dfs(new LocationImpl(location.getPlayerX() - 1, location.getPlayerY()), maxdeep - 1, list,
              locations);
          break;
        case EAST:
          dfs(new LocationImpl(location.getPlayerX() + 1, location.getPlayerY()), maxdeep - 1, list,
              locations);
          break;
        case NORTH:
          dfs(new LocationImpl(location.getPlayerX(), location.getPlayerY() - 1), maxdeep - 1, list,
              locations);

          break;
        case SOUTH:
          dfs(new LocationImpl(location.getPlayerX(), location.getPlayerY() + 1), maxdeep - 1, list,
              locations);
          break;
        default:
          break;
      }
    }

  }

  private boolean valid(Location location) {
    if (location.getPlayerX() < 0 || location.getPlayerX() >= data[0].length) {
      return false;
    }
    if (location.getPlayerY() < 0 || location.getPlayerY() >= data.length) {
      return false;
    }
    return true;
  }

  @Override
  public ArrayList<MonsterImpl> monsterFromTwoStep(Location location) {
    ArrayList<MonsterImpl> list = new ArrayList<>();
    dfs(location, 2, list, new ArrayList<>());
    return list;
  }

  @Override
  public MonsterImpl shootArrow(Location playerLocation, Direction direction, int distance) {
    Location target = new LocationImpl(playerLocation.getPlayerX(), playerLocation.getPlayerY());
    Direction dir = direction;
    for (int i = 0; i < distance; i++) {
      if (!valid(target)) {
        return null;
      }
      if (isCave(target)) {
        if (!getCanMove(target).contains(dir)) {
          return null;
        }

      } else {
        ArrayList<Direction> dirs = getCanMove(target);
        if (i > 0) {
          switch (dir) {
            case WEST:
              dirs.remove(Direction.EAST);
              break;
            case NORTH:
              dirs.remove(Direction.SOUTH);
              break;
            case EAST:
              dirs.remove(Direction.WEST);
              break;
            case SOUTH:
              dirs.remove(Direction.NORTH);
              break;
            default:
              break;
          }

          dir = dirs.get(0);
        } else {
          if (!dirs.contains(dir)) {
            return null;
          }
        }
      }
      switch (dir) {
        case WEST:
          target.setPlayerX(target.getPlayerX() - 1);
          break;
        case NORTH:
          target.setPlayerY(target.getPlayerY() - 1);
          break;
        case EAST:
          target.setPlayerX(target.getPlayerX() + 1);
          break;
        case SOUTH:
          target.setPlayerY(target.getPlayerY() + 1);
          break;
        default:
          break;
      }
    }
    ArrayList<MonsterImpl> list = findMonster(target);
    if (list.size() > 0) {
      list.get(0).reduceHealth();
      if (list.get(0).getHealth() == 0) {
        return list.remove(0);
      }
      return list.get(0);
    }
    return null;
  }
}