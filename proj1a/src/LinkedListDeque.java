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
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T getRecursive(int index) {
        return null;
    }
}
