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
        assertEquals(1, sim.getCurrentLoop());

        assertEquals(b1, sim.getCurrentBidder());
        assertTrue(sim.placeBid(30));
        assertEquals(b2, sim.getCurrentBidder());
        sim.passTurn();

        while (!sim.isReadyToClose())
        {
            sim.passTurn();
        }

        assertTrue(sim.isReadyToClose());
        sim.closeCurrentItem();

        assertEquals(item, sim.getLastClosedItem());
        assertEquals(b1, item.getHighestBidder());
        assertEquals(70.0, b1.getBudget());
    }
    
    
    @Test
    public void testBidLoopExecution()
    {
        Bidder b1 = new Bidder("Alice", 200, "Tech");
        Bidder b2 = new Bidder("Bob", 200, "Tech");
        Item item = new Item("Tablet", 50, "Tech");

        sim.addBidder(b1);
        sim.addBidder(b2);
        sim.addItem(item);

        sim.startNextItem();
        
        sim.passTurn();
        sim.passTurn();
        
        assertEquals(2, sim.getCurrentLoop());
        
        while (!sim.isReadyToClose())
        {
            sim.passTurn();
        }
        
        assertTrue(sim.isReadyToClose());
        sim.closeCurrentItem();
        assertFalse(sim.isRoundActive());
    }


    @Test
    public void testBadInputsAndNulls()
    {
        assertThrows(IllegalArgumentException.class, () -> sim.addBidder(null));
        assertThrows(IllegalArgumentException.class, () -> sim.addItem(null));
        assertThrows(IllegalStateException.class, () -> sim.startNextItem());
    }


    @Test
    public void testActionWithoutActiveTurnThrowsException()
    {
        assertThrows(IllegalStateException.class, () -> sim.placeBid(50.0));
        assertThrows(IllegalStateException.class, () -> sim.passTurn());
        assertThrows(IllegalStateException.class, () -> sim.closeCurrentItem());
    }
    
    
    @Test
    public void testExceptionsWhenStartingEmpty()
    {
        assertThrows(IllegalStateException.class, () -> sim.startNextItem());
    }
}
