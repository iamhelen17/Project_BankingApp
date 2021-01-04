package com.bagonationalbank.app.main;

import java.util.Scanner;

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
		int choice = console.startMenu();
		int customerMenuChoice = 0;

		
		if (choice == 1) {
			Pin customerCredentials = console.logInMenu(1);
			
			try {
				Customer logIn = bankingOperationsService.logIn(customerCredentials);
				if (logIn != null) {
					console.getAllAccounts(logIn);
					do {
						customerMenuChoice = console.getCustomerMenu(logIn);
						switch (customerMenuChoice) {
						case 1:
							console.getAllAccounts(logIn);
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						case 4:
							
							break;
						case 5:
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
				log.info(e.getMessage());;
			}
			
		} else if (choice == 2) {
			log.info("under construction");
		} else if (choice == 3) {
			//int employeeLogin = console.logInMenu(2);
		} else if (choice == 4) {
			log.info("error under construction");
		}
		
	
		
		
		
		
		
	}
		
		
		
}


//System.out.println("Welcome "); //add customer name

//Scanner sc = new Scanner(System.in);
//BankingOperationsService  bankingOperationsService = new BankingOperationsServiceImpl();