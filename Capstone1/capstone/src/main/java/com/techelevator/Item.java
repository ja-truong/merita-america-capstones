package com.techelevator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.techelevator.VendingMachineCLI.log;
import static com.techelevator.VendingMachineCLI.subtractMoney;
import static java.lang.Double.parseDouble;

public class Item {

    //need new class that has variables for string selection, string name, double cost, string type
private String name;
private String type;
private double cost;
private String selection;
private int stock;

    public static List<Item> itemArrayList = new ArrayList<>();

    public static String displayItems() {
        String display = "";
        for (Item item : itemArrayList) {
            //System.out.println(item.getName());
             display += (item.getSelection() +", " + item.getName() + ",\t$" + item.getCost() +
            (item.stock == 0 ? "\tOut of Stock":" \tcurrent stock:" + item.getStock()) + "\n");
        }
        return display;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getCost() {
        return cost;
    }

    public String getSelection() {
        return selection;
    }

    public Item(String[] input) {
       selection = input[0];
       name = input[1];
       cost = parseDouble(input[2]);
       type = input[3];
    }
    public int getStock(){
        return stock;
    }

    public static String dispense (String choice,double amountMoney) {
        //dispense needs to update the stock and subtract from the currentBalance the item.cost

        double initialMoney = amountMoney;
        if(choice.length() == 2) {

          //  choice = choice.substring(0,1).toUpperCase() + choice.substring(1);
            for (Item item : itemArrayList) {
                if (choice.equalsIgnoreCase(item.getSelection())) {
                    if(item.getStock() > 0){
                         if(amountMoney >= item.getCost()) {
                             item.stock --;
                             subtractMoney(item.getCost());
                             amountMoney-=item.getCost();
                             log(item.getName(), initialMoney, amountMoney);
                             if (item.getType().equals("Chip")) {
                                 return "Crunch,Crunch,Yum!";
                             }
                             if (item.getType().equals("Candy")) {
                                 return "Munch,Munch,Yum!";
                             }
                             if (item.getType().equals("Drink")) {
                                 return "Glug,Glug,Yum!";
                             }
                             if (item.getType().equals("Gum")) {
                                 return "Chew,Chew,Yum!";
                             }
                         }else{
                             log("Haha this dude's broke");
                             return("Sorry you don't have enough money:(");
                         }
                    } else {
                        log("This fat person ate all the " + item.getName() + " lol");
                        return ("Sorry! Item out of Stock:(");

                    }
                }
            }
        }
        return "That is not a valid selection.";
    }
//constructor in items takes the varriables as a array and sets each index to a different var
}
