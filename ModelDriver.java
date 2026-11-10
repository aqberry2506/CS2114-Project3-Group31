/**
 * A tiny driver that runs one auction using only the model classes
 * (no view, no controller).
 */
public class ModelDriver {

    public static void main(String[] args) {
        AuctionSim sim = new AuctionSim();
        sim.addBidder(new Bidder("Alex", 100, "Sports"));
        sim.addBidder(new Bidder("Bea", 50, "Cards"));
        sim.addItem(new Item("Shoes", 20, "Sports"));

        sim.startNextItem();
        sim.placeBid(30);   // Alex bids
        sim.placeBid(40);   // Bea bids
        sim.closeCurrentItem();

        Item shoes = sim.getLastClosedItem();
        System.out.println(shoes.getHighestBidder().getName()
            + " won " + shoes.getName() + " at " + shoes.getCurrentBid());
    }
}
