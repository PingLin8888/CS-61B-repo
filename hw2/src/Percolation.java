import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;


public class Percolation {
    // TODO: Add any necessary instance variables.
    private boolean[][] sites;
    private WeightedQuickUnionUF wqu;
    final private int virtualTopSite;
    final private int virtualDownSite;
    private int numOfOpen;


    public Percolation(int N) {
        // TODO: Fill in this constructor.
        wqu = new WeightedQuickUnionUF(N * N + 2);
        this.sites = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sites[i][j] = false;
            }
        }
        virtualTopSite = N * N + 1;
        virtualDownSite = N * N + 2;
    }


    public void open(int row, int col) {
        // TODO: Fill in this method.
        sites[row][col] = true;
        numOfOpen++;
        unionNeighbor(row, col);
        if (row == 0) {
            wqu.union(virtualTopSite, xyTo1D(row, col));
        }
        if (row == sites.length - 1) {
            wqu.union(virtualDownSite, xyTo1D(row, col));
        }
    }


    private void unionNeighbor(int row, int col) {
        int index = xyTo1D(row, col);
        if (col - 1 > 0 && isOpen(row, col - 1)) {
            wqu.union(index, xyTo1D(row, col - 1));
        }
        if (col + 1 < sites.length && isOpen(row, col + 1)) {
            wqu.union(index, xyTo1D(row, col + 1));
        }
        if (row - 1 > 0 && isOpen(row - 1, col)) {
            wqu.union(index, xyTo1D(row - 1, col));
        }
        if (row + 1 < sites.length && isOpen(row + 1, col)) {
            wqu.union(index, xyTo1D(row + 1, col));
        }
    }


    private int xyTo1D(int row, int col) {
        return row * col - 1;
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        return wqu.connected(xyTo1D(row, col), virtualTopSite);
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return numOfOpen;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return wqu.connected(virtualTopSite, virtualDownSite);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

}
