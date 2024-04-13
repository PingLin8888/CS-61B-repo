package main;

import edu.princeton.cs.algs4.In;

import java.util.*;
import java.util.stream.Collectors;

public class WordNet{
    MyGraph wordNetData;
    final Map<Integer,String[]> indexToWord;

    public WordNet(String synsetsFileName, String hyponymsFileName) {
        indexToWord = new HashMap<>();
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
            Hyponyms hyponyms = wordNetData.getHyponymsByFileIndex(indexOfHypernym);
            if (hyponyms != null) {
                for (int i = 0; i < splitLine.length - 1; i++) {
                    hyponyms.addHyponym(Integer.parseInt(splitLine[i + 1]));
                }
                wordNetData.addNode(indexOfHypernym, hyponyms);
            }
        }
    }


    public String getHyponyms(String hypernym) {
        ArrayList<Integer> indexOfHypernyms = new ArrayList<>();
        Set<Integer> indexOfResult = new HashSet<>();
        Set<String> result = new TreeSet<>();

        /*Search the word in the synsets. Find one, add the index to the indexOfHypernyms array. Could be more than one.*/
        for (Map.Entry<Integer, String[]> entry : indexToWord.entrySet()) {
            int key = entry.getKey();
            String[] words = entry.getValue();
            for (int i = 0; i < words.length; i++) {
                if (hypernym.equals(words[i])) {
                    indexOfHypernyms.add(key);
                }
            }
        }

        /*Search the index in the wordnet to find hyponyms.*/
        for (int index : indexOfHypernyms) {
            Hyponyms indexOfHyponyms = wordNetData.getHyponymsByFileIndex(index);
            if (indexOfHyponyms != null) {
                for (int i = 0; i < indexOfHyponyms.getSize(); i++) {
                    indexOfResult.add(indexOfHyponyms.getIndex(i));
                }
            }
            }

        for (int index : indexOfResult) {
            String[] hyponyms = indexToWord.get(index);
            for (String hyponym : hyponyms) {
                result.add(hyponym);
            }
        }

        return setToString(result);
    }

/*Use stream to get hyponyms.*/
    /*public String getHyponyms(String hypernym) {
        Set<String> result = indexToWord.entrySet().stream()
                .filter(entry -> Arrays.asList(entry.getValue()).contains(hypernym))
                .map(Map.Entry::getKey)
                .flatMap(index -> wordNetData.getHyponymsByFileIndex(index).getListOfHyponyms().stream()
                        .map(hyponymIndex -> Arrays.asList(indexToWord.get(hyponymIndex))))
                .flatMap(Collection::stream)
                .map(String::toString)
                .collect(Collectors.toCollection(TreeSet::new));

        return String.join(", ", result);
    }*/

    private String setToString(Set<String> result) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String s : result) {
            sb.append(s).append(", ");
        }
        if (!result.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }

}
