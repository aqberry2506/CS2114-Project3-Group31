import java.util.NoSuchElementException;

/**
 * A generic FIFO queue built on a singly linked list with head and tail
 * pointers. Used for both the bidders and the auction items.
 *
 * @param <T> the type of element stored in the queue
 */
public class AuctionQueue<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Creates an empty queue.
     */
    public AuctionQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds an element to the back of the queue.
     *
     * @param data the element to add
     * @throws IllegalArgumentException if data is null
     */
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot enqueue null");
        }
        Node<T> node = new Node<>(data);
        if (tail == null) {
            head = node;
        } else {
            tail.setNext(node);
        }
        tail = node;
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the front element
     * @throws NoSuchElementException if the queue is empty
     */
    public T dequeue() {
        if (head == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        T data = head.getData();
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }

    /**
     * Returns the element at the front without removing it.
     *
     * @return the front element
     * @throws NoSuchElementException if the queue is empty
     */
    public T peek() {
        if (head == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        return head.getData();
    }

    /**
     * @return true if the queue has no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of elements in the queue
     */
    public int size() {
        return size;
    }
}
