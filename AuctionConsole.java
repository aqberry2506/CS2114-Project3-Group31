
import java.util.Scanner;

/**
 * Console view for the Auction Simulator.
 * Handles user input and displays auction information.
 */
public class AuctionConsole implements AuctionController.AuctionView
{
    private final Scanner scanner;
    private final AuctionController controller;
    private final AuctionSim sim;
    private boolean inputError;

    public AuctionConsole(AuctionSim sim)
    {
        this.sim = sim;
        this.scanner = new Scanner(System.in);
        this.controller = new AuctionController(sim, this);
        this.inputError = false;
    }

    @Override
    public void showError(String message)
    {
        System.out.println("Error: " + message);
        inputError = true;
    }

    @Override
    public void showMessage(String message)
    {
        System.out.println(message);
    }

    @Override
    public void refresh()
    {
        Item item = sim.getCurrentItem();

        if (item == null)
        {
            return;
        }

        System.out.println("\n-------------------------");
        System.out.println("Current Item: " + item.getName());
        System.out.println("Category: " + item.getCategory());
        System.out.printf(
            "Starting Price: $%.2f%n", item.getStartPrice());
        System.out.printf(
            "Current Bid: $%.2f%n", item.getCurrentBid());

        Bidder highest = item.getHighestBidder();

        if (highest != null)
        {
            System.out.println(
                "Highest Bidder: " + highest.getName());
        }
        else
        {
            System.out.println("No bids yet.");
        }

        Bidder current = sim.getCurrentBidder();

        if (current != null)
        {
            System.out.println(
                "\nCurrent Turn: " + current.getName());
            System.out.printf(
                "Remaining Budget: $%.2f%n",
                current.getBudget());
        }
        else
        {
            System.out.println(
                "\nAll bidders have taken their turns.");
        }

        System.out.println("-------------------------");
    }

    @Override
    public void promptBid()
    {
        System.out.println("Please enter a valid bid.");
    }

    @Override
    public void promptBidderDetails()
    {
        System.out.println(
            "Please enter the bidder details again.");
    }

    @Override
    public void promptItemDetails()
    {
        System.out.println(
            "Please enter the item details again.");
    }

    @Override
    public void showResultsText(String text)
    {
        System.out.println("\n===== AUCTION RESULTS =====");
        System.out.println(text);
        System.out.println("===========================");
    }

    public String readLine()
    {
        return scanner.nextLine().trim();
    }

    private int readCount(String message)
    {
        while (true)
        {
            System.out.print(message);

            try
            {
                int count = Integer.parseInt(readLine());

                if (count >= 0)
                {
                    return count;
                }
            }
            catch (NumberFormatException e)
            {
                // Ask again for valid input.
            }

            System.out.println(
                "Please enter a non-negative whole number.");
        }
    }

    private double readAmount(String message)
    {
        while (true)
        {
            System.out.print(message);

            try
            {
                double amount = Double.parseDouble(readLine());

                if (Double.isFinite(amount) && amount >= 0)
                {
                    return amount;
                }
            }
            catch (NumberFormatException e)
            {
                // Ask again for valid input.
            }

            System.out.println("Please enter a valid amount.");
        }
    }

    private void addBidders()
    {
        int count = readCount("How many bidders? ");

        for (int i = 0; i < count; i++)
        {
            do
            {
                inputError = false;

                System.out.println("\nBidder " + (i + 1));

                System.out.print("Name: ");
                String name = readLine();

                double budget = readAmount("Budget: $");

                System.out.print("Preferred Category: ");
                String preference = readLine();

                controller.createBidder(
                    name, budget, preference);
            }
            while (inputError);
        }
    }

    private void addItems()
    {
        int count = readCount("\nHow many items? ");

        for (int i = 0; i < count; i++)
        {
            do
            {
                inputError = false;

                System.out.println("\nItem " + (i + 1));

                System.out.print("Name: ");
                String name = readLine();

                double price = readAmount("Starting Price: $");

                System.out.print("Category: ");
                String category = readLine();

                controller.createItem(
                    name, price, category);
            }
            while (inputError);
        }
    }

    private void runAuction()
    {
        while (sim.hasMoreItems())
        {
            controller.startAuction();

            if (sim.getCurrentItem() == null)
            {
                return;
            }

            while (sim.getCurrentItem() != null)
            {
                Bidder bidder = sim.getCurrentBidder();

                if (bidder == null)
                {
                    controller.finishRound();
                    break;
                }

                System.out.print(
                    "\nEnter bid amount or 'pass': ");

                String input = readLine();

                if (input.equalsIgnoreCase("pass"))
                {
                    controller.pass();
                }
                else
                {
                    controller.submitBid(input);
                }
            }
        }

        controller.showResults();
    }

    public void run()
    {
        System.out.println(
            "Welcome to the Auction Simulator!");

        addBidders();
        addItems();

        if (!sim.hasMoreItems())
        {
            System.out.println(
                "No items were added. Auction ended.");
            return;
        }

        System.out.println("\nStarting the auction...");

        runAuction();

        System.out.println(
            "\nThank you for participating!");
    }

    public static void main(String[] args)
    {
        AuctionSim sim = new AuctionSim();
        AuctionConsole console = new AuctionConsole(sim);
        console.run();
    }
}
