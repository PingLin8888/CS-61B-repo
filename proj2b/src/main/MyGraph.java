package main;

import java.util.*;

public class MyGraph {
    /*Array of Hyponyms. Index of this array is index of the hypernym.
    The array stores Hyponyms, which is lists of index of hyponyms*/
    private Hyponyms[] indexOfWordNet;
    private int size;

    public MyGraph() {
        size = 0;
        indexOfWordNet = new Hyponyms[5];
        for (int i = 0; i < indexOfWordNet.length; i++) {
            indexOfWordNet[i] = new Hyponyms();
        }
    }

    public int getSize() {
        return size;
    }

    public void addNode(int indexOfFile, Hyponyms listOfhyponyms) {
        if (indexOfFile >= indexOfWordNet.length) {
            Hyponyms[] newTemp = new Hyponyms[Math.max(indexOfWordNet.length * 2, indexOfFile + 1)];
            System.arraycopy(indexOfWordNet, 0, newTemp, 0, indexOfWordNet.length);
            indexOfWordNet = newTemp;
        }
        if (indexOfWordNet[indexOfFile] == null) {
            indexOfWordNet[indexOfFile] = new Hyponyms();
        }

        indexOfWordNet[indexOfFile].addAll(listOfhyponyms.getListOfHyponyms());
        size++;
    }

    /*Avoid "Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 1000
"*/
    public Hyponyms getHyponymsByFileIndex(int indexFromFile) {
        if (indexFromFile > 0 && indexFromFile <= size) {
            return indexOfWordNet[indexFromFile];
        }
        return null;
    }

    /*traversal */
    public Set<Integer> traversal(Integer hypernym, int sizeOfSynsets) {
        Set<Integer> hyponyms = new TreeSet<>();
        boolean[] marked = new boolean[sizeOfSynsets];
        Queue<Integer> fringe = new ArrayDeque<>();
        fringe.add(hypernym);
        marked[hypernym] = true;
        while (!fringe.isEmpty()) {
            int v = fringe.remove();
            if (indexOfWordNet[v] != null) {
                for (int w : indexOfWordNet[v].getListOfHyponyms()) {
                    if (!marked[w]) {
                        marked[w] = true;
                        fringe.add(w);
                        hyponyms.add(w);
                    }
                }
            }
        }
        return hyponyms;
    }
}
