# Coined

Currency and purse mod for Minecraft 1.21.1 / NeoForge 21.1.214. Shop blocks remain unfinished.

## Using the purse

Open your survival inventory and click the purse button. Deposit All deposits loose currency and filled money bags from your inventory, hotbar, and offhand. Right-clicking currency also deposits it, consuming it in creative mode too.

Enter a value in copper units and withdraw to receive money bags. Hold Shift while clicking Withdraw for loose coins, using the highest denominations first. Bags hold up to 255 of each denomination; excess currency splits into more bags. Items that do not fit drop at your feet. A transaction may create at most 4096 stacks; larger requests are rejected without debiting the balance.

Each silver is 100 copper; gold is 10,000 copper; platinum is 1,000,000 copper. The +/- buttons adjust the selected amount; Shift adjusts ten coins at once. Invalid withdrawals are disabled. Hover Withdraw for guidance.

The Auto-pickup switch deposits collected currency directly, even with a full inventory. It defaults to OFF and persists through logout and death. Pickup delays and item ownership still apply.

## Death

Balances persist through death and are synchronized on respawn and dimension changes.

`/gamerule BalanceDropPercentage 25` drops 25% of a player's purse as money bags on death. Default: 0. Values clamp to 0–100. Fractional copper rounds down; the remainder stays in the purse. This rule applies independently of keepInventory. Extreme command-created balances use larger bags to avoid spawning millions of entities.

## Mob currency

Entity-type tags `coined:common`, `coined:uncommon`, `coined:miniboss`, and `coined:boss` supply default vanilla mob groups and can be extended in datapacks under `data/<namespace>/tags/entity_type/`. Individual mobs can also be assigned a tier with `/tag <entity> add coined:boss` (plain `boss`, etc. also works).

| Tier | Drop range, copper value |
| --- | --- |
| common | 1–50 |
| uncommon | 100–500 |
| miniboss | 1,000–5,000 |
| boss | 10,000–50,000 |

Ranges are inclusive. Only the highest matching tier drops currency. Drops respect doMobLoot; untagged mobs drop no currency. All tagged mob deaths qualify, including environmental deaths.

## Development

Java 21 is required by the toolchain. Build: `./gradlew build`. Run the dedicated headless currency tests: `./gradlew runGameTestServer`.

Tests cover denomination splitting, capacity boundaries, bag merging, component-copy safety, extreme values, inventory/offhand deposits, withdrawals, full-inventory auto-pickup, and death-drop rounding. Client visuals and actual respawn/reconnect flows still need an in-game playtest.
