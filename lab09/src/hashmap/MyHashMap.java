package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * A hash table-backed Map implementation.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @Ping Lin
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor;
    // You should probably define some more!

    /**
     * Constructors
     */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor      maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        size = 0;
        this.loadFactor = loadFactor;
        buckets = new Collection[initialCapacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = this.createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {

        return new LinkedList<>();
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void put(K key, V value) {
        Collection<Node> bucket = getBucket(key);
        if (bucket != null) {
            Node node = findNode(key);
            if (node != null) {
                node.value = value;
            } else {
                bucket.add(new Node(key, value));
                size++;
                if (checkIfOverLoad(buckets.length, size)) {
                    resize(buckets.length * 2);
                }
            }
        }
    }


    private void resize(int newCapacity) {
        Collection<Node>[] tempBuckets = buckets;
        buckets = new Collection[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            buckets[i] = this.createBucket();
        }
        for (int i = 0; i < tempBuckets.length; i++) {
            Iterator<Node> iterator = tempBuckets[i].iterator();
            while (iterator.hasNext()) {
                Node nextNode = iterator.next();
                getBucket(nextNode.key).add(nextNode);
            }
        }
    }

    private boolean checkIfOverLoad(int length, int size) {
        double loadFactorAfterPut = (size + 1.0) / length;
        if (loadFactorAfterPut > loadFactor) {
            return true;
        }
        return false;
    }

    /*will this bucket be null??? I assume no otherwise i have to new a linkedlist here which is against the intention.*/
    @Override
    public V get(K key) {
        Node node = findNode(key);
        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    private Node findNode(K key) {
        if (size == 0) {
            return null;
        }
        Collection<Node> bucket = getBucket(key);
        if (bucket != null) {
            Iterator<Node> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (node.key.equals(key)) {
                    return node;
                }
            }
        }
        return null;
    }

    private Collection<Node> getBucket(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        return buckets[index];
    }

    @Override
    public boolean containsKey(K key) {
        return (findNode(key) != null);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * collection have clear method
     */
    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i].clear();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();

    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
