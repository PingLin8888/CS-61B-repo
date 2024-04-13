package main;

import browser.NgordnetServer;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();
        
        /* The following code might be useful to you.

        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        */
//        String synsetsFile = "./data/wordnet/synsets11.txt";
//        String hyponymsFile = "./data/wordnet/hyponyms11.txt";
        String synsetsFile = "./data/wordnet/synsets16.txt";
        String hyponymsFile = "./data/wordnet/hyponyms16.txt";
        WordNet wordNet = new WordNet(synsetsFile, hyponymsFile);

        hns.startUp();
        hns.register("history", new DummyHistoryHandler());
        hns.register("historytext", new DummyHistoryTextHandler());
        hns.register("hyponyms", new HyponymsHandler(wordNet));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
