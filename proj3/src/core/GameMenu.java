package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import utils.FileUtils;

import java.awt.*;


/**
 * Inspired by GPT.
 */
public class GameMenu {
    private static World world;
    private static TERenderer ter;
    private static StringBuilder seedBuilder = new StringBuilder();
    private static StringBuilder quitSignBuilder = new StringBuilder();
    private static boolean enteringSeed = false;
    private static boolean gameStarted = false;
    private static boolean redraw = true;
    private double prevMouseX = 0;
    private double prevMouseY = 0;
    private long lastChaserMoveTime = 0; // Variable to track the last time the chaser moved
    private static final long CHASER_MOVE_INTERVAL = 500; // Interval in milliseconds between chaser movements


    public void createGameMenu() {
        StdDraw.setCanvasSize(800, 600);
        ter = new TERenderer();
        while (true) {
            if (redraw) {
                StdDraw.clear(StdDraw.BLACK);
                if (!enteringSeed && !gameStarted) {
                    drawMenu();
                } else if (enteringSeed) {
                    drawSeedEntry();
                } else {
                    ter.renderFrame(world.getMap());
                    updateHUD();
                    if (world.isShowPath() && world.getPathToAvatar() != null) {
                        drawPath();
                    }
                }
                StdDraw.show();
                redraw = false;
            }
            handleInput();
            detectMouseMove();
            StdDraw.pause(20);

            if (gameStarted) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastChaserMoveTime >= CHASER_MOVE_INTERVAL) {
                    world.moveChaser();
                    lastChaserMoveTime = currentTime; // Update the last move time
                    redraw = true;
                }
            }
        }
    }

    private void drawPath() {
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        for (Point p : world.getPathToAvatar()) {
            StdDraw.filledSquare(p.x + 0.5, p.y + 0.5, 0.5);
        }
    }

    private void detectMouseMove() {
        double currentMouseX = StdDraw.mouseX();
        double currentMouseY = StdDraw.mouseY();

        if (hasMouseMoved(currentMouseX, currentMouseY)) {

            // Update previous mouse position
            prevMouseX = currentMouseX;
            prevMouseY = currentMouseY;
            redraw = true;
        }
    }

    private boolean hasMouseMoved(double currentMouseX, double currentMouseY) {
        return currentMouseX != prevMouseX || currentMouseY != prevMouseY;
    }

    private void updateHUD() {
        // Example HUD update method
        String description;
        int mouseX = (int) Math.floor(prevMouseX);
        int mouseY = (int) Math.floor(prevMouseY);

        if (mouseX >= 0 && mouseX < world.getMap().length && mouseY >= 0 && mouseY < world.getMap()[0].length) {
            TETile tile = world.getMap()[mouseX][mouseY];
            description = tile.description();
        } else {
            description = "out side of map";
        }
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(0.01, 0.99, description);
    }


    private static void drawMenu() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.75, "CS61B: THE GAME");
        StdDraw.text(0.5, 0.5, "New Game (N)");
        StdDraw.text(0.5, 0.45, "Load Game (L)");
        StdDraw.text(0.5, 0.4, "Quit (Q)");
    }

    private static void drawSeedEntry() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.5, "Enter seed: " + seedBuilder.toString());
    }

    private static void handleInput() {
        if (StdDraw.hasNextKeyTyped()) {
            char key = Character.toLowerCase(StdDraw.nextKeyTyped());
            redraw = true; // Set redraw flag to true for any key press
            if (!enteringSeed && !gameStarted) {
                switch (key) {
                    case 'n':
                        enteringSeed = true;
                        seedBuilder.setLength(0);
                        break;
                    case 'l':
                        loadGame();
                        break;
                    case 'q':
                        System.exit(0);
                        break;
                }
            } else if (enteringSeed) {
                if (Character.isDigit(key)) {
                    seedBuilder.append(key);
                } else if (key == 's') {
                    finalizeSeed();
                }
            } else {
                if (key == ':') {
                    quitSignBuilder.setLength(0);
                    quitSignBuilder.append(key);
                } else if (key == 'q' && quitSignBuilder.toString().equals(":")) {
                    saveGame();
                    System.exit(0);
                }
                if (key == 'p') {
                    world.togglePathDisplay();
                }
                handleMovement(key);
            }
        }
    }

    private static void finalizeSeed() {
        if (!seedBuilder.isEmpty()) {
            enteringSeed = false;
            gameStarted = true;
            long seed = Long.parseLong(seedBuilder.toString());
            world = new World(seed);
            System.out.println("starting to draw world.");
            drawWorld();

            redraw = true; // Set redraw flag to true after seed entry
        } else {
            StdDraw.text(0.5, 0.5, "Seed cannot be empty. Please enter a valid seed.");
        }
    }

    private static void drawWorld() {
//        StdDraw.clear();
        int width = world.getMap().length;
        int height = world.getMap()[0].length;
        ter.initialize(width, height);
        ter.renderFrame(world.getMap());
    }


    private static void handleMovement(char key) {
        switch (Character.toLowerCase(key)) {
            case 'w':
            case 'a':
            case 's':
            case 'd':
                world.moveAvatar(key);
                break;
        }
    }

    public static void saveGame() {
        String fileName = "save-file.txt";
        try {
            // Serialize seed, avatarX, and avatarY directly
            String contents = world.getSeed() + "\n" + world.getAvatarX() + "\n" + world.getAvatarY() + "\n"+ world.getChaseX() + "\n" + world.getChaseY() + "\n";
            // Write contents to file using FileUtils
            FileUtils.writeFile(fileName, contents);

            // Print the path where the file is saved
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static void loadGame() {
        String fileName = "save-file.txt";
        try {
            // Read contents from file using FileUtils
            String contents = FileUtils.readFile(fileName);

            // Parse seed, avatarX, and avatarY
            String[] lines = contents.split("\n");
            world = new World(Long.parseLong(lines[0]));
            world.setAvatarToNewPosition(Integer.parseInt(lines[1]), Integer.parseInt(lines[2]));
            world.setChaserToNewPosition(Integer.parseInt(lines[3]), Integer.parseInt(lines[4]));
            drawWorld();
            gameStarted = true;
            // Print the path where the file is read from
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}


