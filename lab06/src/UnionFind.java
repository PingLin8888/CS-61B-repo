import java.util.ArrayList;

public class UnionFind {
    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     * You can assume that we are only working with non-negative integers as the items
     * in our disjoint sets.
     */
    private int[] data;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        data = new int[N];
        if (N < 0) {
            throw new IllegalArgumentException("The item must be non-negative.");
        }
        for (int i = 0; i < N; i++) {
            data[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0) {
            throw new IllegalArgumentException("The item must be non-negative.");
        }
        if (data[v] == -1) {
            return 1;
        }
        return getSizeOf(v);
    }

    private int getSizeOf(int v) {
        if (data[v] < -1) {
            return -data[v];
        }
        return getSizeOf(data[v]);
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0) {
            throw new IllegalArgumentException("The item must be non-negative.");
        }
        return data[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2) {
            return true;
        }
        return false;
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0 || v > data.length) {
            throw new IllegalArgumentException("The item must be non-negative.");
        }
        ArrayList<Integer> compressionSet = new ArrayList<>();
        int root = findRoot(v, compressionSet);
        for (int i = 0; i < compressionSet.size(); i++) {
            data[v] = root;
        }
        return root;
    }

    private int findRoot(int v,ArrayList compressionSet) {
        if (data[v] <= -1) {
            return v;
        }
        compressionSet.add(v);
        return findRoot(data[v],compressionSet);
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if (v1 == v2) {
            return;
        }
        int sizeV1 = sizeOf(v1);
        int sizeV2 = sizeOf(v2);
        int root1 = find(v1);
        int root2 = find(v2);
        if (sizeV1 <= sizeV2) {
            data[root1] = root2;
            data[root2] = -(sizeV1 + sizeV2);
        } else {
            data[root2] = root1;
            data[root1] = -(sizeV1 + sizeV2);
        }
    }

    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     */
    public int[] returnData() {
        return data;
    }
}
