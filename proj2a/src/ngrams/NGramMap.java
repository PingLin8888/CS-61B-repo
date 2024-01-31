package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // TODO: Add any necessary static/instance variables.
    HashMap<String,TimeSeries> dataOfWords;
    TimeSeries dataOfCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        dataOfWords = new HashMap<>();
        dataOfCounts = new TimeSeries();
        In file = new In(wordsFilename);
        TimeSeries ts = new TimeSeries();
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splitLine = line.split("\t");
            String key = splitLine[0];
            if (!dataOfWords.containsKey(key)) {
                ts = new TimeSeries();
            }
            ts.put(Integer.parseInt(splitLine[1]), Double.parseDouble(splitLine[2]));
            dataOfWords.put(key, ts);
        }
        file = new In(countsFilename);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] splitLine = line.split(",");
            dataOfCounts.put(Integer.parseInt(splitLine[0]), Double.parseDouble(splitLine[1]));
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries returnHistory = countHistory(word);
        TimeSeries returnTS = new TimeSeries();
        if (!returnHistory.isEmpty()) {
            Map<Integer, Double> subMap = returnHistory.subMap(startYear, true, endYear, true);
            deepCopy(returnTS, subMap);
        }
        return returnTS;
    }

    private void deepCopy(TimeSeries returnCopy, Map<Integer, Double> original) {
        for (Map.Entry<Integer, Double> entry : original.entrySet()) {
            int key = entry.getKey();
            double value = entry.getValue();
            returnCopy.put(key, value);
        }
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        // TODO: Fill in this method.
        TimeSeries historyOfWord = dataOfWords.get(word);
        TimeSeries returnHistory = new TimeSeries();
        if (!historyOfWord.isEmpty()) {
            deepCopy(returnHistory, historyOfWord);
        }
        return returnHistory;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        TimeSeries returnHistory = new TimeSeries();
        if (!dataOfCounts.isEmpty()) {
            deepCopy(returnHistory, dataOfCounts);
        }
        return returnHistory;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries countHistory = countHistory(word, startYear, endYear);
        return getFrequency(countHistory);
    }

    private TimeSeries getFrequency(TimeSeries countHistory) {
        TimeSeries returnTS = new TimeSeries();
        int year;
        double wordCount, countAll, frequency;
        for (Map.Entry<Integer, Double> entry : countHistory.entrySet()) {
            year = entry.getKey();
            wordCount = entry.getValue();
            countAll = dataOfCounts.get(year);
            frequency = wordCount / countAll;
            returnTS.put(year, frequency);
        }
        return returnTS;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        TimeSeries countHistory = countHistory(word);
        return getFrequency(countHistory);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries returnTS = new TimeSeries();
        TimeSeries ts;
        double sum;
        for (int i = startYear; i <= endYear; i++) {
            sum = 0.0;
            for (String s : words) {
                ts = weightHistory(s, i, i);
                if (!ts.isEmpty()) {
                    sum += ts.get(i);
                }
            }
            returnTS.put(i, sum);
        }
        return returnTS;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
