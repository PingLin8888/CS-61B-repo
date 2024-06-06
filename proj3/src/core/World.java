package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {

    // build your own world!
    final private static int WIDTH = 70;
    final private static int HEIGHT = 40;
    final private static TETile UNUSED = Tileset.NOTHING;
    final private static TETile FLOOR = Tileset.FLOOR;
    final private static TETile WALL = Tileset.WALL;
    private static final long SEED = 2873123;
    private Random random;
    private TETile[][] map;
    private ArrayList<Room> rooms;//might sort the rooms base on the location
    private ArrayList<Hallway> hallways;
    private Map<Room, List<Room>> graph;

    private Set<Point> usedSpaces;

    public World() {
        rooms = new ArrayList<>();
        hallways = new ArrayList<>();
        random = new Random(SEED);
        graph = new HashMap<>();
        usedSpaces = new HashSet<>();
        map = new TETile[WIDTH][HEIGHT];
        initializeWorld();
    }

    private void initializeWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = UNUSED;
            }
        }
    }

    public void buildWorld() {
        generateRoom(4);
        for (int i = 0; i < rooms.size() - 1; i++) {
            connectRooms(rooms.get(i), rooms.get(i + 1));
        }
    }

    public void generateRoom(int roomNums) {
        //room should be within the boundaries of the world grid.
        for (int i = 0; i < roomNums; i++) {
            int width = random.nextInt(5) + 3;
            int height = random.nextInt(5) + 3;
            int x = random.nextInt(WIDTH - width - 2) + 1;
            int y = random.nextInt(HEIGHT - height - 2) + 1;
            Room newRoom = new Room(width, height, x, y);
            Iterable<Point> points = roomPoints(newRoom);
            if (!isColliding(points)) {
                rooms.add(newRoom);
                graph.put(newRoom, new ArrayList<>());
                markUsed(points);
                placeRoom(newRoom);
            }
        }
        Collections.sort(rooms);
    }

    private boolean areAllRoomsConnected() {
        if (rooms.isEmpty()) {
            return true;
        }
        Set<Room> visited = new HashSet<>();
        Queue<Room> queue = new LinkedList<>();
        queue.add(rooms.getFirst());
        while (!queue.isEmpty()) {
            Room current = queue.poll();
            visited.add(current);
            for (Room neighbour : graph.get(current)) {
                if (!visited.contains(neighbour)) {
                    queue.add(neighbour);
                }
            }
        }
        return visited.size() == rooms.size();
    }

    private void markUsed(Iterable<Point> points) {
        for (Point p : points) {
            usedSpaces.add(p);
        }
    }


    private boolean isColliding(Iterable<Point> points) {
        for (Point p : points) {
            if (usedSpaces.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private Iterable<Point> roomPoints(Room room) {
        List<Point> points = new ArrayList<>();
        int x = room.getPositionX();
        int y = room.getPositionY();
        int width = room.getWidth();
        int height = room.getHeight();
        for (int i = x; i <= x + width; i++) {
            for (int j = y; j <= y + height; j++) {
                points.add(new Point(i, j));
            }
        }
        return points;
    }

    private Iterable<Point> hallwayPoints(Hallway hallway) {
        if (hallway instanceof StraightHallway) {
            return getStraightHallwayPoints((StraightHallway) hallway);
        } else {
            TurnHallway turnHallway = (TurnHallway) hallway;
            StraightHallway firstPartOfTurn = new StraightHallway(turnHallway.startX, turnHallway.startY, turnHallway.getMidX(), turnHallway.getMidY());
            StraightHallway secondPartOfTurn = new StraightHallway(turnHallway.getMidX(), turnHallway.getMidY(), turnHallway.endX, turnHallway.endY);
            List<Point> points = (List<Point>) getStraightHallwayPoints(firstPartOfTurn);
            points.addAll((Collection<? extends Point>) getStraightHallwayPoints(secondPartOfTurn));
            return points;
        }
    }

    private Iterable<Point> getStraightHallwayPoints(StraightHallway hallway) {
        List<Point> points = new ArrayList<>();
        if (hallway.isVertical()) {
            for (int i = hallway.startX; i < hallway.startX + 3; i++) {
                for (int j = hallway.startY; j <= hallway.endY; j++) {
                    points.add(new Point(i, j));
                }
            }
        } else if (hallway.isHorizontal()) {
            for (int i = hallway.startX; i >= hallway.endX; i--) {
                for (int j = hallway.startY; j < hallway.endY + 3; j++) {
                    points.add(new Point(i, j));
                }
            }
        }
        return points;
    }


    private void placeRoom(Room room) {
        int x = room.getPositionX();
        int y = room.getPositionY();
        int width = room.getWidth();
        int height = room.getHeight();
        for (int i = x + 1; i < x + width; i++) {
            for (int j = y + 1; j < y + height; j++) {
                map[i][j] = FLOOR;
            }
        }
        for (int i = x; i <= x + width; i++) {
            map[i][y] = WALL;
            map[i][y + height] = WALL;
        }
        for (int j = y; j <= y + height; j++) {
            map[x][j] = WALL;
            map[x + width][j] = WALL;
        }
    }

    public void connectRooms(Room room1, Room room2) {
        Hallway hallway = createHallway(room1, room2);
        hallways.add(hallway);
        markUsed(hallwayPoints(hallway));
        placeHallway(hallway);


        graph.get(room1).add(room2);
        graph.get(room2).add(room1);
    }


    private Hallway createHallway(Room room1, Room room2) {
        int x1 = room1.getPositionX() + room1.getWidth() / 2;
        int y1 = room1.getPositionY() + room1.getHeight() / 2;
        Hallway hallway = new Hallway();
        //vertical straight hallway
        if (x1 >= room2.getPositionX() && x1 <= room2.getPositionX() + room2.getWidth()) {
            //room2 is above room1
            if (y1 < room2.getPositionY()) {
                hallway = new StraightHallway(x1, y1 + room1.getHeight() / 2, x1, room2.getPositionY());
            } else if (y1 > room2.getPositionY()) {
                hallway = new StraightHallway(x1, y1 - room1.getHeight() / 2, x1, room2.getPositionY() + room2.getHeight());
            }
            //horizontal straight hallway
        } else if (y1 >= room2.getPositionY() && y1 <= room2.getPositionY() + room2.getHeight()) {
            //room2 is on the left of room1
            if (x1 > room2.getPositionX()) {
                hallway = new StraightHallway(x1 - room1.getWidth() / 2, y1, room2.getPositionX() + room2.getWidth(), y1);
            } else if (x1 < room2.getPositionX()) {
                hallway = new StraightHallway(x1 + room1.getWidth() / 2, y1, room2.getPositionX(), y1);
            }
        }
        //create turn hallway
        else {
            int midX = room2.getPositionX();
            int midY = room1.getPositionY();
            hallway = new TurnHallway(room1.getPositionX() + room1.getWidth(), room1.getPositionY(), midX, midY, room2.getPositionX(), room2.getPositionY());
        }
        return hallway;
    }


    private void placeHallway(Hallway hallway) {
        if (hallway instanceof StraightHallway) {
            placeStraightHallway(hallway);
        } else {
            //turn hallway
            TurnHallway turnHallway = (TurnHallway) hallway;
            placeTurnHallway(turnHallway);
        }
    }

    private void placeTurnHallway(TurnHallway hallway) {
        int minX = Math.min(hallway.getStartX(), Math.min(hallway.getMidX(), hallway.getEndX()));
        int maxX = Math.max(hallway.getStartX(), Math.max(hallway.getMidX(), hallway.getEndX()));
        int minY = Math.min(hallway.getStartY(), Math.min(hallway.getMidY(), hallway.getEndY()));
        int maxY = Math.max(hallway.getStartY(), Math.max(hallway.getMidY(), hallway.getEndY()));
        int x1 = hallway.startX;
        int x2 = hallway.getMidX();
        int y1 = hallway.startY;
        int y2 = hallway.endY;
        drawLShape(x1, x2 + 1, y1 + 1, y2, FLOOR);
        //lower wall
        drawLShape(x1, x2 + 2, y1, y2, WALL);
        //upper wall
        drawLShape(x1, x2, y1 + 2, y2, WALL);
    }

    private void drawLShape(int x1, int x2, int y1, int y2, TETile tileSet) {
        for (int i = x1; i <= x2; i++) {
            map[i][y1] = tileSet;
        }
        for (int j = y1; j <= y2; j++) {
            map[x2][j] = tileSet;
        }
    }

    private void placeStraightHallway(Hallway hallway) {
        if (hallway.isVertical()) {
            for (int i = hallway.startX; i <= hallway.startX + 2; i += 2) {
                for (int j = hallway.startY; j <= hallway.endY; j++) {
                    if (map[i][j] != FLOOR) {
                        map[i][j] = WALL;
                    }
                }
            }
            for (int j = hallway.startY; j <= hallway.endY; j++) {
                map[hallway.startX + 1][j] = FLOOR;
            }
        } else {
            for (int i = hallway.startX; i >= hallway.endX; i--) {
                for (int j = hallway.startY; j <= hallway.startY + 2; j += 2) {
                    if (map[i][j] != FLOOR) {
                        map[i][j] = WALL;
                    }
                }
            }
            for (int i = hallway.startX; i >= hallway.endX; i--) {
                map[i][hallway.startY + 1] = FLOOR;
            }
        }
    }

    public TETile[][] getMap() {
        return map;
    }


}
