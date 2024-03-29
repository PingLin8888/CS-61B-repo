import java.util.ArrayList;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int nextFirst;
    private int nextLast;
    T[] items;
    final double usageRate=0.25;

    public ArrayDeque() {
        items= (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }

    @Override
    public void addFirst(Object x) {
        if (size == items.length) {
            resizeUp(2 * size);
        }
        items[nextFirst] = (T) x;
        updateNextFirst(nextFirst);
        size++;
    }

    private void updateNextFirst(int nextFirst) {
        if (nextFirst == 0) {
            this.nextFirst = items.length - 1;
        }
        else {
            this.nextFirst--;
        }
    }

    /*When size equals item.length and need to add one more item*/
    private void resizeUp(int capacity) {
        resize(capacity);
    }

    /*When items.length is bigger than 15 and usageRate is below 0.25*/
    private void resizeDown() {
        if (items.length > 15 && ((double) (size - 1) / items.length) < usageRate) {
            resize(Math.max((int) ((size - 1) / usageRate), 15));
        }
    }

    /*Helper function to help copy elements from old array to new array*/
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
//            if (nextFirst + 1 + i >= size) {
//                temp[i] = items[nextFirst + 1 + i -size];
//            } else {
//                temp[i] = items[nextFirst + 1 + i];
//            }
            temp[i] = items[getIndex(i)];
        }
        items = temp;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    private int getIndex(int i) {
        int index;
        if (nextFirst + 1 + i >= items.length) {
            index = nextFirst + 1 + i - items.length;
        } else {
            index = nextFirst + 1 + i;
        }
        return index;
    }

    @Override
    public void addLast(Object x) {
        if (size == items.length) {
            resizeUp(2 * size);
        }
        items[nextLast] = (T) x;
        updateNextLast(nextLast);
        size++;
    }

    private void updateNextLast(int nextLast) {
        if (nextLast == items.length - 1) {
            this.nextLast = 0;
        }
        else {
            this.nextLast++;
        }
    }

    @Override
    public List toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            returnList.add(items[getIndex(i)]);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Remove and return the element at the front of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeFirst() {
        if (size >= 1) {
            resizeDown();
            int newNextFirst = nextFirst + 1;
            if (!validIndex(newNextFirst)) {
                newNextFirst = 0;
            }
            T del = items[newNextFirst];
            items[newNextFirst] = null;
            nextFirst = newNextFirst;
            size--;
            return del;
        }
        return null;
    }

    /**
     * Remove and return the element at the back of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeLast() {
        if (size >= 1) {
            resizeDown();
            int newNextLast = nextLast - 1;
            if (!validIndex(newNextLast)) {
                newNextLast = items.length - 1;
            }
            T del = items[newNextLast];
            items[newNextLast] = null;
            nextLast = newNextLast;
            size--;
            return del;
        }
        return null;
    }

    @Override
    public T get(int index) {
        if (validIndex(index)) {
            return items[index];
        }
        return null;
    }

    private boolean validIndex(int index) {
//        if (nextLast <= nextFirst) {
//            if (index > nextFirst && index < items.length) {
//                return true;
//            } else if (index >= 0 && index < nextLast) {
//                return true;
//            }
//        } else if (index > nextFirst && index < nextLast) {
//            return true;
//        }
//        return false;
        if (nextFirst <= nextLast) {
            return (index >= nextFirst && index <= nextLast);
        }
        return (index >= nextFirst && index < items.length) || index >= 0 && index <= nextLast;
    }


    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("\"No need to implement getRecursive for proj 1b\"");
    }
}
