public class AuctionController
{
    public interface AuctionView
    {
        void showError(String message);     // bad input, wrong state


        void showMessage(String message);   // neutral info


        void refresh();                     // redraw current item, bidder, bid


        void promptBid();                   // re-prompt for a bid


        void promptBidderDetails();         // re-prompt for bidder fields


        void promptItemDetails();           // re-prompt for item fields


        void showResultsText(String text);  // final results screen
    }

    // ~ Fields ................................................................
    private final AuctionSim sim;
    private final AuctionView view;
    private final List<String> results = new ArrayList<>();

    // ~ Constructors ..........................................................
    /**
     * 
     */
    public AuctionController(AuctionSim sim, AuctionView view)
    {
        this.sim = sim;
        this.view = view;

    }


    // ~Public Methods ........................................................
    /**
     * 
     */
    public void createBidder(String name, double budget, String preference)
    {
        try
        {
            sim.addBidder(new Bidder(name, budget, preference));
            view.showMessage("New Bidder added: " + name + "!");
        }
        catch (IllegalArgumentException e)
        {
            view.showError(e.getMessage());
            view.promptBidderDetails();
        }
    }


    /**
     * 
     */
    public void createItem(String name, double startPrice, String category)
    {
        try
        {
            sim.addItem(new Item(name, startPrice, category));
            view.showMessage("Added item " + name + ".");
        }
        catch (IllegalArgumentException e)
        {
            view.showError(e.getMessage());
            view.promptItemDetails();
        }
    }


    public void startAuction()
    {
        try
        {
            sim.startNextItem();
            view.refresh();
        }
        catch (IllegalStateException e)
        {

            view.showMessage(e.getMessage());
        }
    }


    public void submitBid(String input)
    {
        if (input == null || input.trim().isEmpty())
        {
            view.showError("Please enter a bid amount.");
            view.promptBid();
            return;
        }

        double amount;
        try
        {
            amount = Double.parseDouble(input.trim());
        }
        catch (NumberFormatException e)
        {
            view.showError("\"" + input.trim() + "\" is not a number.");
            view.promptBid();
            return;
        }

        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0)
        {
            view.showError("Bid must be a positive, finite number.");
            view.promptBid();
            return;
        }

        try
        {
            if (sim.placeBid(amount))
            {
                view.refresh();
            }
            else
            {
                view.showError("Bid is too low or exceeds your budget.");
                view.promptBid();
            }
        }
        catch (IllegalStateException e)
        {
            view.showError(e.getMessage());
        }
    }


    public void pass()
    {
        try
        {
            sim.passTurn();
            view.refresh();
        }
        catch (IllegalStateException e)
        {
            view.showError(e.getMessage());
        }
    }


    public void finishRound()
    {
        try
        {

            Item item = sim.getCurrentItem();
            Bidder winner = (item == null) ? null : item.getHighestBidder();
            double price = (item == null) ? 0 : item.getCurrentBid();

            sim.closeCurrentItem();

            String line = (winner == null)
                ? item.getName() + ": unsold (no bids)."
                : item.getName() + ": won by " + winner.getName() + " for $"
                    + String.format("%.2f", price) + ".";
            results.add(line);
            view.showMessage(line);
            view.refresh();
        }
        catch (IllegalStateException e)
        {
            view.showError("Round can't be closed yet: " + e.getMessage());
        }
    }


    public void showResults()
    {
        if (results.isEmpty())
        {
            view.showMessage(
                "No results yet. Finish at least one round first.");
            return;
        }
        view.showResultsText(String.join("\n", results));
    }

}
