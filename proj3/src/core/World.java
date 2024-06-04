package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {

    // build your own world!
    final private static int WIDTH = 100;
    final private static int HEIGHT = 100;
    final private static TETile UNUSED = Tileset.NOTHING;
    final private static TETile FLOOR = Tileset.FLOOR;
    final private static TETile WALL = Tileset.WALL;

    private Random random;
    private TETile[][] world;
    private ArrayList<Room> rooms;
    private ArrayList<Hallway> hallways;
    private Map<Room, List<Room>> graph;

    private Set<Point> usedSpaces;

    public World(Long seed) {
        rooms = new ArrayList<>();
        hallways = new ArrayList<>();
        random = new Random(seed);
        graph = new HashMap<>();
        initializeWorld();
    }

    private void initializeWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = UNUSED;
            }
        }
    }

    public void generateRoom(int roomNums) {
        //room should be within the boundaries of the world grid.
        for (int i = 0; i < roomNums; i++) {
            int width = random.nextInt(5) + 3;
            int height = random.nextInt(5) + 3;
            int x = random.nextInt(WIDTH - width);
            int y = random.nextInt(HEIGHT - height);
            Room newRoom = new Room(width, height, x, y);
            Iterable<Point> points = roomPoints(newRoom);
            if (!isColliding(points)) {
                rooms.add(newRoom);
                markUsed(points);
                placeRoom(newRoom);
            }
        }
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

    }

    private void placeRoom(Room room) {
        int x = room.getPositionX();
        int y = room.getPositionY();
        int width = room.getWidth();
        int height = room.getHeight();
        for (int i = x + 1; i <= x + width - 1; i++) {
            for (int j = y + 1; j <= y + height - 1; j++) {
                world[i][j] = FLOOR;
            }
        }
        for (int i = x; i <= x + width; i += width) {
            for (int j = y; j <= y + height; j += height) {
                world[i][j] = WALL;
            }
        }
    }

    public void connectRooms(Room room1, Room room2) {
        Hallway hallway = createHallway(room1, room2);
        hallways.add(hallway);
        placeHallway(hallway);


        graph.get(room1).add(room2);
        graph.get(room2).add(room1);
    }


    private Hallway createHallway(Room room1, Room room2) {
        if (isStraightHallway(room1, room2)) {
            
            Hallway straight = new StraightHallway()
        }
    }

    private boolean isStraightHallway(Room room1, Room room2) {
        if (room2.getPositionY() > room1.getPositionY() + room1.getHeight()) {
            if (room2.getPositionX() + room1.getWidth() < room1.getPositionX() || room2.getPositionX() > room1.getPositionX() + room1.getWidth()) {
                return false;
            }
        } else if (room2.getPositionY() + room2.getHeight() < room1.getPositionY()) {
            if (room2.getPositionX() + room1.getWidth() < room1.getPositionX() || room2.getPositionX() > room1.getPositionX() + room1.getWidth()) {
                return false;
            }
        }
        return true;
    }

    private void placeHallway(Hallway straightHallway) {
        
    }

}
