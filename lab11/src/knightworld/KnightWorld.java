package knightworld;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

/**
 * Draws a world consisting of knight-move holes.
 */
public class KnightWorld {

    private TETile[][] tiles;

    // TODO: Add additional instance variables here
    private int width;
    private int height;
    private int holeSize;

    /**
     * Constructs a KnightWorld with specified dimensions and hole size.
     *
     * @param width the width of the world
     * @param height the height of the world
     * @param holeSize the size of each hole
     */
    public KnightWorld(int width, int height, int holeSize) {
        // TODO: Fill in this constructor and class, adding helper methods and/or classes as necessary to draw the
        //  specified pattern of the given hole size for a window of size width x height. If you're stuck on how to
        //  begin, look at the provided demo code!
        this.width = width;
        this.height = height;
        this.holeSize = holeSize;
        tiles = new TETile[width][height];
        fillTheFloor(Tileset.UNLOCKED_DOOR);
        drawKnightWorld(holeSize, Tileset.solidBox);
    }

    /**
     * Draws the knight world with a specified hole size.
     *
     * @param holeSize the size of each hole
     * @param set the tile type to use for the holes
     */
    private void drawKnightWorld(int holeSize, TETile set) {
        for (int i = 0; i < width; i += holeSize * 5) {
            for (int j = 0; j < height; j += holeSize * 5) {
                drawKnightHole(i + 1 * holeSize, j, holeSize, set);
                drawKnightHole(i + 4 * holeSize, j + 1 * holeSize, holeSize, set);
                drawKnightHole(i + 2 * holeSize, j + 2 * holeSize, holeSize, set);
                drawKnightHole(i, j + 3 * holeSize, holeSize, set);
                drawKnightHole(i + 3 * holeSize, j + 4 * holeSize, holeSize, set);
            }
        }

    }

    /**
     * Draws a hole at the specified position.
     *
     * @param x the x-coordinate of the hole's starting position
     * @param y the y-coordinate of the hole's starting position
     * @param holeSize the size of the hole
     * @param set the tile type to use for the hole
     */
    private void drawKnightHole(int x, int y, int holeSize, TETile set) {
        for (int i = x; i < Math.min(x + holeSize, width); i++) {
            for (int j = y; j < Math.min(y + holeSize, height); j++) {
                tiles[i][j] = set;
            }
        }
    }

    /**
     * Fills the entire floor with the specified tile.
     *
     * @param set the tile type to use for the floor
     */
    private void fillTheFloor(TETile set) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = set;
            }
        }
    }


    /**
     * Returns the tiles associated with this KnightWorld.
     */
    public TETile[][] getTiles() {
        return tiles;
    }

    public static void main(String[] args) {
        // Change these parameters as necessary
        int width = 50;
        int height = 30;
        int holeSize = 3;

        KnightWorld knightWorld = new KnightWorld(width, height, holeSize);

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        ter.renderFrame(knightWorld.getTiles());

    }
}
