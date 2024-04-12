package main;

import edu.princeton.cs.algs4.In;

import java.sql.Array;
import java.util.*;

public class WordNet{
    MyGraph wordNetData;
    Map<Integer,String[]> indexToWord;

    public WordNet() {
        indexToWord = new HashMap<>();
    }

    public WordNet(String synsetsFileName, String hyponymsFileName) {
        wordNetData = new MyGraph();
        In file = new In(synsetsFileName);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splitLine = line.split(",");
            int index = Integer.parseInt(splitLine[0]);
            String[] words = splitLine[1].split(" ");
            indexToWord.put(index, words);
        }
        file = new In(hyponymsFileName);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splitLine = line.split(",");
            int indexOfHypernym = Integer.parseInt(splitLine[0]);
            MyGraph.Hyponyms hyponyms = wordNetData.getHyponymsByIndex(indexOfHypernym);
            for (int i = 0; i < splitLine.length - 1; i++) {
                hyponyms.addHyponym(Integer.parseInt(splitLine[i + 1]));
            }
            wordNetData.addNode(indexOfHypernym, hyponyms);
        }
    }


    public String getHyponyms(String hypernym) {
        ArrayList<Integer> indexOfHyponyms = new ArrayList<>();
        Collection<String[]> synsets = indexToWord.values();
        for (int i = 0; i < synsets.size(); i++) {
            if (hypernym.equals(synsets[i])) {
                indexOfHyponyms.add(i);
            }
        }
        for (int index : indexOfHyponyms) {
            ArrayList<Integer> hyponyms = wordNetData.getHyponymsByIndex(index);

        }
        for (int hyponym : hyponyms) {

        }

        return null;
    }
}
