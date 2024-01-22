package deque;

import edu.princeton.cs.algs4.In;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator c;
    public MaxArrayDeque(){
    }

    public MaxArrayDeque(Comparator<T> comparator) {
        c = comparator;
    }

    public T max(){
        return (T) getMax(this.c);
    }

    public T max(Comparator<T> c){
        return getMax(c);
    }

    public T getMax(Comparator<T> c) {
        if (size() != 0) {
            int max = getIndex(0);
            for (int i = 0; i < size(); i++) {
                if (c.compare(items[max], items[this.getIndex(i)]) < 0) {
                    max = i;
                }
            }
            return items[getIndex(max)];
        }
        return null;
    }




}
