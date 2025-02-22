package net.fightingpainter.mc.coined.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.types.Dict;
import net.fightingpainter.mc.coined.util.types.Pair;

public class PlayerBalance {
    private final Path filePath;
    private final String playerId;

    private Dict balance = new Dict(
        new Pair("copper", 0),
        new Pair("silver", 0),
        new Pair("gold", 0),
        new Pair("platinum", 0)
    );

    public PlayerBalance(String playerId, Path filePath) {
        this.filePath = filePath;
        this.playerId = playerId;
    }

    private void load() {
        try {
            if (Files.exists(filePath)) {//check if the file exists
                String json_string = new String(Files.readAllBytes(filePath));
                Dict allBalances = Dict.fromJson(json_string);
                balance = (Dict) allBalances.get(playerId, balance); //get the balance of the player or use the default balance
            } else { //if file doesn't exist, create it
                save();
            }

        } catch (IOException e) {
            Coined.LOGGER.error("There was an error when trying to load player the player balance data for player: " + playerId);
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            Dict allBalances = new Dict();
            if (Files.exists(filePath)) {//check if the file exists
                String json_string = new String(Files.readAllBytes(filePath));
                allBalances = Dict.fromJson(json_string); //get all the balances
            }
            allBalances.set(playerId, balance); //set the balance of the player in the all balances
            Coined.LOGGER.info(allBalances.toJson());
            // Files.write(filePath, allBalances.toJson(4).getBytes()); //write the all balances to the file
        } catch (IOException e) {
            Coined.LOGGER.error("There was an error when trying to save the player balance data for player: " + playerId);
            e.printStackTrace();
        }
    }
    

    /**
     * Gets the balance of a player as a Money object
     * @return
    */
    public Money getBalance() {
        load(); //load the data
        return Money.fromDict(balance); //return the balance of the player
    }

    /**
     * Sets the balance of a player
     * @param newBalance The new balance of the player
    */
    public void setBalance(Money newBalance) {
        load(); //load the data
        this.balance = newBalance.toDict(); //set the balance of the player
        save(); //save the data
    }


}
