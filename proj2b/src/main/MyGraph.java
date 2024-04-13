package main;

import java.util.Arrays;
import java.util.List;

public class MyGraph{
    /*Array of Hyponyms. Index of this array is index of the hypernym.
    The array stores Hyponyms, which is lists of index of hyponyms*/
    private Hyponyms[] indexOfWordNet;
    private int size;

    public MyGraph() {
        size = 0;
        indexOfWordNet = new Hyponyms[1000];
    }

    public int getSize() {
        return size;
    }

    public void addNode(int indexOfFile, Hyponyms listOfhyponyms) {
//        if (indexOfFile >= indexOfWordNet.length) {
//            indexOfWordNet.ensureCapacity
//        }

        if (indexOfFile >= size) {
            Hyponyms[] newTemp = new Hyponyms[2 * size];
            newTemp = Arrays.copyOf(indexOfWordNet, 2 * size);
            indexOfWordNet = newTemp;
        }
        if (indexOfWordNet[indexOfFile - 1] == null) {
            indexOfWordNet[indexOfFile - 1] = new Hyponyms();
        }
        indexOfWordNet[indexOfFile - 1].addAll(listOfhyponyms.getListOfHyponyms());
        size++;
    }

    /*Avoid "Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 1000
"*/
    public Hyponyms getHyponymsByFileIndex(int indexFromFile) {
        if (indexFromFile > 0 && indexFromFile <= size) {
            return indexOfWordNet[indexFromFile - 1];
        }
        return null;
    }

    public void updateHyponyms(int indexOfHypernym, List<Integer> hyponyms) {
    }
}
