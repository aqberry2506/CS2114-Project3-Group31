# Team 31 — Auction Simulator

CS 2114 · Project 3: Deliverable 4 — Second Prototype and Tests

A console auction simulator. Bidders with budgets take turns bidding on items across multi-round bid loops. The highest bid wins and the winner pays for the item. If everyone passes, the item will go unsold. One generic linked queue (AuctionQueue<T>) holds both items and bidders.

- **Model:** `Node`, `AuctionQueue`, `Item`, `Bidder`, `AuctionSim`
- **View:** `AuctionConsole`
- **Controller:** `AuctionController`

---

## How to run

All files are in one folder. Java 11 or newer is needed.

**Model only (no view needed):**

```
javac Node.java AuctionQueue.java Item.java Bidder.java AuctionSim.java ModelDriver.java
java ModelDriver
```

`ModelDriver.java` builds two bidders and one item, places two bids, closes the item and prints the winner. It uses only the model classes. Expected output:

```
Bea won Shoes at 40.0
```

**Tests (JUnit 5):** Run via Eclipse (Right click on the test file -> Run as -> JUnit Test)

**Thin view:** javac Node.java AuctionQueue.java Item.java Bidder.java AuctionSim.java AuctionController.java AuctionConsole.java
java AuctionConsole

---

## Reflection

### How did our design absorb the change?

In Deliverable 3, we predicted that our change feature (Bid Loop) would require edits in `AuctionSim` (to track loops and active participants) and `AuctionConsole` (to display the loop count), while `Node`, `AuctionQueue`, `Item`, and `Bidder` would remain completely untouched.
- **What matched:** Our prediction was 100% accurate. The core linked list data structures and data objects required zero structural changes.
- **What surprised us:** We were initially worried the controller would get messy trying to figure out when a loop ends. But by keeping all the loop counters and our isReadyToClose() check tucked away inside AuctionSim, our controller stayed unaffected.

### Did the change cross the model/view line?

No, our model/view boundary stayed completely solid. `AuctionConsole` doesn't hold onto copies of model state; it just grabs what it needs by calling getters like `sim.getCurrentLoop()` and `sim.getMaxLoops()` when `refresh()` runs. User input only flows one way: view -> controller -> model.

### What changed from our D3 plan?

Our core method signatures and validation rules stayed right on track with our Deliverable 3 plan. We just added private loop tracking fields (`currentLoop`, `MAX_LOOPS`) and helper methods to `AuctionSim` without breaking any existing signatures.

### What design change would make the next feature cheaper?

If we were adding something like automatic proxy bidding next, setting up an Observer / Event Listener pattern between `AuctionSim` and the controller/view would make things way better. Right now, the view is polling the model during a refresh; an observer system would let the model broadcast state changes automatically.

### What doesn't work yet?

- Automatic proxy bidding where bidder preferences dynamically adjust their max bids.
- Saving or exporting completed auction summaries to an external file.

---

## Next Steps

### What is left for the final build? (in order)

1. Implement more logic on dropping uninterested bidders and advancing preference logic.
2. Implement constant file logging of completed auction results.
3. Clean up terminal UI formatting, spacing, and error recovery prompts.


### Stretch Goals Committed vs. Dropped

- **Committed:** The Bid Loop feature was fully implemented and JUnit test coverage with queue operations and bad-input validation.
- **Dropped:** Graphical UI displaying all items to bidders before each round.

### Three-Minute Demo Path

1. **Setup & Initialization:** Launch `AuctionConsole`, input 3 bidders with starting budgets, and add 2 auction items.
2. **Core MVP Bidding:** Walk through standard bidding, demonstrate out of budget rejections, invalid low bids, and passing turns.
3. **Change Feature (Bid Loop):** Demonstrate the multi-round Bid Loop in action as competing bidders circle the queue across multiple loops.
4. **Results & Test Execution:** Close the auction, display final winning results, and showcase passing JUnit test execution in the terminal.

---

## Attribution

| File | Who | How |
|---|---|---|
| `Node.java`, `AuctionQueue.java`, `Item.java`, `Bidder.java`, `AuctionSim.java` | Rohan & Austin | First draft written with GenAI (Claude) from our Deliverable 2 spec; reviewed, compiled and integrated by Rohan; Revised with Bid loop added for Deliverable 4 by Austin |
| `ModelDriver.java` | Rohan | Written with GenAI (Claude); run and checked by Rohan |
| `AuctionController.java` | Poorna | Drafted with GenAI (Claude), verified and written by Poorna.|
| `AuctionConsole.java` | Zara & Austin | Revised with Bid loop display added for Deliverable 4 by Austin | 
| `AuctionQueueTest.java`, `AuctionSimTest.java`, `BidderTest.java`, `ItemTest.java`  | Austin | Written and executed using JUnit 5 based on the Deliverable 2 test plan; revised for updated coverage and bad-input cases for Deliverable 4 |
| `README.md` | Team | Drafted and revised collaboratively with GenAI (Claude) and the team from our Deliverable 1, 2, and 3 documents and model code |

**GenAI note.** We did not use GenAI for Deliverables 1 or 2. For Deliverable 3 and 4 we used Claude (Anthropic) to write the first version of the five model classes from our Deliverable 2 spec, the model driver, and the first draft of this README. We also used it to help clean up some of the code and ensure that cases were covered for submission in Deliverable 4, according to the rubric. The team reviewed and ran the code and made the design decisions listed above.
