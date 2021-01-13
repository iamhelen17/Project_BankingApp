package com.bagonationalbank.app.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.dao.BankingOperationsDAO;
import com.bagonationalbank.app.dao.impl.BankingOperationsDAOImpl;
import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Employee;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transaction;
import com.bagonationalbank.app.model.Username;
import com.bagonationalbank.app.service.BankingOperationsService;

public class BankingOperationsServiceImpl implements BankingOperationsService {
	
	private static Logger log = Logger.getLogger(BankingOperationsServiceImpl.class);
	private BankingOperationsDAO bankingOperationsDAO = new BankingOperationsDAOImpl();

	@Override
	public Account createNewAccount(Customer customer, double deposit, String accountType) throws BusinessException {
		Account account = null;
		
		if (customer != null && deposit > 0 && (accountType.equals("savings") || accountType.equals("checking"))) {
		account = bankingOperationsDAO.createNewAccount(customer, deposit, accountType);
		} else {
			throw new BusinessException("Transaction and Account Type cannot be null, and deposit needs to be greater than $0.00, and account type must be savings or checking."); 
		}
		
		return account;
	}
	
	@Override
	public Customer createNewCustomer(Pin pin) throws BusinessException {
		Customer customer = null;
		
		if (pin.getUsername() != null && pin.getPin() != null) {
			customer = bankingOperationsDAO.createNewCustomer(pin);
		} else {
			throw new BusinessException("Username and Pin cannot be null.");
		}
		return customer;
	}

	@Override
	public Customer customerLogin(Pin customerCredentials) throws BusinessException {
		Customer customer = null;
		
		if (customerCredentials.getUsername().getUsername() != null && customerCredentials.getPin() != null) { 
			customer = bankingOperationsDAO.customerLogin(customerCredentials);
		} else {
			throw new BusinessException("Customer username and pin cannot be null."); 
		}
		return customer;
	}

	@Override
	public void depositFunds(Account account, double amount) throws BusinessException {
		String transactionStatus = "confirmed";
		Transaction transaction = null;

		try {
			if (bankingOperationsDAO.isAccount(account)) {				
				if (amount > 0) {	
					transaction = bankingOperationsDAO.updateBalance(account, amount, transactionStatus);
					if (transaction != null) {
						log.info("$" + String.format("%.2f", amount) + " was successfully deposited into account with Account ID: " + account.getAccountId() + ".");  
					} else {
						log.info("Deposit failed!");  
					}
				} else {
					log.info("You entered: $" + String.format("%.2f", amount) + ". Please enter an amount greater than 0!"); 
				}
			} else {
				log.info("Account ID: " + account.getAccountId() + " is INVALID!");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
	
	@Override
	public Employee employeeLogin(Pin employeeCredentials) throws BusinessException {
		Employee employee = null;
		
		if (employeeCredentials.getUsername().getUsername()  != null && employeeCredentials.getPin() != null) { 
			employee = bankingOperationsDAO.employeeLogin(employeeCredentials);
		} else {
			throw new BusinessException("Employee username and pin cannot be null."); 
		}
		return employee;		
	}

	@Override
	public Account getAccountByAccountId(Account account) throws BusinessException {
		Account retrievedAccount = null;
		
		if (account != null) {
			retrievedAccount = bankingOperationsDAO.getAccountByAccountId(account);
		} else {
			throw new BusinessException("Account ID cannot be null.");
		}
		
		return retrievedAccount;
	}

	@Override
	public List<Account> getAccountsByCustomerId(Customer customer, String status) throws BusinessException {
		List<Account> accounts = null;
		
		if (customer != null) {
			if (status.equals("active") || status.equals("closed") || status.equals("pending") || status.equals("rejected")) {
				accounts = bankingOperationsDAO.getAccountsByCustomerId(customer, status);
			}
			else {
				throw new BusinessException("Invalid status.");
			}
		} else {
			throw new BusinessException("Must pass a Customer."); 
		}

		return accounts;
	}

	@Override
	public List<Account> getAllAccounts(String status) throws BusinessException {
		List<Account> accounts = null;
		
		if (status.equals("active") || status.equals("closed") || status.equals("pending") || status.equals("rejected")) {
			accounts = bankingOperationsDAO.getAllAccounts(status);
		} else {
			throw new BusinessException("Status can be either active, pending or rejected.");  
		}
		return accounts;
	}


	@Override
	public List<Transaction> getAllTransactions() throws BusinessException {
		List<Transaction> allTransactionsList;
		
		allTransactionsList = bankingOperationsDAO.getAllTransactions();
		
		if (allTransactionsList != null && allTransactionsList.size() > 0) {
			return  allTransactionsList;
		} else {
			throw new BusinessException("Transactions cannot be null.");  
		}
		
	}

	@Override
	public Customer getCustomerByAccountId(Account account) throws BusinessException {
		Customer customer = null;
		
		if (account != null) {
			customer = bankingOperationsDAO.getCustomerByAccountId(account);
		} else {
			throw new BusinessException("Account cannot be null.");  
		}
		return customer;
	}

	@Override
	public Customer getCustomerByCustomerId(Customer customer) throws BusinessException {
		Customer retrievedCustomer = null;
		
		if (customer != null) {
			retrievedCustomer = bankingOperationsDAO.getCustomerByCustomerId(customer);
		} else {
			throw new BusinessException("Customer cannot be null.");  
		}
		return retrievedCustomer;
	}

	@Override
	public List<Customer> getCustomersByCustomerName(String firstName, String lastName) throws BusinessException {
		List<Customer> customers = null;
		
		if (firstName != null && lastName != null) {
			customers = bankingOperationsDAO.getCustomersByCustomerName(firstName, lastName);
		} else {
			throw new BusinessException("Name cannot be null.");  
		}
		return customers;
	}

	@Override
	public Customer getCustomerByTransactionId(Transaction transaction) throws BusinessException {
		Customer customer = null;
		
		if (transaction != null) {
			customer = bankingOperationsDAO.getCustomerByTransactionId(transaction);
		} else {
			throw new BusinessException("Transaction cannot be null."); 
		}
		return customer;		
	}
	
	@Override
	public List<Transaction> getTransactionsByAccountId(Account account, String status) throws BusinessException {
		List<Transaction> transactionsListByAccountId = null;
		
		if (account != null && (status.equals("active") || status.equals("pending") || status.equals("rejected"))) {
			transactionsListByAccountId  = bankingOperationsDAO.getTransactionsByAccountId(account, status);
		} else {
			throw new BusinessException("Account cannot be null.");  
		}
		return transactionsListByAccountId;
	}

	@Override
	public List<Transaction> getTransactionsByCustomerId(Customer customer, String status) throws BusinessException {
		List<Transaction> transactionsListByCustomerId = null;
		
		if (customer != null) {
			if (status.equals("active") || status.equals("pending") || status.equals("rejected")) {
				transactionsListByCustomerId  = bankingOperationsDAO.getTransactionsByCustomerId(customer, status);
			} else {
				throw new BusinessException("Invalid status."); 
			}
		} else {
			throw new BusinessException("Customer cannot be null."); 
		}			
		return transactionsListByCustomerId;
	}

	@Override
	public boolean isAccount(Account account) throws BusinessException {
		boolean valid = false;
		
		if (account != null) {
			valid = bankingOperationsDAO.isAccount(account);
		} else {
			throw new BusinessException("Account cannot be null."); 
		}
		return valid;
	}

	@Override
	public boolean isUsername(Username username) throws BusinessException {
		boolean valid = false;
		
		if (username.getUsername() != null) {
			valid = bankingOperationsDAO.isUsername(username);
		} 
		return valid; 
	}
	
	@Override
	public boolean isValidCredentials(String credentials, int minCharacters, int maxCharacters, boolean required) {
		
		boolean valid = false;
		
		if (required && credentials == null) {
			return false;
		}
		
		if (credentials == null || (credentials.length() >= minCharacters  && credentials.length() <=  maxCharacters && credentials.matches("^([a-zA-Z0-9_-]){7,20}$"))) {
			valid = true;
		} 
		return valid;
	}

	@Override
	public boolean isValidDate(String date) {
		boolean valid = false;
		
		if (date != null && date.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
			valid = true;
		}
		return valid;
	}

	@Override
	public boolean isValidEmail(String email) {
		boolean valid = false;
		
		if (email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			valid = true;
		}
		
		return valid;
	}
	
	@Override
	public boolean isValidGender(String gender){
		boolean valid = false;
		
		if (gender != null && gender.matches("[M,F]{1}")) {
			valid = true;
		}
		return valid;
	}

	@Override
	public boolean isValidNumber(String num, int minCharacters, int maxCharacters, boolean required) {
		boolean valid = false;
		
		if (required && num == null) {
			return false;
		}
		
		if (num == null || (num.length() >= minCharacters  && num.length() <=  maxCharacters && num.matches("[0-9]{1,10}") )) {
			valid = true;
		} 
		return valid;
	}
	
	@Override
	public boolean isValidString(String str, int minCharacters, int maxCharacters, boolean required, boolean allAlpha) {
		boolean valid = false;
		
		if (required && str == null) {
			return false;
		}
		
		if (allAlpha && !str.matches("^[a-zA-Z]+$")) {
			return false;
		}
		
		if (str == null || (str.length() >= minCharacters  && str.length() <=  maxCharacters)) {
			valid = true;
		} 
		return valid;
	}
	
	@Override
	public void transferFunds(Account fromAccount, Account toAccount, double amount, String status) throws BusinessException {
		boolean fromSuccess = false;
		boolean toSuccess = false;
		
		Transaction fromTransaction = null;
		Transaction toTransaction = null;
		
		try {
			if (bankingOperationsDAO.isAccount(fromAccount)) { 	
				if (bankingOperationsDAO.isAccount(toAccount)) {	
					if (amount > 0) {	
						if (bankingOperationsDAO.getAccountByAccountId(fromAccount).getBalance() >= amount) {
							fromTransaction = bankingOperationsDAO.updateBalance(fromAccount, -amount, status);
							toTransaction = bankingOperationsDAO.updateBalance(toAccount, amount, status);
							// This is where we link the transactions.
							fromSuccess = bankingOperationsDAO.setLinkedTransactionId(fromTransaction, toTransaction.getTransactionId());
							toSuccess = bankingOperationsDAO.setLinkedTransactionId(toTransaction, fromTransaction.getTransactionId());
							
							if (fromTransaction != null) {
								if (toTransaction != null) {
									if (fromSuccess) {
										if (toSuccess) {
											if (status.equals("confirmed")) {
												log.info("$"+String.format("%.2f", amount) + " was successfully transferred from account "+fromAccount.getAccountId()+" to account " + toAccount.getAccountId() + ".");  //do over checking or saving account
											}
											else if (status.equals("pending")) {
												log.info("$"+String.format("%.2f", amount) + " will be transferred from account "+fromAccount.getAccountId()+" to account " + toAccount.getAccountId() + " pending approval.");
											}
											else {
												log.info("Invalid status: " + status);
											}
										} else {
											log.info("Failed to set linked transaction ID on TO account.");
										}
									} else {
										log.info("Failed to set linked transaction ID on FROM account.");
									}
								} else {
									log.info("Failed to update balance on TO account.");
								}
							} else {
								log.info("Failed to update balance on FROM account.");  
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
	public String updatePendingAccount(Account account, String status, Employee employee) throws BusinessException {
		String result = null;
		
		if (account != null) { 
			if ((status.equals("active") || status.equals("closed") || status.equals("pending") || status.equals("rejected"))) {
				result = bankingOperationsDAO.updatePendingAccount(account, status, employee);
			} else {
				throw new BusinessException("Invalid status.");
			}
		} else {
			throw new BusinessException("Account cannot be null.");  
		}
		return result;
	}

	@Override
	public Account updatePendingTransaction(Transaction transaction, String status) throws BusinessException {
		Account account = null;
		
		if (transaction != null) {
			if (status.equals("confirmed") || status.equals("pending") || status.equals("rejected")) {
				account = bankingOperationsDAO.updatePendingTransaction(transaction, status);
			} else {
				throw new BusinessException("Invalid status.");
			}
		} else {
			throw new BusinessException("Transaction cannot be null.");
		}
		return account;
	}

	@Override
	public void withdrawFunds(Account account, double amount) throws BusinessException {
		String transactionStatus = "confirmed";
		Transaction transaction = null;
		
		try {
			if (bankingOperationsDAO.isAccount(account)) {				
				if (amount > 0) {	
					if (bankingOperationsDAO.getAccountByAccountId(account).getBalance() >= amount) {
						transaction = bankingOperationsDAO.updateBalance(account, -amount, transactionStatus);
						if (transaction != null) {
							log.info("$" + String.format("%.2f", amount) + " was successfully withdrawn from account with Account ID: " + account.getAccountId() + "."); 
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
				log.info("Account ID: " + account.getAccountId() + " is INVALID!");
			}
		} catch (BusinessException e) {
			log.info(e.getMessage());
		}
	}
}
