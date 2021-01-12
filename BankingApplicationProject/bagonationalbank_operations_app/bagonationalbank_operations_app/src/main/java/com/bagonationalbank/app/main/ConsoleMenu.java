package com.bagonationalbank.app.main;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Employee;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transaction;
import com.bagonationalbank.app.model.Username;
import com.bagonationalbank.app.service.BankingOperationsService;
import com.bagonationalbank.app.service.impl.BankingOperationsServiceImpl;

import jdk.internal.org.jline.utils.Log;

public class ConsoleMenu {

	private static Logger log = Logger.getLogger(ConsoleMenuMain.class);
	Scanner sc = new Scanner(System.in);
	private BankingOperationsService bankingOperationsService = new BankingOperationsServiceImpl();
	
	public Account createNewAccount (Customer customer) {
		Account account = null;

		log.info("\nLet's get an account set up for you!");
		log.info("=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*");		

		log.info("\nWould you like to open a: ");
		log.info("1) Checking account");
		log.info("2) Savings account");
		log.info("3) EXIT");

		int accountTypeChoice = 0;
		String accountType = null;
		
		try {
			accountTypeChoice = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return null;
		}

		if (accountTypeChoice == 1) {
			accountType = "checking";
		} else if (accountTypeChoice == 2) {
			accountType = "savings";
		} else if (accountTypeChoice == 3) {  
			return null;
		} else {
			log.info("Invalid menu option");
			return null;
		}
		
		log.info("How much would you like to initially deposit?");
		String depositStr = sc.nextLine();
		double deposit = Double.parseDouble(depositStr);
		
		try {
			account = bankingOperationsService.createNewAccount(customer, deposit, accountType);
			
			if (account != null) {
				log.info("Your " + account.getAccountType() + " account has been created successfully and is awaiting approval. Your account ID is " + account.getAccountId() + ".");
			} else {
				log.info("Account creation failed. Please contact the bank.");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());		
		}
		return account;
	}

	public Customer createNewCustomer () {
		log.info("\nWELCOME TO BAGO NATIONAL BANK!");
		log.info("=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*");
		boolean success = true;
	
		String firstName;
		do {
			log.info("\nPlease enter your First Name: ");
			firstName = sc.nextLine();
			success = bankingOperationsService.isValidString(firstName, 1, 15, true, true);
			if (!success) {
				log.info("\nInvalid First Name! Please Retry.");
			}
		} while (!success);

		String lastName;
		do {
			log.info("\nPlease enter your Last Name: ");
			lastName = sc.nextLine();
			success = bankingOperationsService.isValidString(lastName, 1, 15, true, true);
			
			if (!success) {
				log.info("\nInvalid Last Name! Please Retry.");
			}
			
		} while (!success);

		
		String gender;
			do {
				log.info("Please enter your Gender (M/F): ");  
				gender = sc.nextLine().toUpperCase();
			} while (bankingOperationsService.isValidGender(gender) == false);
		  

		//log.info("Please enter your date of birth (MM/DD/YYYY): ");
		//String dob = sc.nextLine();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		//dateFormat.setLenient(false);
			
		String dob;	
		Date dob2 = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		dateFormat.setLenient(false);
		
		do {
			log.info("Please enter your date of birth (MM/DD/YYYY): ");
			dob = sc.nextLine();
			success = bankingOperationsService.isValidDate(dob);
			
			if (!success) {
				log.info("Bad date format");
			}

			try {
				dob2 = dateFormat.parse(dob);
			} catch (ParseException e) {
				success = false;
				log.info("Invalid date... Please try again!");              //is this correct   
			}

		} while (!success);
			


		
		

		String address1;
		do {
			log.info("Please enter your address 1: ");  //alphanumeric
			address1 = sc.nextLine();
		} while (bankingOperationsService.isValidString(address1, 5, 45, true, false) == false);

		String address2;
		do {
			log.info("Please enter your address 2: ");
			address2 = sc.nextLine();
		} while (bankingOperationsService.isValidString(address2, 5, 45, false, false) == false);

		String city;
		do {
			log.info("Please enter your city: ");
			city = sc.nextLine();
		} while (bankingOperationsService.isValidString(city, 3, 20, true, true) == false);

		
		String state;
		do {
			log.info("Please enter your state (Use abbreviated State Name e.g. use AZ for Arizona): ");
			state = sc.nextLine().toUpperCase();
		} while (bankingOperationsService.isValidString(state, 2, 2, true, true) == false);

		
		String zip5;
		do {
			log.info("Please enter your zip 5: ");
			zip5 = sc.nextLine();
		} while (bankingOperationsService.isValidNumber(zip5, 5, 5, true) == false);

		
		String zip4;
		do {
			log.info("Please enter your zip 4 (write null if you don't have one): ");                 //rewrite message
			zip4 = sc.nextLine();
		} while (bankingOperationsService.isValidNumber(zip4, 4, 4, false) == false);

		String phone1;
		do {
			log.info("Please enter your primary phone number (10 digits, no dashes): ");           
			phone1 = sc.nextLine();
		} while (bankingOperationsService.isValidNumber(phone1, 10, 10, true) == false);

		
		String phone2;
		do {
			log.info("Please enter your secondary phone number (10 digits, no dashes- write null if you don't have one): ");
			phone2 = sc.nextLine();
		} while (bankingOperationsService.isValidNumber(phone2, 10, 10, false) == false);
	
		log.info("Please enter your email address: ");
		String email = sc.nextLine();
		
		String usernameStr;
		do {
			success = true;
			
			log.info("Username Requirements");
			log.info("\nMust start with an alphanumeric character, only underscore and hyphen allowed, min length = 7, max length = 20");
			log.info("\nPlease enter the username you'd like to use: ");   
		
			usernameStr = sc.nextLine();
			
			if (!bankingOperationsService.isValidCredentials(usernameStr, 7, 20, true)) {
				log.info("\nUsername not valid. Please follow the username requirements."); 
				success = false;
			}
			
			try {
				if (bankingOperationsService.isUsername(new Username(usernameStr))) {
					log.info("\nSorry, that username already exists. Please try something else."); 
					success = false;
				}
			} catch (BusinessException e) {
				log.info(e.getMessage());
			}
		} while (!success);

		String pinStr;
		do {
			log.info("Pin Requirements");
			log.info("\nMust start with an alphanumeric character, only underscore and hyphen allowed, min length = 7, max length = 20");
			log.info("\nPlease enter the pin you'd like to use: ");                               //length do u use pin or password
			pinStr = sc.nextLine();
		} while (bankingOperationsService.isValidCredentials(pinStr, 7, 20, true) == false);

		Customer customer = new Customer (firstName, lastName, gender, dob2, address1, address2, city, state, zip5, zip4, phone1, phone2, email);
		Username username = new Username (usernameStr, customer); 
		Pin pin = new Pin (pinStr, username);
		
		try {
			customer = bankingOperationsService.createNewCustomer(pin);
			if (customer != null) {
				log.info("Registration successful!");
			} else {
				log.info("Registration failed. Please contact the bank.");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());		
		}
		return customer;
	}

	public void depositFunds(Customer customer) {
		int accountId = 0;
		double amount = 0;

		log.info("Please enter Account ID : ");  
		
		try {
			accountId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		Account account = new Account (accountId, customer);

		log.info("");
		log.info("Please Enter Amount to Deposit: ");   

		try {
			amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		try {
			bankingOperationsService.depositFunds(account, amount);
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	
	public int getAccountType() {
		int choice = 0;
		
		do {
			log.info("PLEASE SELECT ACCOUNT TYPE");
			log.info("------------------------------");
			log.info("1) Checking");
			log.info("2) Saving");
			log.info("3) EXIT");
			log.info("Please enter an appropriate choice between 1-3");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
				return -1;
			}
			
			if (choice >= 1 && choice < 4) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}	
		} while (true);
	}
		
	public Account getAccountByAccountIdEmployee (Account account) throws BusinessException {
		Account retrievedAccount = account;
		int accountId = 0;
		String accountType = null;
		
		if (account == null) { 
			log.info("Please enter the account ID that you'd like to look up: ");
			
			try {
				accountId = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
				return null;
			}
			
			account = new Account(accountId);

			try {
				retrievedAccount = bankingOperationsService.getAccountByAccountId(account);
			} catch (BusinessException e) {
				log.info(e.getMessage());
			}
		} 

		if (retrievedAccount != null) {
			log.info("");
			log.info("ACCOUNT DETAILS FOR " + retrievedAccount.getCustomer().getFirstName().toUpperCase() + " " + retrievedAccount.getCustomer().getLastName().toUpperCase()  + "; DOB: " + retrievedAccount.getCustomer().getDob());
			log.info("------------------");
			
			if (retrievedAccount.getAccountType().equals("savings")) {
				accountType = "S";
			} else if (retrievedAccount.getAccountType().equals("checking")) {
				accountType = "C";
			}
				
			log.info("1) Account ID: " + retrievedAccount.getAccountId() + " ** Type: " + accountType + " ** Balance: $" + String.format("%.2f", retrievedAccount.getBalance()) + " ** Opened On: " + retrievedAccount.getOpenedDate());
		}	
		return retrievedAccount;
	}

	public int getAccountsByCustomerEmployee () {
		int choice = 0;
		
		do {
			log.info("");
			log.info("How would you like to search for a customer's account?");
			log.info("--------------------------------");
			log.info("1) By Customer ID");
			log.info("2) By Customer Name");
			log.info("3) EXIT");
			log.info("Please enter an appropriate choice between 1-3");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
				return -1;
			}
			
			if (choice >= 1 && choice <= 3) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
		} while (true);		
	}

	public List<Account> getAccountsByCustomerId (Customer customer, String status) throws BusinessException {
		List<Account> accounts = null;
		
		try {
			accounts = bankingOperationsService.getAccountsByCustomerId(customer, status);
			Account account = null;
			
			if (accounts != null && accounts.size() > 0) {
				log.info("");
				log.info("ACCOUNT DETAILS");
				log.info("------------------");
				
				for (int i = 0, j = 1; i < accounts.size(); i++, j++) {
					account = accounts.get(i);
					if (status.equals("active")) { 
						log.info(j + ") Account ID: " + account.getAccountId() + " ** Account Type: " + account.getAccountType() + " ** Balance: $" + String.format("%.2f", account.getBalance()));
					} else if (status.equals("pending")) {
						log.info(j + ") Account ID: " + account.getAccountId() + " ** Account Type: " + account.getAccountType() + " ** Balance: $" + String.format("%.2f", account.getBalance()) + " ** Opened on: " + account.getOpenedDate());
					}   // else
				}
			}
			
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
		return accounts;
	}

	public void getAccountsByCustomerIdEmployee () throws BusinessException {
		List<Account> accounts = null;
		String status = "active";
		int customerId = 0;
		
		log.info("Please enter the customer ID you'd like to look up: ");
		
		try {
			customerId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}

		try {
			Customer customer = new Customer(customerId);
			Customer retrievedCustomer = bankingOperationsService.getCustomerByCustomerId(customer);
			
			accounts = bankingOperationsService.getAccountsByCustomerId(retrievedCustomer, status);
			Account account = null;
			
			if (accounts != null && accounts.size() > 0) {
				log.info("");
				log.info("ACCOUNT DETAILS FOR " + retrievedCustomer.getFirstName().toUpperCase() + " " + retrievedCustomer.getLastName().toUpperCase() + "; DOB: " + retrievedCustomer.getDob());
				log.info("------------------");
				
				for (int i = 0, j = 1; i < accounts.size(); i++, j++) {
					account = accounts.get(i);
					log.info(j + ") Account ID: " + account.getAccountId() + " ** Account Type: " + account.getAccountType() + " ** Balance: $" + String.format("%.2f", account.getBalance())  + " ** Opened on: " + account.getOpenedDate());
				}
			}   //else
			
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
		return;
	}	

	public void getAccountsByCustomerNameEmployee () throws BusinessException {
		List<Account> accounts = null;
		String status = "active";
		String firstName = null;
		String lastName = null;
		
		log.info("Please enter the customer's First Name: ");
		firstName = sc.nextLine();

		log.info("Please enter the customer's Last Name: ");
		lastName = sc.nextLine();

		try {
			List<Customer> customers = bankingOperationsService.getCustomersByCustomerName(firstName, lastName);
			
			if (customers != null && customers.size() > 0) {
				for (Customer customer : customers) {
					accounts = bankingOperationsService.getAccountsByCustomerId(customer, status);
					Account account = null;
					
					if (accounts != null && accounts.size() > 0) {
						log.info("");
						log.info("ACCOUNT DETAILS FOR " + customer.getFirstName().toUpperCase() + " " + customer.getLastName().toUpperCase() + "; DOB: " + customer.getDob());
						log.info("------------------");
						
						for (int i = 0, j = 1; i < accounts.size(); i++, j++) {
							account = accounts.get(i);
							log.info(j + ") Account ID: " + account.getAccountId() + " ** Account Type: " + account.getAccountType() + " ** Balance: $" + String.format("%.2f", account.getBalance())  + " ** Opened on: " + account.getOpenedDate());
						}
					}
				}
			} else {
				log.info("\n" + firstName + " " + lastName + " not found.");
			}
			
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
		return;
	}	

	public int getMenuCustomer (Customer customer) {
		int choice = 0;
		
		do {
			log.info("");
			log.info("What would you like to do today?");
			log.info("--------------------------------");
			log.info("1) View Account Balance");
			log.info("2) View Previous Transactions");
			log.info("3) View Pending Transactions");
			log.info("4) Deposit Funds");
			log.info("5) Withdraw Funds");
			log.info("6) Transfer Funds");
			log.info("7) Open a New Account");
			log.info("8) EXIT");
			log.info("Please enter an appropriate choice between 1-8");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");  
				return -1;
			}
			
			if (choice >= 1 && choice <= 8) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
		} while (true);		
	}

	public void getWelcomeMessageCustomer (Customer customer) {
		log.info("\nWELCOME " + customer.getFirstName()+ " " + customer.getLastName() +"!");
		log.info("=*=*=*=*=*=*=*=*=*=*=*=*");
	}

	public int getMenuEmployee (Employee employee) {
		int choice = 0;
		
		do {
			log.info("");
			log.info("What would you like to do today?");
			log.info("--------------------------------");
			log.info("1) View Accounts Pending Approval");
			log.info("2) View Account by Account ID");
			log.info("3) View Account by Customer");
			log.info("4) EXIT");
			log.info("Please enter an appropriate choice between 1-4");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");  
				return -1;
			}
			
			if (choice >= 1 && choice <= 4) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
		} while (true);		
	}

	public void getWelcomeMessageEmployee (Employee employee) {
		log.info("\nWELCOME "+ employee.getFirstName() + " " + employee.getLastName() +"!");
		log.info("=*=*=*=*=*=*=*=*=*=*=*=*");
	}

	public void managePendingAccountsEmployee () throws BusinessException {
		List<Account> accounts = null;
		int accountChoice = 0;
		int approveRejectChoice = 0;
		Account account = null;
		String accountType = null;
		String result = null;
		int i = 0;
		int j = 1;
		
		try {
			accounts = bankingOperationsService.getAllAccounts("pending");
		} catch (BusinessException e) {
			log.info(e.getMessage());
			return;
		}

		if (accounts != null && accounts.size() > 0) {
			log.info("");
			log.info("ACCOUNT DETAILS");
			log.info("------------------");
			
			for (i = 0, j = 1; i < accounts.size(); i++, j++) {
				account = accounts.get(i);
				
				if (account.getAccountType().equals("savings")) {
					accountType = "S";
				} else if (account.getAccountType().equals("checking")) {
					accountType = "C";
				}
				
				log.info(j + ") Account ID: " + account.getAccountId() + " ** Type: " + accountType + " ** Balance: $" + String.format("%.2f", account.getBalance()) + " ** Opened On: " + account.getOpenedDate() + " ** Opened By: " + account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
			}
		}		
		
		log.info("\nPlease select an account that you'd like to approve or reject (0 for none): ");

		try {
			accountChoice = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		
		if (accountChoice >= 1 && accountChoice <= j) {
			account = accounts.get(accountChoice - 1);
			getAccountByAccountIdEmployee(account);
	
			log.info("\nWhat would you like to do with this account? ");
			log.info("\n1) Approve ");
			log.info("2) Reject ");
			log.info("3) EXIT ");
			
			try {
				approveRejectChoice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
				return;
			}

			if (approveRejectChoice == 1) {
				result = bankingOperationsService.updatePendingAccount(account, "active");
			} else if (approveRejectChoice == 2) {
				result = bankingOperationsService.updatePendingAccount(account, "rejected");
			} else if (approveRejectChoice == 3) {
				return;
			} else {
				log.info("Invalid approval option.");
				return;
			}
			
			if (result.equals("active")) {
				log.info("Account " + account.getAccountId() + " for " + account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName() + " has been successfully APPROVED!");
			} else if (result.equals("rejected")) {
				log.info("Account " + account.getAccountId() + " for " + account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName() + " has been successfully REJECTED!");
			} else {
				log.info("Approval process failed.");
				return;
			}
		} else if (accountChoice == 0) {
			return;
		} else {
			log.info("Invalid option.");
			return;
		}
		return;
	}

	public List<Transaction> managePendingTransactions(Customer customer) {
		List<Transaction> transactions = null;
		List<Transaction> pendingTransactions = new ArrayList<>();
		Transaction transaction = null;
		String status = "pending";
		int transactionChoice = 0;
		int approveRejectChoice = 0;
		String approveRejectStr = null;
		int i = 0;
		int j = 1;
		Account account = null;
		
		try {
			transactions = bankingOperationsService.getTransactionsByCustomerId(customer, status);

			log.info("");
			log.info("PENDING TRANSACTION DETAILS");
			log.info("---------------------------");
		
			if (transactions != null && transactions.size() > 0) {
				for (i = 0, j = 1; i < transactions.size(); i++) {
					transaction = transactions.get(i);
					if (transaction.getTransactionType().equals("credit")) {						
						pendingTransactions.add(transaction);
						getTransactionByTransactionId(transaction, "pending", j++);
					}
				}
				
				//transactionsListByCustomerId.stream().filter(t -> t !=null).forEach(t -> log.info(t));  //collect(Collectors.toList());
				
				if (j > 1) {
					log.info("\nWhich transaction would you like to approve or reject? (0 for none): ");
				} else {
					log.info("You have no pending transactions.");
					return null;
				}

				try {
					transactionChoice = Integer.parseInt(sc.nextLine());
				} catch (NumberFormatException e) {
					log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");  
					return null;
				}
				
				if (transactionChoice >= 1 && transactionChoice <= j) {
					transaction = transactions.get(transactionChoice - 1);
					getTransactionByTransactionId(transaction, "pending", 1);
							
					log.info("\nWhat would you like to do with this transaction? ");
					log.info("\n1) Approve ");
					log.info("2) Reject ");
					log.info("3) EXIT ");
					
					try {
						approveRejectChoice = Integer.parseInt(sc.nextLine());
					} catch (NumberFormatException e) {
						log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");  
						return null;
					}

					if (approveRejectChoice == 1) {
						approveRejectStr = "approved";
						account = bankingOperationsService.updatePendingTransaction(transaction, "confirmed");
					} else if (approveRejectChoice == 2) {
						approveRejectStr = "rejected";
						account = bankingOperationsService.updatePendingTransaction(transaction, "rejected");
					} else if (approveRejectChoice == 3) {
						return null;
					} else {
						log.info("Invalid approval option.");
						return null;
					}
					
					if (account != null) {
						log.info("Transaction " + approveRejectStr + "! Account " + account.getAccountId() + " now has a balance of $" + String.format("%.2f", Math.abs(account.getBalance())) + ".");
					} else {
						log.info("Approval failed.");
						return null;
					}
				} else if (transactionChoice == 0) {
					return null;
				} else {
					log.info("Invalid option.");
					return null;
				}

				return pendingTransactions;
			} else {
				log.info("You have no pending transactions.");
			}
		} catch (BusinessException e) {
			log.info("You have no pending transactions.");
		}
		
		return transactions;
	}

	public void getTransactionsByCustomerId(Customer customer, String status) {
		int i = 0;
		int j = 1;
		
		try {
			List<Transaction> transactions = bankingOperationsService.getTransactionsByCustomerId(customer, status);
			
			log.info("");
			log.info("TRANSACTION DETAILS");
			log.info("------------------");

			if (transactions != null && transactions.size() > 0) {
			
				for (i = 0, j = 1; i < transactions.size(); i++, j++) {
					getTransactionByTransactionId(transactions.get(i), status, j);
				}
				
				//transactionsListByCustomerId.stream().filter(t -> t !=null).forEach(t -> log.info(t));  //collect(Collectors.toList());
			} else {
				log.info("There are no transactions to list.");
			}
			
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	
	public void getTransactionByTransactionId(Transaction transaction, String status, int count) {	
		String accountType = null;
		Customer sendingCustomer = null;
		String countStr = "";

		if (transaction.getAccount().getAccountType().equals("savings")) {
			accountType = "S";
		} else if (transaction.getAccount().getAccountType().equals("checking")) {
			accountType = "C";
		}
		
		if (count > 0) {
			countStr = count + ") ";
		}
		
		if (status.equals("confirmed")) {
			log.info(countStr + 
						"Date: " + transaction.getTransactionDate() +
						" ** Account: " + transaction.getAccount().getAccountId() +
						" ** Account Type: " + accountType +
						" ** "+ transaction.getTransactionType() +
						" ** Amount: $" + String.format("%.2f", Math.abs(transaction.getAmount())) +
						" ** Balance: $" + String.format("%.2f", transaction.getNewBalance()));
		} else if (status.equals("pending")) {
			try {
				sendingCustomer = bankingOperationsService.getCustomerByTransactionId(new Transaction(transaction.getLinkedTransactionId()));
			} catch (BusinessException e) {
				log.info(e.getMessage());
			}
			
			log.info(countStr + 
					"Date: " + transaction.getTransactionDate() +
					" ** Account ID: " + transaction.getAccount().getAccountId() +
					" ** Account Type: " + accountType +
					" ** "+ transaction.getTransactionType() +
					" ** Amount: $" + String.format("%.2f", Math.abs(transaction.getAmount())) +
					" ** Sent By: " + sendingCustomer.getFirstName() + " " + sendingCustomer.getLastName());
		}
	}

	public Pin logInMenu(int user) {
		if (user == 1) {
			log.info("");
			log.info("RETURNING CUSTOMER");
			log.info("------------------");
		} 
		else if (user == 2) {
			log.info("EMPLOYEE LOGIN PORTAL");
			log.info("---------------------");
		}
		
		String usernameInput = null;
		String pinInput = null;
		
		log.info("\nPlease Enter your Username");
		usernameInput = sc.nextLine();
		
		log.info("\nPlease Enter your Pin");
		pinInput = sc.nextLine();
		
		Username username = new Username(usernameInput);
		Pin pin = new Pin(pinInput, username);
		
		return pin;
		
	}
	
	public int startMenu() {		
		int choice = 0;
		
		do {
			log.info("");
			log.info("****************************************************");
			log.info(" Welcome To Bago National Bank's Online Banking App ");
			log.info("****************************************************");
			log.info("\nBANKING MENU");
			log.info("-----------------");
			log.info("1) Returning Customer");
			log.info("2) New Customer");
			log.info("3) Employee");
			log.info("4) EXIT");
			log.info("Please enter an appropriate choice between 1-4");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");  
				return -1;
			}
			
			if (choice >= 1 && choice <= 4) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
		} while (true);
	}

	public void transferFundsExternal(Customer customer) {
		List<Account> accounts = null;
		int fromChoice = 0;
		int toChoice = 0;
		Account fromAccount = null;
		Account toAccount = null;
		double amount = 0;
		String status = "pending";
		
		try {
			accounts = getAccountsByCustomerId(customer, "active");
		} catch (BusinessException e) {
			log.info(e.getMessage());
			return;
		}

		try {
			log.info("\nPlease select the account that you want to transfer from: ");
			fromChoice = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		// validate that the account they choose is actually a choice you gave them
		if (fromChoice < 1 || fromChoice > accounts.size()) {
			return;
		}

		try {
			log.info("\nPlease enter the account ID that you want to transfer to: ");
			toChoice = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}

		fromAccount = accounts.get(fromChoice - 1);
		toAccount = new Account(toChoice);
		
		// validate that toChoice is an actual account
		try {
			if (bankingOperationsService.isAccount(toAccount) == false) {
				return;
			}
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();                               //change print statement
		}
		
		try {
			log.info("\nPlease Enter Amount to Transfer: ");
			amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {                     
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces."); 
			return;
		}

		try {
			toAccount.setCustomer(bankingOperationsService.getCustomerByAccountId(toAccount));
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
		
		try {
			bankingOperationsService.transferFunds(fromAccount, toAccount, amount, status);
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}

	public void transferFundsInternal(Customer customer) {
		List<Account> accounts = null;
		int fromChoice = 0;
		int toChoice = 0;
		Account fromAccount = null;
		Account toAccount = null;
		double amount = 0;
		String status = "confirmed";
		
		try {
			accounts = getAccountsByCustomerId(customer, "active");
		} catch (BusinessException e) {
			log.info(e.getMessage());
			return;
		}
		
		if (accounts.size() < 2) {
			log.info("You need to have at least 2 accounts in order to use this option.");
		}

		try {
			log.info("\nPlease select the account that you want to transfer from: ");
			fromChoice = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		// validate that the account they choose is actually a choice you gave them
		if (fromChoice < 1 || fromChoice > accounts.size()) {
			log.info("Invalid option.");
			return;
		}

		try {
			log.info("\nPlease select the account that you want to transfer to: ");
			toChoice = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}

		// validate that the account they choose is actually a choice you gave them
		
		if (toChoice < 1 || toChoice > accounts.size()) {
			log.info("Invalid option.");
			return;
		}

		try {
			log.info("\nPlease Enter Amount to Transfer: ");
			amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {                     
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}

		fromAccount = accounts.get(fromChoice - 1);
		toAccount = accounts.get(toChoice - 1);
		
		if (fromAccount.getAccountId() != toAccount.getAccountId()) {
			try {
				bankingOperationsService.transferFunds(fromAccount, toAccount, amount, status);
			} catch (BusinessException e) {
				log.info(e.getMessage());
			}
		} else {
			log.info("You cannot transfer funds from the same account to the same account.");
		}
	}

	public int transferFundsMenu() {		
		int choice = 0;
		
		do {
			log.info("");
			log.info("\nTRANSFER MENU");
			log.info("-----------------");
			log.info("1) Transfer between your own accounts");
			log.info("2) Transfer to an external account");
			log.info("3) EXIT");
			log.info("Please enter an appropriate choice between 1-3");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
				return -1;
			}
			
			if (choice >= 1 && choice <= 3) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
		} while (true);
	}

	public void withdrawFunds(Customer customer) {
		int accountId = 0;
		double amount = 0;
		
		log.info("Please enter Account ID : ");  
		
		try {
			accountId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		Account account = new Account (accountId, customer);
		
		log.info("");
		log.info("Please Enter Amount to Withdraw: ");   
		
		try {
			amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Please enter a numeric value. You cannot enter special characters, symbols or white spaces.");
			return;
		}
		
		try {
			bankingOperationsService.withdrawFunds(account, amount);
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}

}
