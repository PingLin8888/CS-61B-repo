package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        WordNet wordNet = new WordNet(synsetFile, hyponymFile, wordFile, countFile);
        HyponymsHandler hyponymsHandler = new HyponymsHandler(wordNet);
        return hyponymsHandler;
//        throw new RuntimeException("Please fill out AutograderBuddy.java!");
    }
}
