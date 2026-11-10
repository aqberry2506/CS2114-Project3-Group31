import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core auction simulation. Owns the item queue and the bidder queue, runs one
 * item at a time, evaluates bids, and decides winners.
 *
 * MVP rules: each bidder gets exactly one turn per item (bid or pass). The
 * highest valid bid wins and only the winner pays. If everybody passes, the
 * item will not be sold. After a bidder's turn, they will move to the back of
 * the queue. When an item closes, the queue is rotated one extra place
 * so a different bidder goes first on the next item.
 *
 */
public class AuctionSim {

    private final AuctionQueue<Item> itemQueue;
    private final AuctionQueue<Bidder> bidderQueue;
    private Item currentActiveItem;
    private int turnsRemaining;
    private Item lastClosedItem;
    private final List<Item> closedItems;

    /**
     * Creates an empty simulation.
     */
    public AuctionSim() {
        itemQueue = new AuctionQueue<>();
        bidderQueue = new AuctionQueue<>();
        currentActiveItem = null;
        turnsRemaining = 0;
        lastClosedItem = null;
        closedItems = new ArrayList<>();
    }

    /**
     * Adds a bidder to the back of the bidder queue.
     *
     * @param bidder the bidder to add
     * @throws IllegalArgumentException if bidder is null
     */
    public void addBidder(Bidder bidder) {
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder cannot be null");
        }
        bidderQueue.enqueue(bidder);
    }

    /**
     * Adds an item to the back of the item queue.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if item is null
     */
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        itemQueue.enqueue(item);
    }

    /**
     * Starts the auction for the next item in the queue. Every bidder will get
     * one turn.
     *
     * @return the item now up for auction
     * @throws IllegalStateException if a round is already running, there are
     *                               no items, or there are no bidders
     */
    public Item startNextItem() {
        if (currentActiveItem != null) {
            throw new IllegalStateException("An item is already being auctioned");
        }
        if (itemQueue.isEmpty()) {
            throw new IllegalStateException("There are no items to auction");
        }
        if (bidderQueue.isEmpty()) {
            throw new IllegalStateException("There are no bidders");
        }
        currentActiveItem = itemQueue.dequeue();
        turnsRemaining = bidderQueue.size();
        return currentActiveItem;
    }

    /**
     * Tries to place a bid for the bidder whose turn it is. A valid bid is a
     * finite amount above the current highest bid and no more than the
     * bidder's remaining budget. If valid, it is recorded and the turn
     * advances. If not, nothing changes and the same bidder keeps the turn so
     * they can try again or pass.
     *
     * @param amount the bid amount
     * @return true if the bid was accepted, false if it was rejected
     * @throws IllegalStateException if there is no active turn
     */
    public boolean placeBid(double amount) {
        requireActiveTurn();
        Bidder bidder = bidderQueue.peek();
        if (!Double.isFinite(amount)
            || amount <= currentActiveItem.getCurrentBid()
            || amount > bidder.getBudget()) {
            return false;
        }
        currentActiveItem.recordBid(bidder, amount);
        advanceTurn();
        return true;
    }

    /**
     * The current bidder passes and the queue rotates to the next bidder.
     *
     * @throws IllegalStateException if there is no active turn
     */
    public void passTurn() {
        requireActiveTurn();
        advanceTurn();
    }

    /**
     * Ends the current item once every bidder has had their turn. The highest
     * bidder (if any) wins, pays their bid, and gets the item. If nobody bid,
     * the item is unsold and no budgets change. The bidder queue is then
     * rotated one extra place so the starting bidder changes next item.
     *
     * @throws IllegalStateException if there is no active round or some
     *                               bidders have not had their turn yet
     */
    public void closeCurrentItem() {
        if (currentActiveItem == null) {
            throw new IllegalStateException("No item is being auctioned");
        }
        if (turnsRemaining > 0) {
            throw new IllegalStateException("Some bidders have not had a turn");
        }
        Bidder winner = currentActiveItem.getHighestBidder();
        if (winner != null) {
            winner.deduct(currentActiveItem.getCurrentBid());
            winner.addWonItem(currentActiveItem);
        }
        closedItems.add(currentActiveItem);
        lastClosedItem = currentActiveItem;
        currentActiveItem = null;
        turnsRemaining = 0;

        // Fairness: shift who goes first on the next item.
        if (bidderQueue.size() > 1) {
            bidderQueue.enqueue(bidderQueue.dequeue());
        }
    }

    /**
     * @return true if there are items still waiting to be auctioned
     */
    public boolean hasMoreItems() {
        return !itemQueue.isEmpty();
    }

    /**
     * @return the item currently up for auction, or null if none
     */
    public Item getCurrentItem() {
        return currentActiveItem;
    }

    /**
     * @return the bidder whose turn it is, or null if there is no active turn
     */
    public Bidder getCurrentBidder() {
        if (currentActiveItem == null || turnsRemaining == 0) {
            return null;
        }
        return bidderQueue.peek();
    }

    // ----- Extras beyond the Deliverable 2 signatures (for the controller) -----

    /**
     * @return true if an item is being auctioned (even if all turns are done
     *         but it has not been closed yet)
     */
    public boolean isRoundActive() {
        return currentActiveItem != null;
    }

    /**
     * @return true if the active item has had every bidder's turn and is ready
     *         for closeCurrentItem()
     */
    public boolean isReadyToClose() {
        return currentActiveItem != null && turnsRemaining == 0;
    }

    /**
     * @return the most recently closed item (check getHighestBidder() for the
     *         winner, null if unsold), or null if no item has closed yet
     */
    public Item getLastClosedItem() {
        return lastClosedItem;
    }

    /**
     * @return a read-only list of all closed items in the order they were
     *         auctioned; an item with a null highest bidder went unsold
     */
    public List<Item> getClosedItems() {
        return Collections.unmodifiableList(closedItems);
    }

    // ----- Helpers -----

    private void requireActiveTurn() {
        if (currentActiveItem == null || turnsRemaining == 0) {
            throw new IllegalStateException("There is no active turn");
        }
    }

    private void advanceTurn() {
        bidderQueue.enqueue(bidderQueue.dequeue());
        turnsRemaining--;
    }
}
