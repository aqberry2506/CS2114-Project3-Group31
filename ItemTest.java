import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Item.
 */
public class ItemTest
{

    private Item item;
    private Bidder bidder;

    @BeforeEach
    public void setUp()
    {
        item = new Item("Laptop", 100.0, "Electronics");
        bidder = new Bidder("Alice", 200.0, "Electronics");
    }


    @Test
    public void testValidCreation()
    {
        assertEquals("Laptop", item.getName());
        assertEquals(100.0, item.getStartPrice());
        assertEquals("Electronics", item.getCategory());
        assertEquals(100.0, item.getCurrentBid());
        assertNull(item.getHighestBidder());
    }


    @Test
    public void testInvalidCreation()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Item(null, 50, "Cat"));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Item("   ", 50, "Cat"));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Item("Item", 50, null));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Item("Item", -10, "Cat"));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Item("Item", Double.NaN, "Cat"));
    }


    @Test
    public void testRecordValidBid()
    {
        item.recordBid(bidder, 150.0);
        assertEquals(150.0, item.getCurrentBid());
        assertEquals(bidder, item.getHighestBidder());
    }


    @Test
    public void testRecordInvalidBid()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> item.recordBid(null, 150.0));
        assertThrows(
            IllegalArgumentException.class,
            () -> item.recordBid(bidder, 90.0));
        assertThrows(
            IllegalArgumentException.class,
            () -> item.recordBid(bidder, 250.0));
        assertThrows(
            IllegalArgumentException.class,
            () -> item.recordBid(bidder, Double.NaN));
    }
}
