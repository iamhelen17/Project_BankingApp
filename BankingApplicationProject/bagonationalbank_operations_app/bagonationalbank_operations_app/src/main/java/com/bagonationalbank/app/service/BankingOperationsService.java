package com.bagonationalbank.app.service;

import java.util.List;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transactions;

public interface BankingOperationsService {

	public String createNewAccount();
	public Customer logIn(Pin pin) throws BusinessException;
	public void depositFunds(Account account, double amount) throws BusinessException;
	public void withdrawFunds(Account account, double amount) throws BusinessException;
	public void transferFunds(Account fromAccount, Account toAccount, double amount) throws BusinessException;
	public List<Customer> getAllCustomers();
	public List<Account> getAllAccounts(Customer customer) throws BusinessException;
	public List<Transactions> getAllTransactions();
	
}
