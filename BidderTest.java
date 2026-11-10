import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Bidder.
 */
public class BidderTest
{

    private Bidder bidder;

    @BeforeEach
    public void setUp()
    {
        bidder = new Bidder("Bob", 500.0, "Sports");
    }


    @Test
    public void testValidCreation()
    {
        assertEquals("Bob", bidder.getName());
        assertEquals(500.0, bidder.getBudget());
        assertEquals("Sports", bidder.getPreference());
        assertTrue(bidder.getWonItems().isEmpty());
    }


    @Test
    public void testInvalidCreation()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Bidder("", 100, "Sports"));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Bidder(null, 100, "Sports"));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Bidder("Bob", -50, "Sports"));
        assertThrows(
            IllegalArgumentException.class,
            () -> new Bidder("Bob", 100, null));
    }


    @Test
    public void testDeduct()
    {
        bidder.deduct(150.0);
        assertEquals(350.0, bidder.getBudget());

        assertThrows(
            IllegalArgumentException.class,
            () -> bidder.deduct(-10.0));
        assertThrows(
            IllegalArgumentException.class,
            () -> bidder.deduct(600.0));
    }


    @Test
    public void testAddWonItem()
    {
        Item item = new Item("Bat", 50.0, "Sports");
        bidder.addWonItem(item);
        assertEquals(1, bidder.getWonItems().size());
        assertEquals(item, bidder.getWonItems().get(0));

        assertThrows(
            IllegalArgumentException.class,
            () -> bidder.addWonItem(null));
    }
}
