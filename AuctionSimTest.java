import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AuctionSim.
 */
public class AuctionSimTest
{

    private AuctionSim sim;

    @BeforeEach
    public void setUp()
    {
        sim = new AuctionSim();
    }


    @Test
    public void testAddAndRunBasicAuction()
    {
        Bidder b1 = new Bidder("Alice", 100, "Art");
        Bidder b2 = new Bidder("Bob", 100, "Art");
        Item item = new Item("Painting", 20, "Art");

        sim.addBidder(b1);
        sim.addBidder(b2);
        sim.addItem(item);

        assertTrue(sim.hasMoreItems());
        assertEquals(item, sim.startNextItem());
        assertTrue(sim.isRoundActive());

        assertEquals(b1, sim.getCurrentBidder());
        assertTrue(sim.placeBid(30));
        assertEquals(b2, sim.getCurrentBidder());
        sim.passTurn();

        assertTrue(sim.isReadyToClose());
        sim.closeCurrentItem();

        assertEquals(item, sim.getLastClosedItem());
        assertEquals(b1, item.getHighestBidder());
        assertEquals(70.0, b1.getBudget());
    }


    @Test
    public void testExceptionsWhenStartingEmpty()
    {
        assertThrows(IllegalStateException.class, () -> sim.startNextItem());
    }
}
