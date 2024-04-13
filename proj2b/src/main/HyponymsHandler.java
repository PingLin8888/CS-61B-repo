package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wordNet;

    public HyponymsHandler(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        String word = words.get(0);
        return wordNet.getHyponyms(word);
    }
}
