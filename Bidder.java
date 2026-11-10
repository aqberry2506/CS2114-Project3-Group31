import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A participant in the auction. Has a name, a remaining budget, a preferred
 * item category, and a list of the items they have won.
 */
public class Bidder {

    private final String name;
    private double budget;
    private final String preference;
    private final List<Item> wonItems;

    /**
     * Creates a bidder with a starting budget.
     *
     * @param name       the bidder's name
     * @param budget     starting money (must be finite and not negative)
     * @param preference the bidder's preferred item category
     * @throws IllegalArgumentException if name is null/blank, preference is
     *                                  null, or budget is negative or not
     *                                  finite
     */
    public Bidder(String name, double budget, String preference) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bidder name cannot be blank");
        }
        if (preference == null) {
            throw new IllegalArgumentException("Preference cannot be null");
        }
        if (!Double.isFinite(budget) || budget < 0) {
            throw new IllegalArgumentException(
                "Budget must be a non-negative, finite number");
        }
        this.name = name.trim();
        this.budget = budget;
        this.preference = preference.trim();
        this.wonItems = new ArrayList<>();
    }

    /**
     * @return the bidder's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the money the bidder has left (can be 0)
     */
    public double getBudget() {
        return budget;
    }

    /**
     * @return the bidder's preferred item category
     */
    public String getPreference() {
        return preference;
    }

    /**
     * Subtracts an amount (e.g. the winning bid) from the budget.
     *
     * @param amount the amount to subtract
     * @throws IllegalArgumentException if amount is negative, not finite, or
     *                                  more than the remaining budget; the
     *                                  budget is unchanged in that case
     */
    public void deduct(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException(
                "Amount must be a non-negative, finite number");
        }
        if (amount > budget) {
            throw new IllegalArgumentException(
                "Amount exceeds the remaining budget");
        }
        budget -= amount;
    }

    /**
     * Records an item this bidder has won.
     *
     * @param item the item won
     * @throws IllegalArgumentException if item is null
     */
    public void addWonItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        wonItems.add(item);
    }

    /**
     * @return a read-only view of the items this bidder has won
     */
    public List<Item> getWonItems() {
        return Collections.unmodifiableList(wonItems);
    }
}
