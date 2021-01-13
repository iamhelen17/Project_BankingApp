package com.bagonationalbank.app.service;

import java.util.List;

import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Employee;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transaction;
import com.bagonationalbank.app.model.Username;

public interface BankingOperationsService {
	
	//Customer
	public Account createNewAccount(Customer customer, double deposit, String accountType) throws BusinessException;
	public Customer createNewCustomer(Pin pin) throws BusinessException;
	
	public Customer customerLogin(Pin customerCredentials) throws BusinessException;
	
	//Transactions
	public void depositFunds(Account account, double amount) throws BusinessException;
	public void withdrawFunds(Account account, double amount) throws BusinessException;	
	public void transferFunds(Account fromAccount, Account toAccount, double amount, String status) throws BusinessException;
	public Account updatePendingTransaction(Transaction transaction, String status) throws BusinessException;
	
	//Employee
	public Employee employeeLogin(Pin employeeCredentials) throws BusinessException;
	
	//Validation
	public boolean isAccount(Account account) throws BusinessException;
	public boolean isValidString(String str, int minCharacters, int maxCharacters, boolean required, boolean allAlpha);
	public boolean isValidNumber(String num, int minCharacters, int maxCharacters, boolean required);
	public boolean isValidCredentials(String credentials, int minCharacters, int maxCharacters, boolean required);
	public boolean isUsername(Username username) throws BusinessException;
	public boolean isValidGender(String gender);
	public boolean isValidEmail(String email);
	public boolean isValidDate(String date);
	
	//Accounts
	public String updatePendingAccount(Account account, String status, Employee employee) throws BusinessException;
	public Account getAccountByAccountId(Account account) throws BusinessException;
	public List<Account> getAccountsByCustomerId(Customer customer, String status) throws BusinessException;
	public List<Account> getAllAccounts(String status) throws BusinessException;
	
	//Shared Banking Operations
	public Customer getCustomerByAccountId(Account account) throws BusinessException;
	public Customer getCustomerByCustomerId(Customer customer) throws BusinessException;
	public List<Customer> getCustomersByCustomerName(String firstName, String lastName) throws BusinessException;
	public Customer getCustomerByTransactionId(Transaction transaction) throws BusinessException;
	
	public List<Transaction> getTransactionsByAccountId(Account account, String status) throws BusinessException;
	public List<Transaction> getTransactionsByCustomerId(Customer customer, String status) throws BusinessException;
	public List<Transaction> getAllTransactions() throws BusinessException;
}
