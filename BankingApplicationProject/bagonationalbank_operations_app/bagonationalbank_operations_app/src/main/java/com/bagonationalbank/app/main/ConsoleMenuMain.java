package com.bagonationalbank.app.main;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Employee;
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
		int employeeMenuChoice = 0;
		int transferMenuChoice = 0;
		int viewAccountByCustomerChoice = 0;

		do {
			startMenuChoice = console.startMenu();
			if (startMenuChoice == 1) {
				Pin customerCredentials = console.logInMenu(1);
				
				try {
					Customer customer = bankingOperationsService.logIn(customerCredentials);
					if (customer != null) {
						console.getWelcomeMessageCustomer(customer);
						//console.getAccountsByCustomerId(logIn);
						do {
							customerMenuChoice = console.getMenuCustomer(customer);
							switch (customerMenuChoice) {
							case 1:
								console.getAccountsByCustomerId(customer, "active");
								break;
							case 2:
								console.getTransactionsByCustomerId(customer, "confirmed");
								break;
							case 3:
								console.managePendingTransactions(customer);
								break;
							case 4:
								console.depositFunds(customer);
								break;
							case 5:
								console.withdrawFunds(customer);
								break;
							case 6:
								do {
									transferMenuChoice = console.transferFundsMenu();
									switch(transferMenuChoice) {
									case 1:
										console.transferFundsInternal(customer);
										break;
									case 2:
										console.transferFundsExternal(customer);
										break;
									case 3:
										break;
									default:
										log.info("INVALID TRANSFER MENU OPTION.... Kindly retry!!!!!");
										break;
									}
								} while (transferMenuChoice != 3);
								break;
							case 7:
								console.createNewAccount(customer);
								break;
							case 8:
								log.info("Thank You for using our Banking App... Have a good one!!");
								break;
							default:
								log.info("INVALID TRANSFER MENU OPTION.... Kindly retry!!!!!");
								break;
							}
						} while (customerMenuChoice != 8);				
					} else {
						log.info("Invalid Credentials... Please try again");  // if not successful
					}
				} catch (BusinessException e) {
					log.info(e.getMessage());
				}
				
			} else if (startMenuChoice == 2) {
				Customer customer = console.createNewCustomer();
				console.createNewAccount(customer);
			} else if (startMenuChoice == 3) {
				Pin employeeCredentials = console.logInMenu(2);
				Employee employee = null;
				
				try {
					employee = bankingOperationsService.employeeLogIn(employeeCredentials);
				
					if (employee != null) {
						console.getWelcomeMessageEmployee(employee);
						do {
							employeeMenuChoice = console.getMenuEmployee(employee);
							switch (employeeMenuChoice) {
							case 1:
								console.managePendingAccountsEmployee();
								break;
							case 2:
								console.getAccountByAccountIdEmployee(null);
								break;
							case 3:
								do {
									viewAccountByCustomerChoice = console.getAccountsByCustomerEmployee();
									switch(viewAccountByCustomerChoice) {
									case 1:
										console.getAccountsByCustomerIdEmployee();
										break;
									case 2:
										console.getAccountsByCustomerNameEmployee();
										break;
									case 3:
										break;
									default:
										log.info("INVALID ACCOUNT MENU OPTION.... Kindly retry!!!!!");
										break;
									}
								} while (viewAccountByCustomerChoice != 3);	
								break;
							default:
								log.info("INVALID EMPLOYEE MENU OPTION.... Kindly retry!!!!!");
								break;
							}
						} while (employeeMenuChoice != 4);				
					}
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (startMenuChoice == 4) {
				log.info("Thanks for banking with Bago National Bank. Have a great day!");
			} 
		} while (startMenuChoice != 4);	
	}
}
