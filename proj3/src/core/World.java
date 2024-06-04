package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

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
            if (!isColliding(newRoom)) {
                rooms.add(newRoom);
                placeRoom(newRoom);
            }
        }
    }


    private boolean isColliding(Room newRoom) {
        for (Room room : rooms) {
            if (room.isOverlap(newRoom)) {
                return true;
            }
        }
        return false;
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
        Hallway straightHallway = createHallway(room1, room2);
        hallways.add(straightHallway);
        placeHallway(straightHallway);


        graph.get(room1).add(room2);
        graph.get(room2).add(room1);
    }

    //in this way, I'm getting 2 hallways. Am i returning a list of hallways?
    // If it's taken as a turn hallway, it's one.
    //if x1=x2 or y1=y2,this won't be a turn hallway.
    private Hallway createHallway(Room room1, Room room2) {
        int x1 = room1.getPositionX() + room1.getWidth() / 2;
        int y1 = room1.getPositionY() + room1.getHeight() / 2;
        int x2 = room2.getPositionX() + room2.getWidth() / 2;
        int y2 = room2.getPositionY() + room2.getHeight() / 2;
        if (random.nextBoolean()) {

        }
    }

    private void placeHallway(Hallway straightHallway) {
        
    }

}
