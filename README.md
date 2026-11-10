# Team 31 — Auction Simulator

CS 2114 · Project 3: Designing for Change · Deliverable 3 — First Prototype

A console auction simulator. Bidders with budgets take turns bidding on (or passing on) items that are auctioned off one at a time. The highest valid bid wins the item and the winner pays; if everyone passes, the item goes unsold. The program is split into model, view and controller (MVC), and the model is built on one generic singly linked queue (`AuctionQueue<T>`) that holds both the items and the bidders.

| Role | Classes |
|---|---|
| Model | `Node<T>`, `AuctionQueue<T>`, `Item`, `Bidder`, `AuctionSim` |
| View | `AuctionConsole` |
| Controller | `AuctionController` |

---

## How to run

All files are in one folder (default package). Java 11+ is needed (the model uses `String.isBlank()`).

**1. The model on its own (no view needed)**

```
javac Node.java AuctionQueue.java Item.java Bidder.java AuctionSim.java ModelDriver.java
java ModelDriver
```

`ModelDriver.java` is a tiny driver that builds two bidders and an item, places two bids, closes the item and prints the winner. It uses only the model classes. *(TODO: confirm the driver's file name and expected output, e.g. `Bea won at 40.0`.)*

**2. Tests**

*(TODO: describe how to run the tests, e.g. "Right-click `AuctionQueueTest.java` > Run As > JUnit Test", or `java SmokeTest` if you hand in the plain-Java smoke test. State which test files exist.)*

**3. The thin view**

```
javac *.java
java <class that contains main>
```

*(TODO: name the class with `main`, and give a 3-line example of what to type, e.g. create bidders, add items, start auction.)*

---

## Reflection

### What changed from our specification?

The five model classes and their public methods match the Deliverable 2 spec. The differences are below.

**Additions to the model (not in the spec).** The spec gave the controller no way to learn what happened after an item closed, and `closeCurrentItem()` returns `void`. So we added:

- `AuctionSim.isRoundActive()`, `isReadyToClose()`, `getLastClosedItem()` and `getClosedItems()`.
- `Bidder.addWonItem(Item)` and `getWonItems()`. Our class design said a Bidder has an "owned items list", but the Data & State section never listed the field.
- New private fields in `AuctionSim`: `turnsRemaining`, `lastClosedItem` and `closedItems`. The spec listed only `itemQueue`, `bidderQueue` and `currentActiveItem`.

*Why:* the controller needs the winner and price to call `showWinner()` and the full list to call `showResults()`. This keeps the controller from ever touching the queues.

**Queue rotation fix.** The spec said bidders rotate to the back of the queue after their turn. When we built it, we saw that if every bidder rotates once per item, the queue returns to the same order, so the same bidder would always go first. That defeated the fairness goal in our scope. `closeCurrentItem()` now rotates the queue one extra place, so with bidders A, B, C the order for the next item is B, C, A.

**Where validation lives.** The spec said the model decides what is an error and the view only displays it. We kept that:

- Constructors, `Item.recordBid`, `Bidder.deduct`, and the `AuctionSim` add/start/pass/close methods throw `IllegalArgumentException` or `IllegalStateException`.
- `AuctionSim.placeBid` returns `false` instead of throwing (as the spec says), and the same bidder keeps the turn so they can retry or pass.
- Parsing the text a user types (non-numeric, blank) stays in the controller. The model only ever receives a `double`.
- `Item.recordBid` repeats the same checks as `placeBid` on purpose, so it is safe even if it is called directly.

**Small decisions the spec did not cover.** Names and categories are trimmed of surrounding spaces. A blank preference is allowed (it could mean "no preference"), but a null one is rejected. A bid must be strictly above the current bid, which is the start price before anyone has bid, so a $20 bid on a $20 item is rejected (this matches our test plan).

**Test cases.** *(TODO: say which spec test rows are now actual tests, which are not yet written, and any changed expected results. Example: "We wrote a plain-Java smoke test covering most of the Deliverable 2 test plan rows; converting to JUnit is planned for Deliverable 4.")*

### Did the model really stand alone?

Yes. None of the five model classes imports, references or prints to the view or controller (no `System.out`, no `Scanner`). We ran the whole MVP path (add bidders and items, start an item, bid and pass, close, check budgets and winners) from a test driver with no view. Nothing was moved across the model/view line to make it run.

Two things to watch:

- Exception messages are written in the model as readable sentences so the controller can pass them straight to `showError()`. That is mild coupling to presentation, but the model does not know how or whether they are shown.
- The controller receives model objects (`Item`, `Bidder`) from `AuctionSim`. The view should only receive plain Strings and doubles. *(TODO: confirm this is true in our `AuctionConsole`/`AuctionController`.)*

### What happened to our unknowns?

| Unknown from our scope | Status |
|---|---|
| How users decide whether to raise the bid or pass | **Still open.** In the MVP a person types a bid or passes. Automatic decisions would need the attribute/preference stretch goal. |
| How to implement the Bid Loop and the number of loops | **Still open.** Not in the MVP. It is our planned change feature (see Next Steps). |
| What to do if everyone passes or nobody can afford an item | **Solved.** The item is unsold, budgets are unchanged, and the auction moves on. Zero-budget bidders can still pass. |
| How to rotate the queue fairly | **Solved.** Rotate each bidder to the back after their turn, plus one extra rotation when an item closes. |
| How to update money and track items won | **Solved.** `closeCurrentItem()` deducts the winning bid from the winner only and records the item in `Bidder.wonItems`. Closed items are kept in order in `AuctionSim`. |

### What doesn't work yet?

- **No Bid Loop.** Each bidder gets one turn per item, so an early bidder cannot respond if later bidders outbid them.
- **No automatic bidding.** Every bid is typed by a person. Category/preference exist on `Item` and `Bidder` but do not affect anything yet.
- **Money uses `double`**, so repeated deductions could show tiny rounding errors.
- **Duplicate bidder names are allowed** and not handled.
- *(TODO: list anything failing or missing in the controller/view and any untested bad-input cases, e.g. zero bidders at the console, blank bidder names, non-numeric bids.)*
- *(TODO: list missing JUnit tests, if any.)*

---

## Next Steps

### What is left to finish the MVP (in order)

1. *(TODO: adjust to the real status.)* Run the full MVP path through the controller and console: create bidders and items, auction every item, show the final results.
2. Check every bad-input case from our scope at the console: non-numeric or blank bids, bids too low or over budget, blank names, zero bidders or items, a zero-budget bidder who is given a turn.
3. Write JUnit tests for the model from the Deliverable 2 test plan.
4. Decide what to do about duplicate bidder names.
5. Clean up the results display (winner, price and budget left for each bidder).

### Our change feature: Bid Loop

Instead of one turn per bidder per item, the bidders are looped through repeatedly. A bidder who passes drops out of that item. The item closes when only one bidder is left or a maximum number of loops is reached. We will use a fixed, configurable maximum number of loops (default 3) instead of trying to calculate an "optimal" number, which was one of our unknowns.

This is non-trivial because it changes model state and logic: `AuctionSim` must track the current loop and which bidders are still in. A user reaches it through the controller and console.

**Prediction of what will change:**

| Class | Change? | Why |
|---|---|---|
| `AuctionSim` | **Yes** | `turnsRemaining` becomes loop/active-bidder tracking; a max-loops setting is added; the "item is finished" condition changes. We hope the public methods (`startNextItem`, `placeBid`, `passTurn`, `closeCurrentItem`, `isReadyToClose`) keep their signatures. |
| `AuctionController` | **Yes, small** | It keeps prompting until the model says the item is done, and it may pass the max-loops setting in. |
| `AuctionConsole` | **Maybe, small** | Possibly show the loop number or who dropped out. |
| `Node`, `AuctionQueue` | **No** | A queue that rotates is already enough. |
| `Item` | **No** | `recordBid` already accepts a series of higher bids. |
| `Bidder` | **No** | Budget deduction and won items are unaffected. |

We will compare this table with what actually happened in Deliverable 4.

### Stretch goals

- **Committing to:** Bid Loop (our change feature) and randomized item order (shuffle the items before they are added, so it should not touch the queue).
- **Dropping unless there is time:** preferences/categories affecting how much a bidder will spend (and the automatic bidding that goes with it), and showing the full item list to bidders before each iteration.

### New tests needed

- Bid Loop: item ends when one bidder remains; item ends at the max loop count; a bidder who passes is not asked again for that item; everyone passing in the first loop leaves the item unsold; a single bidder; a zero-budget bidder drops out immediately.
- Bid Loop bad input: max loops of zero or negative; a bid below the current bid in a later loop; a bidder bidding against themselves.
- Randomized order: all items are still auctioned exactly once, and an empty or single-item list works.
- Missing JUnit tests for the existing model methods (see "What doesn't work yet").

### Who does what

*(TODO: confirm with the team. This follows the Deliverable 2 division of work.)*

| Item | Owner |
|---|---|
| Bid Loop in `AuctionSim` and its tests | Rohan |
| Controller changes and bad-input handling | Poorna |
| Console changes and results display | Zara |
| Integration, end-to-end testing, README updates | Austin |

---

## Attribution

*(TODO: have each person check their own row before submitting.)*

| File | Who | How |
|---|---|---|
| `Node.java` | Rohan | First draft written with GenAI (Claude) from our Deliverable 2 spec; reviewed, compiled and integrated by Rohan |
| `AuctionQueue.java` | Rohan | Same as above |
| `Item.java` | Rohan | Same as above |
| `Bidder.java` | Rohan | Same as above |
| `AuctionSim.java` | Rohan | Same as above; the extra rotation at close and the added controller methods came out of that work and were reviewed by Rohan |
| `AuctionController.java` | Poorna | *(TODO: wrote / revised / GenAI-assisted?)* |
| `AuctionConsole.java` | Zara | *(TODO)* |
| `ModelDriver.java` | *(TODO)* | *(TODO)* |
| Test file(s) *(TODO: name them, e.g. `SmokeTest.java` or the JUnit files)* | *(TODO)* | Smoke test written with GenAI (Claude); *(TODO: who ran/reviewed it)* |
| `README.md` | Austin | Drafted with GenAI (Claude) from our Deliverable 1 and 2 documents and the model code; reviewed and revised by the team |

**GenAI note.** GenAI was not used for Deliverables 1 or 2 (our design documents). For Deliverable 3 we used Claude (Anthropic) to write the first version of the five model classes from our Deliverable 2 spec, to write a plain-Java smoke test of the model, and to help draft this README. The team reviewed and ran the code, and chose the design decisions listed under "What changed from our specification". *(TODO: add any other GenAI use for the controller, console or tests, or say there was none.)*
