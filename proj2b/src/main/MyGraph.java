package main;

import java.util.ArrayList;
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
        if (indexOfFile >= size) {
            Hyponyms[] newTemp = new Hyponyms[2 * size];
            newTemp = Arrays.copyOf(indexOfWordNet, 2 * size);
            indexOfWordNet = newTemp;
        }
        indexOfWordNet[indexOfFile - 1] = listOfhyponyms;
        size++;
    }


    public Hyponyms getHyponymsByIndex(int indexFormFile) {
        return indexOfWordNet[indexFormFile - 1];
    }

    public void updateHyponyms(int indexOfHypernym, List<Integer> hyponyms) {
    }


    public class Hyponyms {
        private List<Integer> listOfHyponyms;

        public Hyponyms() {
            listOfHyponyms = new ArrayList<>();
        }

        public Hyponyms(List<Integer> hyponyms) {
            this.listOfHyponyms = hyponyms;
        }

        public void addHyponym(int index) {
            listOfHyponyms.add(index);
        }

    }
}
