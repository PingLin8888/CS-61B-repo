import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    // TODO: Add any necessary instance variables.
    private boolean[][] sites;
    private WeightedQuickUnionUF disjointSetsPercolate;
    private WeightedQuickUnionUF disjointSetsFull;
    private int virtualTopSitePercolate;
    private int virtualDownSitePercolate;
    private int virtualTopSiteFull;
    private int numOfOpen;


    public Percolation(int N) {
        // TODO: Fill in this constructor.
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        numOfOpen = 0;
        disjointSetsPercolate = new WeightedQuickUnionUF(N * N + 2);
        disjointSetsFull = new WeightedQuickUnionUF(N * N + 1);
        virtualTopSitePercolate = N * N;
        virtualDownSitePercolate = N * N + 1;
        virtualTopSiteFull = N * N;
        this.sites = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sites[i][j] = false;
            }
        }
    }

    public void open(int row, int col) {
        // TODO: Fill in this method.
        checkArguments(row, col);
        sites[row][col] = true;
        numOfOpen++;
        unionNeighbor(disjointSetsPercolate, row, col);
        unionNeighbor(disjointSetsFull, row, col);
        unionTopDown(disjointSetsPercolate, row, col);
        unionTop(disjointSetsFull, row, col);
    }

    private void checkArguments(int row, int col) {
        if (row < 0 || row > sites.length - 1 || col < 0 || col > sites.length - 1) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void unionTop(WeightedQuickUnionUF set, int row, int col) {
        if (row == 0) {
            set.union(virtualTopSiteFull, xyTo1D(row, col));
        }
    }

    private void unionTopDown(WeightedQuickUnionUF set, int row, int col) {
        if (row == 0) {
            set.union(virtualTopSitePercolate, xyTo1D(row, col));
        }
        if (row == sites.length - 1) {
            set.union(virtualDownSitePercolate, xyTo1D(row, col));
        }
    }


    private void unionNeighbor(WeightedQuickUnionUF set, int row, int col) {
        int index = xyTo1D(row, col);
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            set.union(index, xyTo1D(row, col - 1));
        }
        if (col + 1 < sites.length && isOpen(row, col + 1)) {
            set.union(index, xyTo1D(row, col + 1));
        }
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            set.union(index, xyTo1D(row - 1, col));
        }
        if (row + 1 < sites.length && isOpen(row + 1, col)) {
            set.union(index, xyTo1D(row + 1, col));
        }
    }


    private int xyTo1D(int row, int col) {
        return row * sites.length + col;
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        checkArguments(row, col);
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        checkArguments(row, col);
        return disjointSetsFull.connected(xyTo1D(row, col), virtualTopSiteFull);
//        return disjointSetsPercolate.connected(xyTo1D(row, col), virtualTopSitePercolate);
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return numOfOpen;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return disjointSetsPercolate.connected(virtualTopSitePercolate, virtualDownSitePercolate);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

}
