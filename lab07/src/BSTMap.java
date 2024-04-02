import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private class BSTNode{
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        BSTNode(K key, V value, BSTNode left, BSTNode right){
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        BSTNode get(BSTNode nodeRoot, K k){
            if (k == null || nodeRoot == null) {
                return null;
            }
            else if(k.equals(nodeRoot.key)){
                return nodeRoot;
            } else if (k.compareTo(nodeRoot.key) < 0) {
                return get(nodeRoot.left, k);
            } else if (k.compareTo(nodeRoot.key) > 0) {
                return get(nodeRoot.right, k);
            } else {
                return null;
            }
        }
    }

    int size;
    private BSTNode root;

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value, null, null);
            size++;
        } else {
            BSTNode result = root.get(root, key);
            if (result == null) {
                insertNode(root, key, value);
                size++;
            } else {
                result.value = value;
            }
        }
    }

    private void insertNode(BSTNode root, K key, V value) {
        if (key.compareTo(root.key) > 0 && root.right == null) {
            root.right = new BSTNode(key, value, null, null);
        } else if (key.compareTo(root.key) > 0 && root.right != null) {
            insertNode(root.right, key, value);
        } else if(key.compareTo(root.key) < 0 && root.left == null){
            root.left = new BSTNode(key, value, null, null);
        }else if(key.compareTo(root.key) < 0 && root.left != null){
            insertNode(root.left, key, value);
        }

    }

    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        BSTNode result = root.get(root, key);
        if (result == null) {
            return null;
        }
        return result.value;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return root.get(root, key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
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

    public void printInOrder(){
        printMap(root);
    }

    private void printMap(BSTNode root) {
        if (root != null) {
            printMap(root.left);
            System.out.println(root.key);
            printMap(root.right);
        }
    }

}
