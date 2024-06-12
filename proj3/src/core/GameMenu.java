package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import utils.FileUtils;

import java.io.File;


/**
 * Created by gpt
 */
public class GameMenu {
    private static World world;
    private static TERenderer ter;
    private static StringBuilder seedBuilder = new StringBuilder();
    private static StringBuilder quitSignBuilder = new StringBuilder();
    private static boolean enteringSeed = false;
    private static boolean gameStarted = false;
    private static boolean redraw = true;
    private String HUD;


    public void createGameMenu() {
        StdDraw.setCanvasSize(800, 600);
        ter = new TERenderer();


        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            if (!enteringSeed && !gameStarted) {
                drawMenu();
//                StdDraw.show();

            } else if (enteringSeed) {
                drawSeedEntry();
//                StdDraw.show();
            } else {
//                ter.renderFrame(world.getMap());
                drawHUD();
            }
            handleInput();
            StdDraw.pause(100);
        }
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
//                        StdDraw.show();
                        break;
                    case 'l':
                        loadGame();
//                        StdDraw.show();
                        break;
                    case 'q':
                        System.exit(0);
//                        StdDraw.show();
                        break;
                }
            } else if (enteringSeed) {
                if (Character.isDigit(key)) {
                    seedBuilder.append(key);
//                    StdDraw.show();
                } else if (key == 's') {
                    finalizeSeed();
//                    StdDraw.show();
                }
            } else {
                if (key == ':') {
                    quitSignBuilder.setLength(0);
                    quitSignBuilder.append(key);
                } else if (key == 'q' && quitSignBuilder.toString().equals(":")) {
                    saveGame();
                    System.exit(0);
                }
                handleMovement(key);
//                StdDraw.show();
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
//            StdDraw.show();
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
            String contents = world.getSeed() + "\n" + world.getAvatarX() + "\n" + world.getAvatarY() + "\n";
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
            drawWorld();
            gameStarted = true;
            // Print the path where the file is read from
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void drawHUD() {
        String description;
        int mouseX = (int) Math.floor(StdDraw.mouseX());
        int mouseY = (int) Math.floor(StdDraw.mouseY());

        if (mouseX >= 0 && mouseX < world.getMap().length && mouseY >= 0 && mouseY < world.getMap()[0].length) {
            TETile tile = world.getMap()[mouseX][mouseY];
            description = tile.description();
        } else {
            description = "out side of map";
        }
        if (!description.equals(HUD)) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(0.01, 0.99, description);
//            StdDraw.show();
        }

    }
}


