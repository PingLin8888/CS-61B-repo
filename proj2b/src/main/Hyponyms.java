package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Hyponyms {
    final private List<Integer> listOfHyponyms;

    public Hyponyms() {
        listOfHyponyms = new ArrayList<>();
    }

    public Hyponyms(Collection<Integer> hyponyms) {
        this.listOfHyponyms = new ArrayList<>(hyponyms);
    }



    public void addHyponym(int index) {
        listOfHyponyms.add(index);
    }

    public int getSize() {
        return listOfHyponyms.size();
    }

    public Integer getIndex(int index) {
        return listOfHyponyms.get(index);
    }

    public void addAll(List<Integer> hyponyms) {
        listOfHyponyms.addAll(hyponyms);
    }

    public List<Integer> getListOfHyponyms() {
        return listOfHyponyms;
    }
}
