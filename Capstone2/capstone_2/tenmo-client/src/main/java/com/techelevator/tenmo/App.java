package com.techelevator.tenmo;

import com.techelevator.tenmo.Util.BasicLogger;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private static final String TRANSFER_MENU_OPTION_APPROVE = "Approve";
	private static final String TRANSFER_MENU_OPTION_REJECT = "Reject";
	private static final String TRANSFER_MENU_OPTION_CANCEL = "Don't approve or reject";
	private static final String[] TRANSFER_MENU_OPTIONS = {TRANSFER_MENU_OPTION_APPROVE, TRANSFER_MENU_OPTION_REJECT, TRANSFER_MENU_OPTION_CANCEL};

    private static final String toPrefix = "To: ";
    private static final String fromPrefix = "From: ";
    private Map<Long, String> mapUser;

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;
    private UserService userService = new UserService();

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		BasicLogger.log("\nSTART");
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {

        try {
            System.out.println("Your current account balance is: $" + accountService.getBalance());

			BasicLogger.log(currentUser.getUser().getId() + " viewed current balance.");
		} catch (AccountServiceException e) {
			System.out.println("Error accessing account: " + formatErrorMessage(e.getMessage()));
			BasicLogger.log("Error encountered while viewing current balance. " + e.getMessage());
		}
	}

	private void viewTransferHistory() {
        Transfer[] transfers = null;
		Map<Long, Transfer> mapTransfer = new HashMap<>();

        try {
			Long userAccountId = getAccountIdByUserIdFromAccounts(Long.valueOf(currentUser.getUser().getId()));
			transfers = transferService.getTransfersByUserId(Long.valueOf(currentUser.getUser().getId()));

			if (transfers.length != 0) {
				console.displayHeader("Transfers", "ID       From/To                       Amount");
				for (Transfer transfer : transfers) {
					String detail = null;
					Account account = null;
					if (transfer.getAccountFrom() != userAccountId) {
						detail = fromPrefix;
						account = getAccountDetails(Long.valueOf(transfer.getAccountFrom()));
					} else {
						detail = toPrefix;
						account = getAccountDetails(Long.valueOf(transfer.getAccountTo()));
					}
					//get username from existing users map
					if (mapUser.containsKey(account.getUserId())) {
						detail += mapUser.get(account.getUserId());
					}
					console.printTransferView(transfer, detail);

					//utilize the foreach, create hashmap here for faster search.
					mapTransfer.put(transfer.getTransferId(), transfer);
				}


				TransferDetail transferDetail = new TransferDetail();
				long transferIdChoice = 0;
				System.out.println("---------");
				do {
					transferIdChoice = Long.valueOf(console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)"));
					if (mapTransfer.containsKey(transferIdChoice) && transferIdChoice != 0) {
						Account account = null;
						transferDetail.setTransferId(mapTransfer.get(transferIdChoice).getTransferId());
						transferDetail.setTransferStatus(mapTransfer.get(transferIdChoice).getTransferStatusDesc());
						transferDetail.setTransferType(mapTransfer.get(transferIdChoice).getTransferTypeDesc());
						account = getAccountDetails(Long.valueOf(mapTransfer.get(transferIdChoice).getAccountFrom()));
						transferDetail.setFromName(mapUser.get(account.getUserId()));
						account = getAccountDetails(Long.valueOf(mapTransfer.get(transferIdChoice).getAccountTo()));
						transferDetail.setToName(mapUser.get(account.getUserId()));
						transferDetail.setAmount(mapTransfer.get(transferIdChoice).getAmount());
						console.printTransferDetails(transferDetail);
						BasicLogger.log(transferIdChoice + " was viewed successfully by user id " + currentUser.getUser().getId());
						break;
					} else if (transferIdChoice == 0) {
						break;
					} else {
						console.displayErrorMessage("*** Transfer ID not found ***");
						BasicLogger.log("Error encountered while viewing transfer history.");
					}
				} while (transferIdChoice != 0);
			} else {
				System.out.println("*** No transfer history to display. ***");
			}
        } catch(TransferServiceException e){
        	System.out.println("Error accessing transfers: " + formatErrorMessage(e.getMessage()));
        	BasicLogger.log("Error encountered while displaying transfer history. " + e.getMessage());
        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		Transfer[] transfers = null;
		Map<Long, Transfer> mapTransfer = new HashMap<>();

		try {
			transfers = transferService.getPendingRequests(Long.valueOf(currentUser.getUser().getId()));

			//make sure to display if transfers has records
			if (transfers.length != 0) {
				console.displayHeader("Pending Transfers", "ID          To                     Amount");
				for (Transfer transfer : transfers) {
					String detail = null;
					Account account = null;
					account = getAccountDetails(Long.valueOf(transfer.getAccountFrom()));
					//get username from existing users map
					if (mapUser.containsKey(account.getUserId())) {
						detail = mapUser.get(account.getUserId());
					}
					console.printTransferView(transfer, detail);

					//utilize the foreach, create hashmap here for faster search.
					mapTransfer.put(transfer.getTransferId(), transfer);
				}


				TransferDetail transferDetail = new TransferDetail();
				long transferIdChoice = 0;
				System.out.println("---------");
				do {
					transferIdChoice = Long.valueOf(console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel)"));
					if (mapTransfer.containsKey(transferIdChoice) && transferIdChoice != 0) {
						//display request transfer menu
						while (true) {
							String choice = (String) console.getChoiceFromOptions(TRANSFER_MENU_OPTIONS);
							if (TRANSFER_MENU_OPTION_APPROVE.equals(choice)) {
								//deduct money from own account
								//add money to recipient account
								//update transfer to 'Approve'
								transferService.approveRequestTransaction(transferIdChoice);
								System.out.println("Request transaction approved for Transfer Id: " + transferIdChoice);
								BasicLogger.log(transferIdChoice + " request was approved by " + currentUser.getUser().getId());
								transferIdChoice = 0;
								break;
							} else if (TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
								//update transfer record where status will become 'Rejected'
								transferService.rejectRequestTransaction(transferIdChoice);
								System.out.println("Request transaction rejected for Transfer Id: " + transferIdChoice);
								BasicLogger.log(transferIdChoice + " request was rejected by " + currentUser.getUser().getId());
								transferIdChoice = 0;
								break;
							} else {
								transferIdChoice = 0;
								break;
							}
						}
					} else if (transferIdChoice == 0) {
						break;
					} else {
						console.displayErrorMessage("*** Transfer ID not found ***");
						BasicLogger.log("Error encountered while processing request transaction.");
					}
				} while (transferIdChoice != 0);
			} else {
				System.out.println("*** No pending requests to display. ***");
			}

		} catch (TransferServiceException e) {
			System.out.println("Error accessing transfers: " + formatErrorMessage(e.getMessage()));
			BasicLogger.log("Error encountered while processing request transaction. " + e.getMessage());
		}
	}

	private void sendBucks() {
        boolean isValid = false;
        long sendToUserId = 0;
        double amount = 0.00;
        Long accountId = null;
        Account senderAccount = null;
        Account receiverAccount = null;

        //get sender's Account details
        accountId = getAccountIdByUserIdFromAccounts(Long.valueOf(currentUser.getUser().getId()));
        senderAccount = getAccountDetails(accountId);

        //print all registered users
        //make sure to skip printing current user
        console.printRegisteredUsers(mapUser, Long.valueOf(currentUser.getUser().getId()));

        do {
            sendToUserId = Long.valueOf(console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)"));
            if (mapUser.containsKey(sendToUserId) && sendToUserId != 0) {
                amount = console.getUserInputDouble("Enter amount");
                if (senderAccount.getBalance() >= amount) {
                    accountId = getAccountIdByUserIdFromAccounts(sendToUserId);
                    receiverAccount = getAccountDetails(accountId);
                    isValid = true;
                    break;
                } else {
                    console.displayErrorMessage("*** Balance not enough for sending. ***");
                }
            } else if (sendToUserId == 0) {
				break;
			} else {
            	console.displayErrorMessage("*** Enter valid User Id or 0 to cancel. ***");
			}
        } while (sendToUserId != 0);

        //deduct money from own account
        //add money to recipient account
        //create record in transfer table with initial status of "approve"
        if (isValid) {
            try {

                TransactionData transactionData = new TransactionData();
                transactionData.setReceiverId(sendToUserId);
                transactionData.setAmount(amount);
                transferService.processSendTransaction(Long.valueOf(currentUser.getUser().getId()), transactionData);
				System.out.println("Amount successfully sent to user " + sendToUserId);
                BasicLogger.log(senderAccount.getUserId() + " sent " + amount + " to " + receiverAccount.getUserId());
            } catch (TransferServiceException e) {
                System.out.println("Error updating transfer service for sending: " + formatErrorMessage(e.getMessage()));
				BasicLogger.log("Error encountered while sending money. " + e.getMessage());
            }
        }
    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		boolean isValid = false;
		long sendToUserId = 0;
		double amount = 0.00;
		Long accountId = null;
		Account senderAccount = null;
		Account receiverAccount = null;

		//get sender's Account details
		accountId = getAccountIdByUserIdFromAccounts(Long.valueOf(currentUser.getUser().getId()));
		senderAccount = getAccountDetails(accountId);

		//print all registered users
		//make sure to skip printing current user
		console.printRegisteredUsers(mapUser, Long.valueOf(currentUser.getUser().getId()));

		do {
			sendToUserId = Long.valueOf(console.getUserInputInteger("Enter ID of user you are requesting to (0 to cancel)"));
			if (mapUser.containsKey(sendToUserId) && sendToUserId != 0) {
				amount = console.getUserInputDouble("Enter amount");
				accountId = getAccountIdByUserIdFromAccounts(sendToUserId);
				receiverAccount = getAccountDetails(accountId);
				isValid = true;
				break;
			} else if (sendToUserId == 0) {
				break;
			} else {
				console.displayErrorMessage("*** Enter valid User Id or 0 to cancel. ***");
			}
		} while (sendToUserId != 0);

		//create record in transfer table with initial status of "pending"
		if (isValid) {
			try {
				//populate fields for new transfer
				Transfer newTransfer = new Transfer();
				newTransfer.setTransferTypeDesc("Request");
				newTransfer.setTransferStatusDesc("Pending");
				newTransfer.setAccountFrom(Integer.valueOf(String.valueOf(senderAccount.getAccountId())));
				newTransfer.setAccountTo(Integer.valueOf(String.valueOf(receiverAccount.getAccountId())));
				newTransfer.setAmount(amount);
				transferService.createTransfer(newTransfer);
				System.out.println("Request transfer sent to user Id " + sendToUserId);
				BasicLogger.log(senderAccount.getUserId() + " requested " + amount + " to " + receiverAccount.getUserId());
			} catch (TransferServiceException e) {
				System.out.println("Error updating transfer service for request: " + formatErrorMessage(e.getMessage()));
				BasicLogger.log("Error encountered while requesting money. " + e.getMessage());
			}
		}
	}
	
	private void exitProgram() {
    	BasicLogger.log("END" + "\n");
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");

            	BasicLogger.log("New user registered. Welcome " + credentials.getUsername());
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+ formatErrorMessage(e.getMessage()));
				System.out.println("Please attempt to register again.");

				BasicLogger.log("Registration failure");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				accountService.setAuthToken(currentUser.getToken());
				transferService.setAuthToken(currentUser.getToken());

				BasicLogger.log("logged in as " + currentUser.getUser().getUsername());
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+ formatErrorMessage(e.getMessage()));
				System.out.println("Please attempt to login again.");

				BasicLogger.log("Login failure");
			}
		}

		User[] users = userService.getUserName(API_BASE_URL);
		getAllUsersForReference(users);
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	private void printTransfers(Transfer[] transfers) {

    	for (Transfer transfer : transfers) {
			System.out.println(transfer.getTransferId() + "     ");
			System.out.println(transfer.getAmount());
		}
	}

	private void getAllUsersForReference(User[] users) {

		mapUser  = new HashMap<>();
    	for (User user: users) {
			mapUser.put(Long.valueOf(user.getId()), user.getUsername());
		}
	}

	private Account getAccountDetails(Long accountId) {

    	Account account = null;

    	try {
			account = accountService.getAccountByAccountId(accountId);
		} catch (AccountServiceException e) {
			System.out.println("Error accessing Account To: " + formatErrorMessage(e.getMessage()));
            BasicLogger.log("Error encountered while accessing account details. Method: getAccountDetails " + e.getMessage());
		}

    	return account;
	}

	private Long getAccountIdByUserIdFromAccounts(Long userId) {

        Long userAccountId = null;

        try {
            userAccountId = accountService.getAccount(userId).getAccountId();
        } catch (AccountServiceException e) {
            System.out.println("Error retrieving Account Id from Accounts: " + formatErrorMessage(e.getMessage()));
            BasicLogger.log("Error encountered while accessing account details. Method: getAccountIdByUserIdFromAccounts " + e.getMessage());
        }

        return userAccountId;
    }

    private String formatErrorMessage(String message) {

		String[] msg = message.split(",");
		return msg[2].substring(msg[2].lastIndexOf(":") + 1);
	}
}
