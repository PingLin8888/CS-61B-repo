package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import java.util.ArrayList;
import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap data;
    public HistoryTextHandler(NGramMap map) {
        data = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";
        if (words.size() > 0) {
            for (String word : words) {
                response += word;
                response += ": ";
                response += data.weightHistory(word, startYear, endYear).toString();
                response += "\n";
            }
        } else {
            response += "Please enter valid words.";
        }
        return response;
    }
}
