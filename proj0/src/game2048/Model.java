package game2048;

import java.util.Formatter;

import static org.apache.commons.lang3.Validate.validIndex;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    private final Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore) {
        board = new Board(rawValues);
        this.score = score;
        this.maxScore = maxScore;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists(board) || !atLeastOneMoveExists(board);
    }

    /** Checks if the game is over and sets the maxScore variable
     *  appropriately.
     */
    private void checkGameOver() {
        if (gameOver()) {
            maxScore = Math.max(score, maxScore);
        }
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        boolean maxTileExists = false;
        int board_size = b.size();
        for (int i = 0; i < board_size; i += 1) {
            for (int j = 0; j < board_size; j += 1) {
                if (b.tile(i, j) != null) {
                    if (b.tile(i, j).value() == MAX_PIECE) {
                        maxTileExists = true;
                    }
                }
            }
        }
        return maxTileExists;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (emptySpaceExists(b)) {
                    return true;
                }
                if (sameValueAdjacentTilesExist(b, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*check if adjacent tiles with the same values exist*/
    private static boolean sameValueAdjacentTilesExist(Board board, int i, int j) {
        int[] adjacentX = {i - 1, i + 1, i, i}; // Left, Right, Up, Down
        int[] adjacentY = {j, j, j - 1, j + 1}; // Left, Right, Up, Down
        // Display adjacent dot locations
        for (int m = 0; m < adjacentX.length; m++) {
            int compareIndexX = adjacentX[i];
            int compareIndexY = adjacentY[i];
            if (validIndex(board, compareIndexX, compareIndexY)) {
                if (board.tile(i, j).value() == board.tile(compareIndexX, compareIndexY).value()) {
                    return true;
                }
            }
        }
        return false;
    }

    /*check if index is valid*/
    private static boolean validIndex(Board board, int compareIndexX, int compareIndexY) {
        return (compareIndexX >= 0) && (compareIndexY >= 0) && (compareIndexX < board.size()) && (compareIndexY < board.size());
    }


    /** Tilt the board toward SIDE.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public void tilt(Side side) {
        // TODO: Modify this.board (and if applicable, this.score) to account
        // for the tilt to the Side SIDE.
        int size = this.board.size();
        for (int j = 0; j < size; j += 1) {
            moveSingleColumn(side,j);
        }


        checkGameOver();
    }

    private void moveSingleColumn(Side side, int j) {
        int size = this.board.size();
        for (int i = size - 2; i >= 0; i -= 1) {
            if (board.tile(j,i) != null) {
                Tile t = board.tile(j, i);
//                System.out.println("i's value is: " + i);
//                System.out.println("tile value is: "+t.value());
                int[] location = getDestinationTile(board, j, i);
                board.move(location[0], location[1], t);
            }
        }
    }

    private int[] getDestinationTile(Board board, int j, int i) {
        int[] location = new int[2];
        location[0] = j;
        location[1] = i;
        int row = i;
        int mergeTime = 0;
        while (row < board.size() && validIndex(board, j, row + 1)) {
            //check if upper tile is null
//            System.out.println("checking row's  value before upper null: "+row);
            if (board.tile(j, row + 1) == null ) {
                location[0] = j;
                location[1] = row + 1;
                row += 1;
            } else if (checkIfMerge(row, j, i, mergeTime)) {
                //upper tile is the same value
                location[0] = j;
                location[1] = row + 1;
                row += 1;
                mergeTime += 1;
                score += 2 * board.tile(j, location[1]).value();
//                System.out.println("checking row's value after merge: " + row);
            } else {
//                System.out.println("checking row's value after else: " + row);
                break;
            }
        }
        return location;
    }

    private boolean checkIfMerge(int row,int j, int i, int mergeTime) {
        boolean checkIfNotNull = board.tile(j, row + 1)!= null;
        boolean checkIfSameValue = board.tile(j, row + 1).value() == board.tile(j, i).value() && mergeTime < 1;
        return checkIfNotNull && checkIfSameValue;
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

