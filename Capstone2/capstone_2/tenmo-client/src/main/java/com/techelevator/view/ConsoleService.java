package com.techelevator.view;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetail;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	private static final String LINEBAR = "-------------------------------------------";
	private static final String SHORTLINE = "---------";

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public Double getUserInputDouble(String prompt) {
		Double result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Double.parseDouble(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void printTransferView(Transfer transfer, String detail) {

		out.printf(transfer.getTransferId() + "     %-30s" + "$" + transfer.getAmount() + "\n", detail);
	}

	public void printTransferDetails(TransferDetail transferDetail) {

		out.println();
		displayHeader("Transfer Details", "");
		out.println("Id: " + transferDetail.getTransferId());
		out.println("From: " + transferDetail.getFromName());
		out.println("To: " + transferDetail.getToName());
		out.println("Type: " + transferDetail.getTransferType());
		out.println("Status: " + transferDetail.getTransferStatus());
		out.println("Amount: $" + transferDetail.getAmount());
	}

	public void printRegisteredUsers(Map<Long, String> mapUser, Long currentUser) {

		displayHeader("Users", "ID          Name");
		for (Map.Entry<Long, String> user: mapUser.entrySet()) {
			if (!user.getKey().equals(currentUser)) {
				out.println(user.getKey() + "      " + user.getValue());
			}
		}
		out.println();
		out.println(SHORTLINE);
	}

	public void displayHeader(String header1, String header2) {
		out.println(LINEBAR);
		if (!header1.isEmpty()) {
			out.println(header1);
		}
		if (!header2.isEmpty()) {
			out.println(header2);
		}
		out.println(LINEBAR);
	}

	public void displayErrorMessage(String msg) {

		out.println(System.lineSeparator() + msg + System.lineSeparator());
	}

}
