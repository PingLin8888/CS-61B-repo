package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;


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
                }
                StdDraw.show();
                redraw = false;
            }
            handleInput();
            StdDraw.pause(20);
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
                    world.saveGame();
                    System.exit(0);
                }
                handleMovement(key);
                redraw = true;
            }
        }
    }

    private static void finalizeSeed() {
        if (seedBuilder.length() > 0) {
            long seed = Long.parseLong(seedBuilder.toString());
            world = new World(seed);
            int width = world.getMap().length;
            int height = world.getMap()[0].length;
            ter.initialize(width, height);

            enteringSeed = false;
            gameStarted = true;
            StdDraw.clear();
            ter.renderFrame(world.getMap());
            redraw = true; // Set redraw flag to true after seed entry
        } else {
            StdDraw.text(0.5, 0.5, "Seed cannot be empty. Please enter a valid seed.");
            StdDraw.show();
        }
    }

    private static void loadGame() {
        world = new World();
        world.loadGame();
        int width = world.getMap().length;
        int height = world.getMap()[0].length;
        ter.initialize(width, height);
        gameStarted = true;
        ter.renderFrame(world.getMap());
        redraw = true; // Set redraw flag to true after loading game
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
}


