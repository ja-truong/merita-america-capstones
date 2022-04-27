package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static com.techelevator.Item.itemArrayList;
import static java.lang.Double.parseDouble;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCTS = "Select Products";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCTS ,PURCHASE_MENU_OPTION_FINISH_TRANSACTION};

	private boolean running = true;

	public static void setCurrentBalance(double currentBalance) {
		VendingMachineCLI.currentBalance = currentBalance;
	}

	private static double currentBalance = 0.00;

	public static String printCurrentBalance() {
		return ("Current balance: $" + String.format("%.2f", currentBalance)+ "\n");
	}
	public static void subtractMoney(double amount){
		currentBalance = (currentBalance - amount);
	}
	private static final double valueOfPENNY = 0.01;
	private static final double valueOfNICKLE = 0.05;
	private static final double valueOfDIME = 0.10;
	private static final double valueOfQUARTER = 0.25;
    private static final int STARTING_STOCK = 5;

	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (running) {

			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);


			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {////////////////////////////////////
				System.out.println(Item.displayItems());


			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {////////////////////////////////////

				boolean purchaseMenu = true;
				while (purchaseMenu) {

					choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);


					if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
						feedMoney();


					} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCTS)) {
						System.out.println(Item.displayItems());
						System.out.println("Please Choose an item.");
						Scanner scanner = new Scanner(System.in);
						String userInput = scanner.nextLine();
						System.out.println(Item.dispense(userInput, currentBalance));


					} else if (choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
						//needs to give back change
						//make variables to store how many coins of each type to give back
						//while current balamce < 0
							//check if current Balance is more than valueOfQuarter
								//if so, add one to the amount of quarters to return and subtract valueOfQuater from currentBalance
							//repeat for all coins
						//print out how many coins of each type are returned
						//needs to log this transaction
						purchaseMenu = false;
						System.out.println(giveChange());
					}
				}


			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {//////////////////////////////////////
				running = false;
				System.out.println("Please do have a nice day!:)");
				log("EXIT");
			}
		}
	}

	public void feedMoney() {
		boolean userInsertingMoney = true;
		while (userInsertingMoney) {

			double initialMoney = currentBalance;
			System.out.println("Insert $1/$2/$5/$10/$20\ntype exit to stop giving money");
			Scanner scanner = new Scanner(System.in);
			String userInput = scanner.nextLine();
			if (
					userInput.equals("1") || userInput.equals("2" )||
					userInput.equals("5") || userInput.equals("10") || userInput.equals("20"))
			{
				currentBalance += parseDouble(userInput);
			} else if (userInput.equalsIgnoreCase("exit")){
				userInsertingMoney = false;
			} else {
				System.out.println("Thats not a real dollar bill!");
			}
			System.out.println(printCurrentBalance());
			log("FEED MONEY", initialMoney, currentBalance);
		}
	}

	public static String giveChange() {
		int penny =0;
		int nickels = 0;
		int dimes = 0;
		int quarters = 0;
		double change = currentBalance;
		while(currentBalance > 0){
			if (currentBalance >= valueOfQUARTER) {
				quarters ++;
				currentBalance -= valueOfQUARTER; }
			else if(currentBalance >= valueOfDIME){
				dimes ++;
				currentBalance -= valueOfDIME;
			} else if (currentBalance >= valueOfNICKLE) {
				nickels ++;
				currentBalance -= valueOfDIME;
			} else if (currentBalance >= valueOfPENNY) {
				penny ++;
				currentBalance -= valueOfPENNY;
			}else {
				currentBalance = 0;
			}
		}
		log("GIVE CHANGE", change, currentBalance);
		return ("dimes:"+ dimes + " penny:" + penny +" nickels:" + nickels +" quarters:" + quarters);
	}

	public static void restock(String restockPath){
		File inputFile = new File(restockPath);
		try (Scanner datainput = new Scanner(inputFile)){
			while(datainput.hasNext()) {
			//for( String items : datainput)
				String items = datainput.nextLine();
				//System.out.println(items);
				String[] itemStock = items.split("\\|");
				Item vitems = new Item(itemStock);
                itemArrayList.add(vitems);
                vitems.setStock(STARTING_STOCK);
			}

		} catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	public static void log(String transaction,  double initialMoney, double finalMoney){
		String logpath = "log.txt";
		File logFile = new File(logpath);
		try(PrintWriter log = new PrintWriter(new FileOutputStream(logFile,true))){
			try {
				log.print(LocalDateTime.now().format(
						DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")));
				log.print(": " + transaction);
				log.println(" $" + String.format("%.2f", initialMoney) + " $" + String.format("%.2f", finalMoney));
				//System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:ss")));

			} catch (DateTimeException e) {
				System.err.println("There is a problem with your temporal location, please see a local time traveler for help");
			}

		}catch (FileNotFoundException e){
			//throw(FileNotFoundException e);
			System.out.println("File not found.:(");

		}

	}

	public static void log(String message){
		String logpath = "log.txt";
		File logFile = new File(logpath);
		try(PrintWriter log = new PrintWriter(new FileOutputStream(logFile,true))){
			try {
				log.print("\n" + LocalDateTime.now().format(
						DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")) + ": ");
				log.println(message);
			} catch (DateTimeException e) {
				System.err.println("There is a problem with your temporal location, please see a local time traveler for help");
			}

		}catch (FileNotFoundException e){
			//throw(FileNotFoundException e);
			System.out.println("File not found.:(");

		}

	}

	public static void main(String[] args){

		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		restock("vendingmachine.csv");
		log("START");
		//make menu
		cli.run();
	}

}