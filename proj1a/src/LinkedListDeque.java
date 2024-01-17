import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {
    private int size;
    private StuffNode sentinel;
    public LinkedListDeque() {
        size = 0;
        sentinel = new StuffNode<>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public class StuffNode<T> {
        public T item;
        public StuffNode prev;
        public StuffNode next;

        public StuffNode(T i, StuffNode p, StuffNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    @Override
    public void addFirst(T x) {
        size++;
        if (sentinel.next == sentinel) {
            sentinel.next = new StuffNode<>(x, sentinel, sentinel);
            sentinel.prev = sentinel.next;
        }
        else{
            sentinel.next = new StuffNode<>(x, sentinel, sentinel.next);
            sentinel.next.next.prev = sentinel.next;
        }
    }

    @Override
    public void addLast(T x) {
        size++;
        if (sentinel.next == sentinel) {
            sentinel.next = new StuffNode<>(x, sentinel, sentinel);
            sentinel.prev = sentinel.next;
        }
        else{
            sentinel.prev = new StuffNode<>(x, sentinel.prev, sentinel);
            sentinel.prev.prev.next = sentinel.prev;
        }
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        StuffNode temp = new StuffNode<>(null, null, sentinel.next);
        while (temp.next != sentinel) {
            returnList.add((T) temp.next.item);
            temp.next = temp.next.next;
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
//        if (size == 1) {
//            sentinel.next = sentinel;
//            sentinel.prev = sentinel;
//            size--;
//        } else if (size > 1) {
//            sentinel.next = sentinel.next.next;
//            sentinel.next.prev = sentinel;
//            size--;
//        }
        if (size >= 1) {
            StuffNode delNode = sentinel.next;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return (T) delNode;
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
            StuffNode delNode = sentinel.prev;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return (T) delNode;
        }
        return null;
    }

    @Override
    public T get(int index) {
        StuffNode temp = new StuffNode<>(null, null, sentinel.next);
        int count = 0;
        while (size > 0 && size > index) {
            if (count == index) {
                return (T) temp.next.item;
            }
            temp.next = temp.next.next;
            count++;
        }
        return null;
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) getRecursiveHelper(index, sentinel.next);
    }

    private T getRecursiveHelper(int index, StuffNode node) {
        if (index == 0) {
            return (T) node.item;
        }
        return (T) getRecursiveHelper(index-1, node.next);
    }
}
