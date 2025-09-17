package net.fightingpainter.mc.coined.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.types.Dict;

public class PlayerBalance {
    // private final Path filePath;
    // private final String playerId;

    // private Money balance = new Money(0l); //the balance of the player

    // public PlayerBalance(String playerId, Path filePath) {
    //     this.filePath = filePath;
    //     this.playerId = playerId;
    // }

    // private void load() {
    //     try {
    //         if (Files.exists(filePath)) {//check if the file exists
    //             String json_string = new String(Files.readAllBytes(filePath));
    //             Dict allBalances = Dict.fromJson(json_string);
    //             Dict balanceBalance = (Dict) allBalances.get(playerId, balance.toDict()); //get the balance of the player or use the default balance
    //             balance = Money.fromDict(balanceBalance); //set the balance of the player
    //         } else { //if file doesn't exist, create it
    //             save();
    //         }

    //     } catch (IOException e) {
    //         Coined.LOGGER.error("There was an error when trying to load player the player balance data for player: " + playerId);
    //         Coined.LOGGER.error(e.getMessage());
    //         Coined.LOGGER.error(e.getStackTrace().toString());
    //     }
    // }

    // private void save() {
    //     try {
    //         Dict allBalances = new Dict();
    //         if (Files.exists(filePath)) {//check if the file exists
    //             String json_string = new String(Files.readAllBytes(filePath));
    //             allBalances = Dict.fromJson(json_string); //get all the balances
    //         }
    //         allBalances.set(playerId, balance.toDict()); //set the balance of the player in the all balances
    //         Files.write(filePath, allBalances.toJson(4).getBytes()); //write the all balances to the file
    //     } catch (IOException e) {
    //         Coined.LOGGER.error("There was an error when trying to save the player balance data for player: " + playerId);
    //         Coined.LOGGER.error(e.getMessage());
    //         Coined.LOGGER.error(e.getStackTrace().toString());
    //     }
    // }
    

    // /**
    //  * Gets the balance of a player as a Money object
    //  * @return
    // */
    // public Money getBalance() {
    //     load(); //load the data
    //     return balance; //return the balance of the player
    // }

    // /**
    //  * Sets the balance of a player
    //  * @param newBalance The new balance of the player
    // */
    // public void setBalance(Money newBalance) {
    //     load(); //load the data
    //     this.balance = newBalance; //set the balance of the player
    //     save(); //save the data
    // }

    // /**
    //  * Adds an amount to the balance of a player
    //  * @param amount The amount to add
    // */
    // public void addBalance(Money amount) {
    //     load(); //load the data
    //     this.balance = this.balance.add(amount); //add the amount to the balance of the player
    //     save(); //save the data
    // }

    // /**
    //  * Subtracts an amount from the balance of a player
    //  * @param amount The amount to subtract
    // */
    // public void subtractBalance(Money amount) {
    //     load(); //load the data
    //     this.balance = this.balance.subtract(amount); //subtract the amount from the balance of the player
    //     save(); //save the data
    // }
}
