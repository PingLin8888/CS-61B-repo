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
            for (int i = hallway.startX; i <= hallway.endX; i++) {
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
        markUsed(hallwayPoints(hallway));
        placeHallway(hallway);


        graph.get(room1).add(room2);
        graph.get(room2).add(room1);
    }

    //check if points in room1 and room2 share respectively have some points that shared the same x or y.
    //yes, staight. get the point as the start point and end point;
    // no, turn. take middle point of room1 as start point, room2 as end point.
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
