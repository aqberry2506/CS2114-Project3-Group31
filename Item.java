/**
 * An item up for auction. Tracks its name, starting price, category, the
 * current highest bid, and the bidder who made it.
 */
public class Item {

    private final String name;
    private final double startPrice;
    private final String category;
    private double currentBid;
    private Bidder highestBidder;

    /**
     * Creates a new auction item with no bids.
     *
     * @param name       the item's name
     * @param startPrice the starting price (must be finite and not negative)
     * @param category   the item's category (e.g. sports, cards)
     * @throws IllegalArgumentException if name or category is null/blank, or
     *                                  startPrice is negative or not finite
     */
    public Item(String name, double startPrice, String category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Item category cannot be blank");
        }
        if (!Double.isFinite(startPrice) || startPrice < 0) {
            throw new IllegalArgumentException(
                "Start price must be a non-negative, finite number");
        }
        this.name = name.trim();
        this.category = category.trim();
        this.startPrice = startPrice;
        this.currentBid = startPrice;
        this.highestBidder = null;
    }

    /**
     * @return the item's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the item's starting price
     */
    public double getStartPrice() {
        return startPrice;
    }

    /**
     * @return the item's category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the current highest bid, or the starting price if no bids yet
     */
    public double getCurrentBid() {
        return currentBid;
    }

    /**
     * @return the bidder with the highest bid, or null before any valid bid
     */
    public Bidder getHighestBidder() {
        return highestBidder;
    }

    /**
     * Records a new highest bid. The bid must be strictly greater than the
     * current bid (which is the starting price if nobody has bid yet) and no
     * more than the bidder's remaining budget. If the bid is rejected the
     * item's state is left unchanged.
     *
     * @param bidder the bidder making the bid
     * @param amount the amount of the bid
     * @throws IllegalArgumentException if bidder is null, amount is not
     *                                  finite, amount is not above the
     *                                  current bid, or amount exceeds the
     *                                  bidder's budget
     */
    public void recordBid(Bidder bidder, double amount) {
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder cannot be null");
        }
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Bid must be a finite number");
        }
        if (amount <= currentBid) {
            throw new IllegalArgumentException(
                "Bid must be higher than the current bid of " + currentBid);
        }
        if (amount > bidder.getBudget()) {
            throw new IllegalArgumentException(
                "Bid exceeds the bidder's remaining budget");
        }
        currentBid = amount;
        highestBidder = bidder;
    }
}
