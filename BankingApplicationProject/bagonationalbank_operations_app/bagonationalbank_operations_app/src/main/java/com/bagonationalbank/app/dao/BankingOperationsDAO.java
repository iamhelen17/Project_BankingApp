package com.bagonationalbank.app.dao;

import java.util.List;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transactions;

public interface BankingOperationsDAO {

	public String createNewAccount();
	public Customer logIn(Pin pin) throws BusinessException;
	public Account getBalance(Account account) throws BusinessException;
	public int updateBalance(Account account, double amount) throws BusinessException;
	public boolean isValidAccount(Account account) throws BusinessException;
	public List<Customer> getAllCustomers();
	public List<Account> getAllAccounts(Customer customer) throws BusinessException;
	public List<Transactions> getAllTransactions();
}
