package com.bagonationalbank.app.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.dao.BankingOperationsDAO;
import com.bagonationalbank.app.dao.impl.BankingOperationsDAOImpl;
import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transactions;
import com.bagonationalbank.app.service.BankingOperationsService;

public class BankingOperationsServiceImpl implements BankingOperationsService {
	
	private static Logger log = Logger.getLogger(BankingOperationsServiceImpl.class);
	

	private BankingOperationsDAO bankingOperationsDAO = new BankingOperationsDAOImpl();

	@Override
	public String createNewAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer logIn(Pin customerCredentials) throws BusinessException {
		Customer customer = bankingOperationsDAO.logIn(customerCredentials);
		return customer;
	}

	
	@Override
	public void depositFunds(Account account, double amount) throws BusinessException {
		try {
			if (bankingOperationsDAO.isValidAccount(account)) {				
				if (amount > 0) {	
						int update = bankingOperationsDAO.updateBalance(account, amount);
						if (update == 1) {
							log.info("$"+String.format("%.2f", amount) + " was successfully deposited into Account with Account ID: "+account.getAccountId()+".");  //checking or saving account
						} else {
							log.info("Deposit failed!");  
						}
				} else {
					log.info("You entered: $" + String.format("%.2f", amount) + ". Please enter an amount greater than 0!"); 
				}
			} else {
				log.info("Account ID: "+account.getAccountId() +" is INVALID!");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	
	
	
	@Override
	public void withdrawFunds(Account account, double amount) throws BusinessException {
		try {
			if (bankingOperationsDAO.isValidAccount(account)) {				
				if (amount > 0) {	
					if (bankingOperationsDAO.getBalance(account).getBalance() >= amount) {
						int update = bankingOperationsDAO.updateBalance(account, -amount);
						if (update == 1) {
							log.info("$"+String.format("%.2f", amount) + " was successfully withdrawn from Account with Account ID: "+account.getAccountId()+".");  //checking or saving account
						} else {
							log.info("Withdrawal failed!");  
						}
					} else {
						log.info("Insufficient funds in your account!");
					}
				} else {
					log.info("You entered: $" + String.format("%.2f", amount) + ". Please enter an amount greater than 0!"); 
				}
			} else {
				log.info("Account ID: "+account.getAccountId() +" is INVALID!");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	
	@Override
	public void transferFunds(Account fromAccount, Account toAccount, double amount) throws BusinessException {
		try {
			if (bankingOperationsDAO.isValidAccount(fromAccount)) {// && bankingOperationsDAO.isValidAccount(toAccount)) {	
				if (bankingOperationsDAO.isValidAccount(toAccount)) {	
					if (amount > 0) {	
						if (bankingOperationsDAO.getBalance(fromAccount).getBalance() >= amount) {
							int fromUpdate = bankingOperationsDAO.updateBalance(fromAccount, -amount);
							int toUpdate = bankingOperationsDAO.updateBalance(toAccount, amount);
							if (fromUpdate == 1 && toUpdate == 1) {
								log.info("$"+String.format("%.2f", amount) + " was successfully transferred from Account with Account ID: "+fromAccount.getAccountId()+" to  Account with Account ID: " + toAccount.getAccountId() + ".");  //do over checking or saving account
							} else {
								log.info("Transfer failed!");  
							}
						} else {
							log.info("Insufficient funds in your account!");
						}
					} else {
						log.info("You entered: $" + String.format("%.2f", amount) + ". Please enter an amount greater than 0!"); 
					}
				} else {
					log.info("Account ID: "+toAccount.getAccountId()+ " is Invalid!");
				}
			} else {
				log.info("Account ID: "+fromAccount.getAccountId()+ " is Invalid!");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}	

	@Override
	public List<Customer> getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> getAllAccounts(Customer customer) throws BusinessException {
		List<Account> allAccountsList;
		
		if (customer != null) {
			allAccountsList = bankingOperationsDAO.getAllAccounts(customer);
		} else {
			throw new BusinessException("No customer entered." );    //enter +accountID
		}
		
		return allAccountsList;
	}

	@Override
	public List<Transactions> getAllTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
