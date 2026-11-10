# Team 31 — Auction Simulator

CS 2114 · Project 3: Designing for Change · Deliverable 3 — First Prototype

A console auction simulator. Bidders with budgets take turns bidding on items, one item at a time. The highest bid wins and the winner pays for the item. If everyone passes, the item will go unsold. One generic linked queue (`AuctionQueue<T>`) holds both the items and the bidders.

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

**Tests:** *(To complete by Deliverable 4)*

**Thin view:** *(To complete by Deliverable 3)*

---

## Reflection

### What changed from our specification?

- **Extra methods in the model.** The spec gave the controller no way to see the result of an item. We added `isRoundActive()`, `isReadyToClose()`, `getLastClosedItem()` and `getClosedItems()` to `AuctionSim`, and `addWonItem()` and `getWonItems()` to `Bidder`. The controller needs these to show winners and final results without touching the queues.
- **Fairer turn order.** The spec said each bidder moves to the back of the queue after their turn. But then the queue ends up in the same order after every item, so the same bidder always goes first. We added one extra rotation when an item closes, so a different bidder goes first next time.
- **Validation.** The model throws exceptions for bad data (blank names, negative money, bad bids). `placeBid` returns `false` instead of throwing, as the spec says, and the same bidder keeps their turn. Turning typed text into a number stays in the controller.
- **Small decisions.** A bid must be strictly higher than the current bid (the start price if there are no bids yet). Names are trimmed. A blank preference is allowed, but a null one is not.
- **Tests.** *(TODO: which spec test cases are written, which are not, and anything that changed.)*

### Did the model really stand alone?

Yes. The model classes never print, read input, or refer to the view or controller. We ran a full auction with `ModelDriver` and no view. Nothing had to be moved across the model/view line. Small note: the model writes its exception messages, which the controller passes to `showError()`.

### What happened to our unknowns?

- **Solved:** What if everyone passes? The item goes unsold and budgets stay the same.
- **Solved:** How to rotate fairly? Rotate after each turn, plus one extra rotation when an item closes.
- **Solved:** How to track money and items won? The winner is charged when the item closes, and the item is added to their won list.
- **Still open:** How a bidder decides to raise the bid or pass. In the MVP a person types it.
- **Still open:** The Bid Loop and how many loops to allow. This is our planned change feature.

### What doesn't work yet?

- No Bid Loop. Each bidder gets one turn per item, so early bidders can't respond to later bids.
- No automatic bidding. Category and preference exist but don't affect anything yet.
- Duplicate bidder names are allowed and not handled.

---

## Next Steps

### Finishing the MVP (in order)

1. Get the controller and console running the whole MVP path: add bidders and items, auction every item, show results.
2. Check every bad-input case at the console: non-numeric or blank bids, bids too low or over budget, blank names, no bidders or items.
3. Write JUnit tests for the model from our Deliverable 2 test plan.
4. Decide what to do about duplicate bidder names.

### Our change feature: Bid Loop

Bidders go around the queue repeatedly. A bidder who passes drops out of that item. The item ends when one bidder is left or a maximum number of loops is reached. We will use a fixed, adjustable maximum (default 3) instead of trying to find an "optimal" number.

**What we predict will change:**

| Class | Change? | Why |
|---|---|---|
| `AuctionSim` | Yes | It must track the current loop and who is still bidding. We hope its public method signatures stay the same. |
| `AuctionController` | Yes, small | It keeps prompting until the model says the item is finished. |
| `AuctionConsole` | Maybe, small | Possibly show the loop number. |
| `Node`, `AuctionQueue`, `Item`, `Bidder` | No | The queue already rotates, and `Item.recordBid` already accepts a series of higher bids. |

### Stretch goals

- **Doing:** Bid Loop (our change feature) and randomized item order.
- **Dropping:** Preferences changing how much a bidder spends, and showing all items to bidders before each round.

### New tests needed

- Bid Loop: ends when one bidder is left; ends at the max loops; a bidder who passes isn't asked again; everyone passing leaves the item unsold; one bidder only; zero-budget bidder drops out.
- Bad input: max loops of zero or negative; a too-low bid in a later loop.
- Randomized order: every item is auctioned exactly once; empty and one-item lists work.
- Any model tests still missing from the list above.

### Who does what

| Item | Owner |
|---|---|
| Bid Loop in `AuctionSim` and its tests | Rohan |

---

## Attribution

| File | Who | How |
|---|---|---|
| `Node.java`, `AuctionQueue.java`, `Item.java`, `Bidder.java`, `AuctionSim.java` | Rohan | First draft written with GenAI (Claude) from our Deliverable 2 spec; reviewed, compiled and integrated by Rohan |
| `ModelDriver.java` | Rohan | Written with GenAI (Claude); run and checked by Rohan |
| `AuctionController.java` | Poorna | |
| `AuctionConsole.java` | Zara |  | 
| Test file(s) *(TODO: name them)* | *(TODO)* | *(TODO)* |
| `README.md` | Team | Drafted with GenAI (Claude) from our Deliverable 1 and 2 documents and the model code; reviewed and revised by the team |

**GenAI note.** We did not use GenAI for Deliverables 1 or 2. For Deliverable 3 we used Claude (Anthropic) to write the first version of the five model classes from our Deliverable 2 spec, the model driver, and the first draft of this README. The team reviewed and ran the code and made the design decisions listed above.
