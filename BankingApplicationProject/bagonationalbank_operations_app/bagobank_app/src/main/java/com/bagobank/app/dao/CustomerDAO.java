package com.bagobank.app.dao;

import java.util.List;

import com.bagobank.app.exception.BusinessException;
import com.bagobank.app.model.Account;
import com.bagobank.app.model.Customer;
import com.bagobank.app.model.Pin;
import com.bagobank.app.model.Transaction;


public interface CustomerDAO {

	public int createCustomer();
	public int updateCustomer();
	
	
	
	public List<Account> getAllAccounts(Customer customer) throws BusinessException;
	public List<Transaction> getTransactionsByAccountId(Account account) throws BusinessException;
	public List<Transaction> getTransactionsByCustomerId(Customer customer) throws BusinessException;
	public List<Transaction> getAllTransactions() throws BusinessException;
}
