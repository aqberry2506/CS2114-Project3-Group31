import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

/**
 * Unit tests for AuctionQueue.
 */
public class AuctionQueueTest
{

    private AuctionQueue<String> queue;

    @BeforeEach
    public void setUp()
    {
        queue = new AuctionQueue<>();
    }


    @Test
    public void testNewQueueIsEmpty()
    {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }


    @Test
    public void testEnqueueAndPeek()
    {
        queue.enqueue("First");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals("First", queue.peek());
    }


    @Test
    public void testEnqueueNullThrowsException()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            queue.enqueue(null);
        });
    }


    @Test
    public void testFIFOOrder()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assertEquals(3, queue.size());
        assertEquals("A", queue.dequeue());
        assertEquals("B", queue.dequeue());
        assertEquals("C", queue.dequeue());
        assertTrue(queue.isEmpty());
    }


    @Test
    public void testDequeueEmptyThrowsException()
    {
        assertThrows(NoSuchElementException.class, () -> {
            queue.dequeue();
        });
    }


    @Test
    public void testPeekEmptyThrowsException()
    {
        assertThrows(NoSuchElementException.class, () -> {
            queue.peek();
        });
    }
}
