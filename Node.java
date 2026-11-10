/**
 * A single node in a singly linked structure. Holds one piece of data and a
 * reference to the next node.
 *
 * @param <T> the type of data stored in the node
 */
public class Node<T> {

    private final T data;
    private Node<T> next;

    /**
     * Creates a node holding the given data with no next node.
     *
     * @param data the data to store
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * @return the data stored in this node
     */
    public T getData() {
        return data;
    }

    /**
     * @return the next node, or null if this is the last node
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * @param next the node that should follow this one
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}
