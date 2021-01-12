package com.bagonationalbank.app.dao;

import java.util.List;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Employee;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transaction;
import com.bagonationalbank.app.model.Username;

public interface BankingOperationsDAO {
	
	//customer
	public Account createNewAccount(Customer customer, double deposit, String accountType) throws BusinessException;
	public Customer createNewCustomer(Pin pin) throws BusinessException;
	
	public Customer logIn(Pin pin) throws BusinessException;
	
	
	//transactions
	
	public Customer getCustomerByTransactionId(Transaction transaction) throws BusinessException;
	public List<Transaction> getTransactionsByAccountId(Account account, String status) throws BusinessException;
	public List<Transaction> getTransactionsByCustomerId(Customer customer, String status) throws BusinessException;
	
	public Account updatePendingTransaction(Transaction transaction, String status) throws BusinessException;
	public List<Transaction> getAllTransactions() throws BusinessException;
	
	//employee
	public Employee employeeLogIn(Pin employeeCredentials) throws BusinessException;
	
	
	
	//verification
	public boolean isAccount(Account account) throws BusinessException;   //if the account exists
	public boolean isUsername(Username username) throws BusinessException;  //if the username already exists
	
	//accounts
	public String updatePendingAccount(Account account, String status) throws BusinessException;
	public Account getAccountByAccountId(Account account) throws BusinessException;
	public List<Account> getAccountsByCustomerId(Customer customer, String status) throws BusinessException;
	public List<Account> getAllAccounts(String status) throws BusinessException;
	public Account getBalance(Account account) throws BusinessException;
	public Transaction updateBalance(Account account, double amount, String status) throws BusinessException;

	//common banking operations
	public Customer getCustomerByAccountId(Account account) throws BusinessException;
	public Customer getCustomerByCustomerId(Customer customer) throws BusinessException;
	public List<Customer> getCustomersByCustomerName(String firstName, String lastName) throws BusinessException;
	
	
	
	public boolean setLinkedTransactionId (Transaction transaction, int linkedTransactionId) throws BusinessException;
	
	
	
}
