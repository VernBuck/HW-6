import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Vernon Buck
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {

    // Do not make any new instance variables.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     */
    public AVL() {
        root = null;
        size = 0;
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        this();
        if (data == null) {
            throw new IllegalArgumentException("data not there");
        }
        for (T x : data) {
            add(x);
        }
    }

    /**
     * Updates height and balance factor
     * @param n the node passed
     */
    private void update(AVLNode<T> n) {
        n.setBalanceFactor(height2(n.getLeft()) - height2(n.getRight()));
        n.setHeight(1 + Math.max(height2(n.getRight()), height2(n.getLeft())));
    }

    /**
     * Performs a right rotation
     * @param n the node passed
     * @return proper node position
     */
    private AVLNode<T> rRotation(AVLNode<T> n) {
        AVLNode<T> n1 = n;
        AVLNode<T> n2 = n.getLeft();
        n1.setLeft(n2.getRight());
        n2.setRight(n1);
        update(n1);
        update(n2);
        return n2;
    }

    /**
     * Performs a left rotation
     * @param n the node passed
     * @return proper node position
     */
    private AVLNode<T> lRotation(AVLNode<T> n) {
        AVLNode<T> n1 = n;
        AVLNode<T> n2 = n.getRight();
        n1.setRight(n2.getLeft());
        n2.setLeft(n1);
        update(n1);
        update(n2);
        return n2;
    }

    /**
     * Performs a zig-zag rotation
     * @param n the node passed
     * @return proper node position
     */
    private AVLNode<T> rlRotation(AVLNode<T> n) {
        AVLNode<T> n3 = rRotation(n.getRight());
        n.setRight(n3);
        return lRotation(n);
    }

    /**
     * Performs a zig-zag rotation
     * @param n the node passed
     * @return proper node position
     */
    private AVLNode<T> lrRotation(AVLNode<T> n) {
        AVLNode<T> n3 = lRotation(n.getLeft());
        n.setLeft(n3);
        return rRotation(n);
    }

    @Override
    /**
     * Add the data as a leaf in the AVL.  Should traverse the tree to find the
     * appropriate location. If the data being added already exists in the
     * tree, do nothing. The tree has to be balanced after each adding
     * operation.
     *
     * Rotations are performed as necessary.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("null data passed");
        }
        root = addHelper(root, data);
    }

    /**
     * Adds and rotates and balances nodes
     * @param node node being used to traverse
     * @param data data of node to create
     * @return the balanced tree/root
     */
    private AVLNode<T> addHelper(AVLNode<T> node, T data) {
        if (node == null) {
            size++;
            return new AVLNode<>(data);
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(addHelper(node.getLeft(), data));
        } else {
            node.setRight(addHelper(node.getRight(), data));
        }
        update(node);
        if (node.getBalanceFactor() > 1) {
            if (node.getLeft().getBalanceFactor() < 0) {
                return lrRotation(node);
            } else {
                return rRotation(node);
            }
        } else if (node.getBalanceFactor() < -1) {
            if (node.getRight().getBalanceFactor() < 0) {
                return lRotation(node);
            } else {
                return rlRotation(node);
            }
        }
        return node;
    }

    @Override
    public T remove(T data) {
    	if (data == null) {
            throw new IllegalArgumentException("null data passed");
        }
        T dat = remHelper(root, data);
        size--;
        return dat;
    }

    /**
     * Helper for tree traversal
     * @param node node used for traverse
     * @param data data to match with node
     * @return removed data
     */
    private T remHelper(AVLNode<T> node, T data) {
        if (data == null) {
            throw new NoSuchElementException("data not there");
        }
        return null;
    }

    @Override
    public List<T> findPathBetween(T start, T end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("null start or end");
        }

        List<T> list = new ArrayList<>();
        return findHelper(start, end, list, root);
    }

    /**
     * Traverses through the tree
     * @param start initial data
     * @param end data to end on
     * @param list list containing data
     * @param node node being used to traverse
     * @return complete list
     */
    private List<T> findHelper(T start, T end, List<T> list, AVLNode<T> node) {
        if (start == null || end == null) {
            throw new NoSuchElementException("data not there");
        }
        if (start.compareTo(end) == 0) {
            list.add(start);
            return list;
        }
        if (node != null && node != end) {
            findHelper(start, end, list, node.getLeft());
            list.add(node.getData());
            findHelper(start, end, list, node.getRight());
        }
        return list;
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("null data");
        }
    	return helper3(root, data);
    }

    /**
     * Recursive method called for traversal
     * @param node node being checked
     * @param data data being compared
     * @return node requested
     */
    private T helper3(AVLNode<T> node, T data) {
        if (node == null) {
            throw new NoSuchElementException("Element not there");
        }
        if (node.getData().compareTo(data) == 0) {
            return node.getData();
        } else if (node.getData().compareTo(data) > 0) {
            return helper3(node.getLeft(), data);
        } else {
            return helper3(node.getRight(), data);
        }
    }

    @Override
    public boolean contains(T data) {
    	try {
            get(data);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    @Override
    public int size() {
    	return size;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public int height() {
        if (root == null) {
            return -1;
        } else {
            return root.getHeight();
        }
    }

    /**
     * Helper used to get specific node height
     * @param n node being passed
     * @return height of passed node
     */
    private int height2(AVLNode<T> n) {
        if (n == null) {
            return -1;
        } else {
            return n.getHeight();
        }
    }

    @Override
    public int depth(T data) {
        if (data == null) {
            throw new IllegalArgumentException("null data");
        }
        return helper2(root, data);
    }

    /**
     * Method used to obtain tree depth
     * @param node current node be traversed
     * @param data data to compare with node
     * @return the depth of the tree
     */
    private int helper2(AVLNode<T> node, T data) {
        if (data == null) {
            throw new NoSuchElementException("Data not here");
        }
        int count = 0;
        if (node.getData().compareTo(data) == 0) {
            count++;
            return count;
        } else if (node.getData().compareTo(data) < 0) {
            count++;
            return helper2(node.getRight(), data) + 1;
        } else {
            count++;
            return helper2(node.getLeft(), data) + 1;
        }
    }

    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     * DO NOT USE IT IN YOUR CODE
     * DO NOT CHANGE THIS METHOD
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        return root;
    }
    
    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     * DO NOT USE IT IN YOUR CODE
     * DO NOT CHANGE THIS METHOD
     *
     * @param node the root of the tree
     */
    public void setRoot(AVLNode<T> node) {
        this.root = node; 
    }

    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     * DO NOT USE IT IN YOUR CODE
     * DO NOT CHANGE THIS METHOD
     *
     * @param size the size of the tree
     */
    public void setSize(int size) {
        this.size = size;
    }

}
