package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.*;

import java.util.Iterator;

public class BSTDictionary<K extends Comparable<? super K>, V>
        implements IDictionary<K, V> {

    protected BSTNode<K, V> root;
    protected int size;

    /**
     * Class representing an individual node in the Binary Search Tree
     */
    protected static class BSTNode<K, V> {
        public K key;
        public V value;

        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        /**
         * Constructor initializes this node's key, value, and children
         */
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.left = o.left;
            this.right = o.right;
        }

        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        public boolean hasBothChildren() {
            return this.left != null && this.right != null;
        }
    }

    /**
     * Initializes an empty Binary Search Tree
     */
    public BSTDictionary() {
        this.root = null;
        this.size = 0;
    }

    private BSTNode<K, V> getNode (BSTNode<K, V> current, K key) {
        if (current == null) {
            return null;
        }
        if (current.key.compareTo(key) == 0) {
            return current;
        }
        else if (current.key.compareTo(key) > 0) {
            return getNode(current.left, key);
        }
        else if (current.key.compareTo(key) < 0) {
            return getNode(current.right, key);
        }
        return null;
    }

    @Override
    public V get(K key) {
        BSTNode<K, V> node = getNode(this.root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public V remove(K key) {
        BSTNode<K, V> node = getNode(this.root, key);
        if (node == null) {
            return null;
        }
        else {
            V val = node.value;
            this.size--;
            this.root = remove(this.root, key);
            return val;
        }
    }

    private BSTNode<K, V> remove (BSTNode<K, V> current, K key) {
        if (current == null) {
            return null;
        }
        if (current.key.compareTo(key) == 0) {
            if (current.isLeaf()) {
                current = null;
            }
            else if (!current.hasBothChildren()) {
                if (current.left == null) {
                    current = current.right;
                }
                else {
                    current = current.left;
                }
            }
            else {
                BSTNode<K, V> newCurrent = findSuccessor(current);
                K k = newCurrent.key;
                V v = newCurrent.value;
                remove(k);
                this.size++;
                current.key = k;
                current.value = v;
            }
            return current;
        }
        else if (current.key.compareTo(key) > 0) {
            current.left = remove(current.left, key);
        }
        else if (current.key.compareTo(key) < 0) {
            current.right = remove(current.right, key);
        }
        return current;
    }

    //findMin(right subtree)
    private BSTNode<K, V> findSuccessor(BSTNode<K, V> current) {
        if (current.right.isLeaf()) {
            BSTNode<K, V> node = current.right;
            return node;
        }
        else {
            return findMin(current.right);
        }
    }
    private BSTNode<K, V> findMin(BSTNode<K, V> current) {
        if (current.left == null) {
            return current;
        }
        else {
            return findMin(current.left);
        }
    }

    @Override
    public V put(K key, V value) {
        BSTNode<K, V> node = getNode(this.root, key);
        if (node == null) {
            this.root = put(this.root, key, value);
            size++;
            return null;
        }
        else {
            V val = node.value;
            node.value = value;
            return val;
        }
    }

    private BSTNode<K, V> put (BSTNode<K, V> current, K key, V value) {
        if (current == null) {
            current = new BSTNode<K, V>(key, value);
        }
        else if (current.key.compareTo(key) > 0) {
            current.left = put(current.left, key, value);
        }
        else if (current.key.compareTo(key) < 0) {
            current.right = put(current.right, key, value);
        }
        else if (current.key.compareTo(key) == 0) {
            current.value = value;
        }

        return current;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(this.root, key);
    }
    private boolean containsKey(BSTNode<K, V> current, K key) {
        if (current == null) {
            return false;
        }
        else if (current.key.compareTo(key) == 0) {
            return true;
        }
        else if (current.key.compareTo(key) < 0) {
            return containsKey(current.right, key);
        }
        else {
            return containsKey(current.left, key);
        }
    }

    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
    }


    /**
     * @return number of nodes in the BST
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keySet() {
        ICollection<K> result = new LinkedDeque<K>();
        keySet(this.root, result);
        return result;
    }
    private void keySet(BSTNode<K, V> current, ICollection<K> result) {
        if (current == null) {
            return;
        }
        else {
            result.add(current.key);
            keySet(current.left, result);
            keySet(current.right, result);
        }
    }

    //recurse
    @Override
    public ICollection<V> values() {
        ICollection<V> result = new LinkedDeque<V>();
        values(this.root, result);
        return result;
    }
    private void values(BSTNode<K, V> current, ICollection<V> result) {
        if (current == null) {
            return;
        }
        else {
            result.add(current.value);
            values(current.left, result);
            values(current.right, result);
        }
    }

    /**
     * Implementation of an iterator over the BST
     */

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.left != null) {
                nodes.enqueue(current.left);
            }
            if (current.right != null) {
                nodes.enqueue(current.right);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }
}
