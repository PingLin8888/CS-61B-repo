package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    MyGraph wordNetData;
    final Map<Integer, Set<String>> indexToWord;

    public WordNet(String synsetsFileName, String hyponymsFileName) {
        indexToWord = new HashMap<>();
        wordNetData = new MyGraph();
        In file = new In(synsetsFileName);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splitLine = line.split(",");
            int index = Integer.parseInt(splitLine[0]);
            String[] words = splitLine[1].split(" ");
            Set<String> wordSet = new HashSet<>(Arrays.asList(words));
            indexToWord.put(index, wordSet);
        }
        file = new In(hyponymsFileName);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splitLine = line.split(",");
            int indexOfHypernym = Integer.parseInt(splitLine[0]);
            Hyponyms hyponyms = new Hyponyms();
            for (int i = 1; i < splitLine.length; i++) {
                hyponyms.addHyponym(Integer.parseInt(splitLine[i]));
            }
            wordNetData.addNode(indexOfHypernym, hyponyms);
        }
    }

    public String getHyponyms(List<String> words, int startYear, int endYear, int k) {
        Set<String> intersection = new TreeSet<>(getHyponymsOfOneWord(words.get(0)));
        for (String word : words) {
            Set<String> hyponymsOfOneWord = getHyponymsOfOneWord(word);
            intersection.retainAll(hyponymsOfOneWord);
        }
        return setToString(intersection);
    }

    private Set<String> getHyponymsOfOneWord(String word) {
        Set<Integer> indexOfHypernyms = new TreeSet<>();
        indexOfHypernyms.addAll(getIndexOfHypernyms(word));
        Set<Integer> indexOfHyponyms = new HashSet<>();
        indexOfHyponyms.addAll(indexOfHypernyms);
        Set<String> result = new TreeSet<>();
        int size = indexToWord.size();

        /*        Search the index in the wordnet to find hyponyms.
         */
        if (indexOfHypernyms != null) {
            for (int index : indexOfHypernyms) {
                indexOfHyponyms.addAll(wordNetData.traversal(index, size));
            }
        }
        for (int index : indexOfHyponyms) {
            if (indexToWord.containsKey(index)) {
                result.addAll(indexToWord.get(index));
            }
        }
        return result;
    }

    /*
            Search the word in the synsets. Find one, add the index to the indexOfHypernyms array. Could be more than one.
    */
    private Set<Integer> getIndexOfHypernyms(String hypernym) {
        Set<Integer> indexOfHypernyms = new HashSet<>();
        for (Map.Entry<Integer, Set<String>> entry : indexToWord.entrySet()) {
            Set<String> words = entry.getValue();
            if (words.contains(hypernym)) {
                indexOfHypernyms.add(entry.getKey());
            }
        }
        return indexOfHypernyms;
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
