package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {

    // build your own world!
    final private static int WIDTH = 80;
    final private static int HEIGHT = 45;
    final private static TETile UNUSED = Tileset.NOTHING;
    final private static TETile FLOOR = Tileset.FLOOR;
    final private static TETile WALL = Tileset.WALL;
    final private static long SEEDDefault = 87654L;
    final private static TETile AVATAR = Tileset.AVATAR;
    final private static TETile CHASER = Tileset.FLOWER;

    private int avatarX, avatarY;
    private int chaseX, chaseY;
    private boolean isShowPath;
    private List<Point> pathToAvatar;

    private long seed;
    private Random random;
    private TETile[][] map;
    private ArrayList<Room> rooms;
    private ArrayList<Hallway> hallways;
    private Set<Point> usedSpaces;

    public World() {
        this(SEEDDefault);
    }

    public World(Long seed) {
        this.seed = seed;
        initializeWorldComponents();
    }

    private void initializeWorldComponents() {
        rooms = new ArrayList<>();
        hallways = new ArrayList<>();
        random = new Random(seed);
        usedSpaces = new HashSet<>();
        map = new TETile[WIDTH][HEIGHT];
        initializeWorld();
        placeAvatar();
        placeChaser();
    }

    private void placeAvatar() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (map[i][j] == FLOOR) {
                    avatarX = i;
                    avatarY = j;
                    map[i][j] = AVATAR;
                    return;
                }
            }
        }
    }

    private void placeChaser() {
        for (int i = WIDTH - 1; i >= 0; i--) {
            for (int j = HEIGHT - 1; j >= 0; j--) {
                if (map[i][j] == FLOOR) {
                    chaseX = i;
                    chaseY = j;
                    map[i][j] = CHASER;
                    return;
                }
            }
        }
    }

    public void moveChaser() {
        pathToAvatar = findPath(new Point(chaseX, chaseY), new Point(avatarX, avatarY));
        if (pathToAvatar != null && !pathToAvatar.isEmpty()) {
            Point next = pathToAvatar.get(0);
            setChaserToNewPosition(next.x, next.y);
        }
    }

    public void setChaserToNewPosition(int x, int y) {
        map[chaseX][chaseY] = FLOOR;
        chaseX = x;
        chaseY = y;
        map[chaseX][chaseY] = CHASER;
    }

    public void togglePathDisplay() {
        isShowPath = !isShowPath;
    }


    public void moveAvatar(char direction) {
        int newX = avatarX;
        int newY = avatarY;
        switch (Character.toLowerCase(direction)) {
            case 'w' -> newY += 1;
            case 'a' -> newX -= 1;
            case 's' -> newY -= 1;
            case 'd' -> newX += 1;
        }
        if (newX >= 0 && newX <= WIDTH && newY >= 0 && newY <= HEIGHT && map[newX][newY] == FLOOR) {
            setAvatarToNewPosition(newX, newY);
        }

    }

    void setAvatarToNewPosition(int newX, int newY) {
        map[avatarX][avatarY] = FLOOR;
        avatarX = newX;
        avatarY = newY;
        map[avatarX][avatarY] = AVATAR;
    }

    private void initializeWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = UNUSED;
            }
        }
        this.buildWorld();
    }

    public void buildWorld() {
        generateRoom();
        Collections.sort(rooms);
        connectRoomsWithMST();
    }

    private void connectRoomsWithMST() {
        if (rooms.isEmpty()) {
            return;
        }
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getDistance));
        Set<Room> inMST = new HashSet<>();
        Room startRoom = rooms.getFirst();
        inMST.add(startRoom);

        for (Room room : rooms) {
            if (room != startRoom) {
                pq.add(new Edge(startRoom, room, startRoom.calculateDistance(room)));
            }
        }

        while ((inMST.size() < rooms.size())) {
            Edge minEdge = pq.poll();
            assert minEdge != null;
            if (inMST.contains(minEdge.getRoom1()) && inMST.contains(minEdge.getRoom2())) {
                continue;
            }
            Room newRoom = inMST.contains(minEdge.getRoom1()) ? minEdge.getRoom2() : minEdge.getRoom1();
            inMST.add(newRoom);

            connectRooms(minEdge.getRoom1(), minEdge.getRoom2());

            for (Room room : rooms) {
                if (!inMST.contains(room)) {
                    pq.add(new Edge(newRoom, room, newRoom.calculateDistance(room)));
                }
            }
        }
    }

    public void generateRoom() {
        int roomNums = random.nextInt(50) + 5;
        //room should be within the boundaries of the world grid.
        while (rooms.size() < roomNums) {
            int width = random.nextInt(10) + 5;
            int height = random.nextInt(7) + 4;
            int x = random.nextInt(WIDTH - width - 2) + 1;
            int y = random.nextInt(HEIGHT - height - 2) + 1;
            Room newRoom = new Room(width, height, x, y);
            Iterable<Point> points = newRoom.roomPoints();
            if (!isColliding(points)) {
                rooms.add(newRoom);
                markUsed(points);
                placeRoom(newRoom);
            }
        }
    }

    private ArrayList<Point> findPath(Point start, Point goal) {
        Map<Point, Double> gScore = new HashMap<>();
        PriorityQueue<Point> openSet = new PriorityQueue<>(Comparator.comparingDouble(p -> gScore.getOrDefault(p, Double.POSITIVE_INFINITY)));
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Point> comeFrom = new HashMap<>();
        openSet.add(start);
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            if (current.equals(goal)) {
                return constructPath(comeFrom, current);
            }

            closedSet.add(current);

            for (Point neighbour : getNeighbour(current)) {
                if (closedSet.contains(neighbour)) {
                    continue;
                }

                double tentativeGScore = gScore.get(current) + 1;

                if (!openSet.contains(neighbour)) {
                    openSet.add(neighbour);
                } else if (tentativeGScore >= gScore.get(neighbour)) {
                    continue;
                }

                gScore.put(neighbour, tentativeGScore);
                comeFrom.put(neighbour, current);
            }
        }
        return null;
    }

    private List<Point> getNeighbour(Point current) {
        List<Point> neighbour = new ArrayList<>();
        int x = current.x;
        int y = current.y;
        if (x > 0 && (map[x - 1][y] == FLOOR || map[x - 1][y] == AVATAR)) {
            neighbour.add(new Point(x - 1, y));
        }
        if (x < WIDTH - 1 && (map[x + 1][y] == FLOOR || map[x + 1][y] == AVATAR)) {
            neighbour.add(new Point(x + 1, y));
        }
        if (y > 0 && (map[x][y - 1] == FLOOR||map[x][y - 1] == AVATAR)) {
            neighbour.add(new Point(x, y - 1));
        }
        if (y < HEIGHT - 1 && (map[x][y + 1] == FLOOR || map[x][y + 1] == AVATAR)) {
            neighbour.add(new Point(x, y + 1));
        }
        return neighbour;
    }

    private ArrayList<Point> constructPath(Map<Point, Point> comeFrom, Point current) {
        ArrayList<Point> path = new ArrayList<>();
        path.add(current);
        while (comeFrom.containsKey(current)) {
            current = comeFrom.get(current);
            path.add(current);
        }
        path.removeLast();
        path.removeFirst();
        Collections.reverse(path);
        return path;
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

    //should check if place hallway is successful, if not. connect in another hallway.
    public void connectRooms(Room room1, Room room2) {
        Hallway hallway;
        if (room1.getPositionY() > room2.getPositionY()) {
            hallway = createHallway(room2, room1);

        } else {
            hallway = createHallway(room1, room2);
        }
        placeHallway(hallway);
        hallways.add(hallway);
    }


    private Hallway createHallway(Room room1, Room room2) {
        int x1 = room1.getPositionX() + room1.getWidth() / 2;
        int y1 = room1.getPositionY() + room1.getHeight() / 2;
        Hallway hallway = new Hallway();
        //vertical straight hallway
        if (x1 >= room2.getPositionX() + 2 && x1 <= room2.getPositionX() + room2.getWidth() - 2) {
            //room2 is above room1
            if (y1 < room2.getPositionY()) {
                hallway = new StraightHallway(x1, y1 + room1.getHeight() / 2, x1, room2.getPositionY());
            } else if (y1 > room2.getPositionY()) {
                hallway = new StraightHallway(x1, y1 - room1.getHeight() / 2, x1, room2.getPositionY() + room2.getHeight());
            }
            //horizontal straight hallway
        } else if (y1 >= room2.getPositionY() + 2 && y1 <= room2.getPositionY() + room2.getHeight() - 2) {
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
            if (room1.getPositionX() <= room2.getPositionX()) {
                //room2 is on the right of room1
                hallway = new TurnHallway(room1.getPositionX() + room1.getWidth() - 1, room1.getPositionY(), midX, midY, room2.getPositionX(), room2.getPositionY());

            } else {
                //room2 is on the left of room1
                hallway = new TurnHallway(room1.getPositionX(), room1.getPositionY(), midX, midY, room2.getPositionX(), room2.getPositionY());
            }
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
        int x1 = hallway.startX;
        int x2 = hallway.getMidX();
        int y1 = hallway.startY;
        int y2 = hallway.endY;
        if (x1 <= x2) {
            //room2 is on the right,above room1
            //floor
            drawLShape(x1, x2 + 1, y1 + 1, y2, FLOOR, false);
            //lower wall
            drawLShape(x1, x2 + 2, y1, y2, WALL, false);
            //upper wall
            drawLShape(x1, x2, y1 + 2, y2, WALL, false);
        } else {
            //room2 is on the left, above room1
            //floor
            drawLShape(x2 + 1, x1, y1 + 1, y2, FLOOR, true);
            //lower wall
            drawLShape(x2, x1, y1, y2, WALL, true);
            //upper wall
            drawLShape(x2 + 2, x1, y1 + 2, y2, WALL, true);
        }

    }

    private void drawLShape(int smallX, int bigX, int smallY, int bigY, TETile tileSet, boolean isBasedOnSmallX) {
        //draw the horizontal first
        int i, j;
        for (i = smallX; i <= bigX; i++) {
            if (tileSet == FLOOR) {
                map[i][smallY] = tileSet;
            } else if (map[i][smallY] != FLOOR) {
                map[i][smallY] = tileSet;
            }
        }
        //then the vertical part
        for (j = smallY; j <= bigY; j++) {
            int x = smallX;
            if (!isBasedOnSmallX) {
                x = bigX;
            }
            if (tileSet == FLOOR) {
                map[x][j] = tileSet;
            } else if (map[x][j] != FLOOR) {
                map[x][j] = tileSet;
            }
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
            int startX = hallway.startX;
            int endX = hallway.endX;
            if (startX > endX) {
                int temp = startX;
                startX = endX;
                endX = temp;
            }
            for (int i = startX; i <= endX; i++) {
                for (int j = hallway.startY; j <= hallway.startY + 2; j += 2) {
                    if (map[i][j] != FLOOR) {
                        map[i][j] = WALL;
                    }
                }
            }
            for (int i = startX; i <= endX; i++) {
                map[i][hallway.startY + 1] = FLOOR;
            }
        }
    }

    public TETile[][] getMap() {
        return map;
    }

    public int getAvatarX() {
        return avatarX;
    }


    public int getAvatarY() {
        return avatarY;
    }


    public long getSeed() {
        return seed;
    }

    public boolean isShowPath() {
        return isShowPath;
    }

    public List<Point> getPathToAvatar() {
        return pathToAvatar;
    }

    public int getChaseX() {
        return chaseX;
    }

    public int getChaseY() {
        return chaseY;
    }
}
