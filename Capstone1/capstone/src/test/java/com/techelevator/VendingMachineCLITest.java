package com.techelevator;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VendingMachineCLITest extends TestCase {

    @Test
    public void testPrintCurrentBalance() {
        VendingMachineCLI.setCurrentBalance(5);
        String actualValue = VendingMachineCLI.printCurrentBalance();
        String expectedValue = "Current balance: $5.00\n";

        //
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGiveChange() {
        VendingMachineCLI.setCurrentBalance(4.40);
        String actualValue = VendingMachineCLI.giveChange();
        String expectedValue = "dimes:1 penny:0 nickels:1 quarters:17";
        //
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testRestock() {

        VendingMachineCLI.restock("src\\test\\java\\com\\techelevator\\testvending.csv");

        Item jasper = new Item(new String[] {"D1","Jasper","100","Chip"});
        Item notJasper = new Item(new String[] {"R8","Not Jasper","145","Chip"});
        String actualValue = Item.itemArrayList.get(0).getName();
        String expectedValue = jasper.getName();



        Assert.assertEquals(expectedValue, actualValue);
    }
}