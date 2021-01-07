package com.bagonationalbank.app.main;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Username;
import com.bagonationalbank.app.service.BankingOperationsService;
import com.bagonationalbank.app.service.impl.BankingOperationsServiceImpl;

public class ConsoleMenu {

	private static Logger log = Logger.getLogger(ConsoleMenuMain.class);
	Scanner sc = new Scanner(System.in);
	private BankingOperationsService bankingOperationsService = new BankingOperationsServiceImpl();
	
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
				
			}
			
			if (choice >= 1 && choice <5) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
			
			
		} while (true);
	}
	
	
	public Pin logInMenu(int user) {
		if (user == 1) {
			log.info("");
			log.info("RETURNING CUSTOMER");
			log.info("---------------------");
		} 
		else if (user == 2) {
			log.info("Welcome Employee");
		}
		
		String usernameInput = null;
		String pinInput = null;
		
		log.info("Please Enter your Username");
		usernameInput = sc.nextLine();
		
		log.info("\nPlease Enter your Pin");
		pinInput = sc.nextLine();
		
		Username username = new Username(usernameInput);
		Pin pin = new Pin(pinInput, username);
		
		return pin;
		
	}
	
	public void getWelcomeMessage (Customer logIn) {
		log.info("\nWELCOME "+logIn.getFirstName()+ " " +logIn.getLastName() +"!");
		log.info("=*=*=*=*=*=*=*=*=*=*=*=*");
	}
	
	
	public int getCustomerMenu (Customer customer) {
		int choice = 0;
		
		do {
	
			log.info("");
			log.info("What would you like to do today?");
			log.info("--------------------------------");
			log.info("1) View Acount Balance");
			log.info("2) View Previous Transactions");
			log.info("3) Deposit Funds");
			log.info("4) Withdraw Funds");
			log.info("5) Transfer Funds");
			log.info("6) EXIT");
			log.info("Please enter an appropriate choice between 1-6");
			
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				
			}
			
			if (choice >= 1 && choice < 7) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
		
		} while (true);
		
		
	}
	
	public void getAllAccounts (Customer customer) throws BusinessException {
		try {
			List<Account> allAccountsList = bankingOperationsService.getAllAccounts(customer);
			
			if (allAccountsList != null && allAccountsList.size() > 0) {
				log.info("");
				log.info("ACCOUNT DETAILS");
				log.info("------------------");
				
				for (Account a :allAccountsList) {
					log.info("Account ID: "+a.getAccountId() +" ** Type: "+a.getType() +" ** Balance: $"+ String.format("%.2f", a.getBalance()));
				}
			}
			
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
		return;
	}
	
	public void depositFunds(Customer customer) {
		int accountId = 0;
		double amount = 0;

		log.info("Please enter Account ID : ");  
		
		try {
			accountId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid Account Id... Please try again");
			return;
		}
		
		Account account = new Account (accountId, customer);

		log.info("");
		log.info("Please Enter Amount to Deposit: ");   

		try {
			amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {
				log.info("Invalid Amount... Please try again!");   
				return;
		}
		
		try {
			bankingOperationsService.depositFunds(account, amount);
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	

	
	public void withdrawFunds(Customer customer) {
		int accountId = 0;
		double amount = 0;
		

		log.info("Please enter Account ID : ");  
		
		try {
			accountId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid Account Id... Please try again!");
			return;
		}
		
		Account account = new Account (accountId, customer);
		
		log.info("");
		log.info("Please Enter Amount to Withdraw: ");   
		
		try {
			amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid Amount... Please try again");   
			return;
		}
		
		try {
			bankingOperationsService.withdrawFunds(account, amount);
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	
	public void transferFunds(Customer customer) {
		int fromAccountId = 0;
		int toAccountId = 0;
		double amount = 0;
		
		log.info("Please enter Account ID you'd like to transfer from: ");
		
		try {
			fromAccountId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid Account Id... Please try again!");
			return;
		}
		
		log.info("Please enter Account ID you'd like to transfer to: ");
		try {
			toAccountId = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid Account Id... Please try again!");
			return;
		}
		
		
		Account fromAccount = new Account (fromAccountId, customer);
		Account toAccount = new Account (toAccountId);
		
		log.info("");
		log.info("Please Enter Amount to Transfer: ");
		
		try {
		amount = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {                     // what about nullpointerexception
			log.info("Invalid Amount... Please try again");   
			return;
		}
		
		try {
			bankingOperationsService.transferFunds(fromAccount, toAccount, amount);
		} catch (BusinessException e) {
			log.info(e.getMessage());;
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
				
			}
			
			if (choice >= 1 && choice < 4) {
				return choice;
			} else {
				log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
			}
			
			
		}while (true);
		
		
	}
	
}
