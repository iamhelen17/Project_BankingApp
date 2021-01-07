package com.bagonationalbank.app.main;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.service.BankingOperationsService;
import com.bagonationalbank.app.service.impl.BankingOperationsServiceImpl;

public class ConsoleMenuMain {

private static Logger log = Logger.getLogger(ConsoleMenuMain.class);
	

	public static void main (String[] args) {
			
		BankingOperationsService bankingOperationsService = new BankingOperationsServiceImpl();
		
		ConsoleMenu console = new ConsoleMenu();
		int startMenuChoice = 0;
		int customerMenuChoice = 0;

		do {
			startMenuChoice = console.startMenu();
			if (startMenuChoice == 1) {
				Pin customerCredentials = console.logInMenu(1);
				
				try {
					Customer customer = bankingOperationsService.logIn(customerCredentials);
					if (customer != null) {
						console.getWelcomeMessage(customer);
						//console.getAllAccounts(logIn);
						do {
							customerMenuChoice = console.getCustomerMenu(customer);
							switch (customerMenuChoice) {
							case 1:
								//log.info("Customer ID: " + customer.getCustomerId());
								console.getAllAccounts(customer);
								break;
							case 2:
								
								break;
							case 3:
								console.depositFunds(customer);
								break;
							case 4:
								console.withdrawFunds(customer);
								break;
							case 5:
								console.transferFunds(customer);
								break;
							case 6:
								log.info("Thank You for using our Banking App... Have a good one!!");
								break;
							default:
								log.info("INVALID MENU OPTION.... Kindly retry!!!!!");
								break;
							}
						} while (customerMenuChoice != 5);
						
					} else {
						log.info("Invalid Credentials... Please try again");  // if not successful
					}
				} catch (BusinessException e) {
					log.info(e.getMessage());
				}
				
			} else if (startMenuChoice == 2) {
				log.info("under construction");
			} else if (startMenuChoice == 3) {
				//int employeeLogin = console.logInMenu(2);
			} else if (startMenuChoice == 4) {
				log.info("Thanks for banking with Bago National Bank. Have a great day!");
			}
		} while (startMenuChoice != 4);
		
	
		
		
		
		
		
	}
		
		
		
}
