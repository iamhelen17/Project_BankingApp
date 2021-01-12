package com.bagonationalbank.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.dao.BankingOperationsDAO;
import com.bagonationalbank.app.dao.dbutil.PostresqlConnection;
import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Employee;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transaction;
import com.bagonationalbank.app.model.Username;

public class BankingOperationsDAOImpl implements BankingOperationsDAO {

private static Logger log = Logger.getLogger(BankingOperationsDAOImpl.class);
private Account account;


	@Override
	public String updatePendingAccount(Account account, String status) throws BusinessException {
		boolean success = false;

		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "update bago_national_bank.account " +
							"set status = ? " +
							"where account_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, account.getAccountId());
			
			int accountUpdate = preparedStatement.executeUpdate();

			if (accountUpdate == 1) {
				return status;
			} else {
				throw new BusinessException("Update account failed. Contact SYSADMIN! approvePendingAccount");
			}

		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error. Contact SYSADMIN! approvePendingAccount");
		}		
	}

	@Override
	public Account updatePendingTransaction(Transaction transaction, String status) throws BusinessException {
		Account account = null;
		int fromAccountId = 0;
		ResultSet resultSet;
		int toAccountUpdate = 0;
		int transactionUpdate = 0;
		int fromAccountUpdate = 0;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "update bago_national_bank.transactions " +
							"set status = ? where transaction_id in (?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, transaction.getTransactionId());
			preparedStatement.setInt(3, transaction.getLinkedTransactionId());
			
			transactionUpdate = preparedStatement.executeUpdate();

			if (status.equals("confirmed")) {
				// Update the balance in the TO account. 
				// The details are in the transaction in the parameter.
				sql = "update bago_national_bank.account " +
						"set balance = balance + ? " +
						"where account_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				
				preparedStatement.setDouble(1, transaction.getAmount());
				preparedStatement.setInt(2, transaction.getAccount().getAccountId());
				
				toAccountUpdate = preparedStatement.executeUpdate();
				
			
				sql = "select account_id " +
						"from bago_national_bank.transactions " +
						"where transaction_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, transaction.getLinkedTransactionId());
				
				resultSet = preparedStatement.executeQuery();
				
				if (resultSet.next()) {
					fromAccountId = resultSet.getInt("account_id");
				} else {
					log.info("Could not find account.");
				}
				
				
				// Update the balance in the FROM account.
				sql = "update bago_national_bank.account " +
						"set balance = balance - ? " +
						"where account_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				
				preparedStatement.setDouble(1, transaction.getAmount());
				preparedStatement.setInt(2, fromAccountId);
				
				fromAccountUpdate = preparedStatement.executeUpdate();
			}
			
			// Get the update To account information, and return it.
			sql = "select account_id, account_type, balance " +
					"from bago_national_bank.account " +
					"where account_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, transaction.getAccount().getAccountId());
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				account = new Account();
				account.setAccountId(resultSet.getInt("account_id"));
				account.setType(resultSet.getString("account_type"));
				account.setBalance(resultSet.getFloat("balance"));
				account.setCustomer(transaction.getCustomer());
			}

			if (status.equals("rejected")) {
				if (transactionUpdate == 2) {
					return account;
				} else {
					throw new BusinessException("Update transaction failed. Contact SYSADMIN! updatePendingTransaction");
				}
			} else if (status.equals("confirmed")) {
				if (fromAccountUpdate == 1) {
					if (toAccountUpdate == 1) {
						if (transactionUpdate == 2) {
							return account;
						} else {
							throw new BusinessException("Update transaction failed. Contact SYSADMIN! updatePendingTransaction");
						}
					} else {
						throw new BusinessException("To Account update failed. Contact SYSADMIN! updatePendingTransaction");
					}
				} else {
					throw new BusinessException("From Account update failed. Contact SYSADMIN! updatePendingTransaction");
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error. Contact SYSADMIN! updatePendingTransaction");
		}
		
		return account;
	}

	@Override
	public Account createNewAccount(Customer customer, double deposit, String accountType) throws BusinessException {
		Account account = null;
		int account_id = 0;
		Date today = Calendar.getInstance().getTime();

		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "insert into bago_national_bank.account (account_type, balance, opened_date, status, customer_id) values (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, accountType);
			preparedStatement.setDouble(2, deposit);
			preparedStatement.setDate(3, new java.sql.Date(today.getTime()));
			preparedStatement.setString(4, "pending");
			preparedStatement.setInt(5, customer.getCustomerId());
			
			int accountInsert = preparedStatement.executeUpdate();

			sql = "select max(account_id) account_id from bago_national_bank.account";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				account_id = resultSet.getInt("account_id");
			} else {
				throw new BusinessException("Problem getting account_id. createNewAccount");
			}
	
			sql = "insert into bago_national_bank.transactions (transaction_type, amount, old_balance, new_balance, account_id, customer_id) values (?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			
			
			preparedStatement.setString(1, "CREDIT");
			preparedStatement.setDouble(2, deposit);
			preparedStatement.setDouble(3, 0);
			preparedStatement.setDouble(4, deposit);
			preparedStatement.setInt(5, account_id);
			preparedStatement.setInt(6, customer.getCustomerId());
			
			int update = preparedStatement.executeUpdate();

			if (accountInsert == 1) {
				if (update == 1) {
					account = new Account(account_id, accountType, deposit, today, "pending", customer);
				} else {
					throw new BusinessException("Update transaction failed. Contact SYSADMIN! createNewAccount");
				}
			} else {
				throw new BusinessException("Customer insert failed. Contact SYSADMIN! createNewAccount");
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error. Contact SYSADMIN! createNewAccount");
		}
		
		return account;
	}
	
	@Override
	public Customer createNewCustomer(Pin pin) throws BusinessException {
		Username username = pin.getUsername();
		Customer customer = username.getCustomer();
		int customer_id = 0;
		int username_id = 0;

		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "insert into bago_national_bank.customer (first_name, last_name, gender, dob, address1, address2, city, state, zip5, zip4, phone1, phone2, email, join_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, customer.getFirstName());
			preparedStatement.setString(2, customer.getLastName());
			preparedStatement.setString(3, customer.getGender());
			preparedStatement.setDate(4, new java.sql.Date(customer.getDob().getTime()));
			preparedStatement.setString(5, customer.getAddress1());
			preparedStatement.setString(6, customer.getAddress2());
			preparedStatement.setString(7, customer.getCity());
			preparedStatement.setString(8, customer.getState());
			preparedStatement.setString(9, customer.getZip5());
			preparedStatement.setString(10, customer.getZip4());
			preparedStatement.setString(11, customer.getPhone1());
			preparedStatement.setString(12, customer.getPhone2());
			preparedStatement.setString(13, customer.getEmail());
			preparedStatement.setDate(14, new java.sql.Date(customer.getJoinDate().getTime()));
			
			int customerInsert = preparedStatement.executeUpdate();
			
			
			sql = "select max(customer_id) customer_id from bago_national_bank.customer";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				customer_id = resultSet.getInt("customer_id");
			} else {
				throw new BusinessException("Problem getting customer_id. createNewCustomer");
			}
			
			
			sql = "insert into bago_national_bank.username (username, customer_id) values (?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, username.getUsername());
			preparedStatement.setInt(2, customer_id);
			
			int usernameInsert = preparedStatement.executeUpdate();
		

			sql = "select max(username_id) username_id from bago_national_bank.username";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				username_id = resultSet.getInt("username_id");
			} else {
				throw new BusinessException("Problem getting username_id. createNewCustomer");
			}

			
			sql = "insert into bago_national_bank.pin (pin, username_id) values (?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, pin.getPin());
			preparedStatement.setInt(2, username_id);
			
			int pinInsert = preparedStatement.executeUpdate();

			
			if (customerInsert == 1) {
				if (usernameInsert == 1) {
					if (pinInsert == 1) {
						customer.setCustomerId(customer_id);
					} else {
						throw new BusinessException("Pin insert failed. Contact SYSADMIN! createNewCustomer");
					}
				} else {
					throw new BusinessException("Username insert failed. Contact SYSADMIN! createNewCustomer");
				}
			} else {
				throw new BusinessException("Customer insert failed. Contact SYSADMIN! createNewCustomer");
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error. Contact SYSADMIN! createNewCustomer");
		}
		
		return customer;
	}

	@Override
	public Employee employeeLogIn(Pin employeeCredentials) throws BusinessException {
		Employee employee = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select u.username, u.employee_id, p.pin, e.first_name, e.last_name "
					+ "from bago_national_bank.username u "
					+ "join bago_national_bank.pin p on u.username_id = p.username_id "
					+ "join bago_national_bank.employee e on u.employee_id = e.employee_id "
					+ "where u.username = ? and p.pin = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, employeeCredentials.getUsername().getUsername());
			preparedStatement.setString(2, employeeCredentials.getPin());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				employee = new Employee();
				employee.setEmployeeId(resultSet.getInt("employee_id"));
				employee.setFirstName(resultSet.getString("first_name"));
				employee.setLastName(resultSet.getString("last_name"));
			} else {
				throw new BusinessException("\nNo Employee found with Username: " + employeeCredentials.getUsername().getUsername()+ " and Pin: " + employeeCredentials.getPin());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error occurred. Contact Admin!!! employeeLogIn");
		}
					
		return employee;
	}

	@Override
	public Account getAccountByAccountId(Account account) throws BusinessException {
		Account retrievedAccount = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select account_id, account_type, balance, opened_date, closed_date, status, approved_by, customer_id " +
							"from bago_national_bank.account " +
							"where account_id = ? " +
							"order by account_id";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, account.getAccountId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				retrievedAccount = new Account();
				retrievedAccount.setAccountId(resultSet.getInt("account_id"));
				retrievedAccount.setType(resultSet.getString("account_type"));
				retrievedAccount.setBalance(resultSet.getFloat("balance"));
				retrievedAccount.setOpenedDate(resultSet.getDate("opened_date"));
				retrievedAccount.setClosedDate(resultSet.getDate("closed_date"));
				retrievedAccount.setStatus(resultSet.getString("status"));
				retrievedAccount.setApprovedBy(resultSet.getInt("approved_by"));
				
				Customer customer = getCustomerByAccountId(retrievedAccount);
				retrievedAccount.setCustomer(customer);
			} else {
				throw new BusinessException("No Accounts found with Account ID: "+resultSet.getInt("account_id"));  
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage()); //Take off this line when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getAccountByAccountId");
		}
		
		return retrievedAccount;
	}

	@Override
	public List<Account> getAccountsByCustomerId(Customer customer, String status) throws BusinessException {
		List<Account> accounts = new ArrayList<>();
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select account_id, account_type, balance, opened_date, closed_date, status, approved_by, customer_id " +
							"from bago_national_bank.account " +
							"where customer_id = ? " +
							"and status = ? " +
							"order by account_id";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, customer.getCustomerId());
			preparedStatement.setString(2, status);
			
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Account account = new Account();
				account.setAccountId(resultSet.getInt("account_id"));
				account.setType(resultSet.getString("account_type"));
				account.setBalance(resultSet.getFloat("balance"));
				account.setOpenedDate(resultSet.getDate("opened_date"));
				account.setClosedDate(resultSet.getDate("closed_date"));
				account.setStatus(resultSet.getString("status"));
				account.setApprovedBy(resultSet.getInt("approved_by"));
				
				account.setCustomer(customer);
				
				accounts.add(account);				
			}

			if (accounts.size() == 0) {
				throw new BusinessException("No Accounts found with Customer ID: " + customer.getCustomerId());  
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage()); //Take off this line when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getAccountsByCustomerId");
		}
		
		return accounts;
	}

	@Override
	public List<Account> getAllAccounts(String status) throws BusinessException {
		List<Account> accounts = new ArrayList<>();
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select account_id, account_type, balance, opened_date, closed_date, status, approved_by, customer_id " +
							"from bago_national_bank.account " +
							"where status = ? " +
							"order by account_id";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, status);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Account account = new Account();
				account.setAccountId(resultSet.getInt("account_id"));
				account.setType(resultSet.getString("account_type"));
				account.setBalance(resultSet.getFloat("balance"));
				account.setOpenedDate(resultSet.getDate("opened_date"));
				account.setClosedDate(resultSet.getDate("closed_date"));
				account.setStatus(resultSet.getString("status"));
				account.setApprovedBy(resultSet.getInt("approved_by"));
				
				Customer customer = getCustomerByAccountId(account);
				account.setCustomer(customer);
				
				accounts.add(account);				
			}
			
			if (accounts.size() == 0) {
				throw new BusinessException("No Accounts found with Account ID: " + resultSet.getInt("account_id"));  
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage()); //Take off this line when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getAccountsByCustomerId");
		}

		return accounts;
	}


	@Override
	public List<Transaction> getAllTransactions() throws BusinessException {
		List<Transaction> transactionsList = new ArrayList<>();
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select t.transaction_id, t.transaction_type, t.amount, t.old_balance, t.new_balance, t.transaction_date, t.account_id, t.customer_id, t.status, t.linked_transaction_id "
					+ "from bago_national_bank.transactions t join  bago_national_bank.account a on t.account_id = a.account_id where t.status = ? order by t.customer_id, t.account_id, t.transaction_date, t.transaction_id";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "active");
			
			ResultSet resultSet = preparedStatement.executeQuery();
			Account retrievedAccount = null;
			Customer retrievedCustomer = null;
			
			while (resultSet.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(resultSet.getInt("transaction_id"));
				transaction.setTransactionType(resultSet.getString("transaction_type"));
				transaction.setAmount(resultSet.getDouble("amount"));
				transaction.setOldBalance(resultSet.getDouble("old_balance"));
				transaction.setNewBalance(resultSet.getDouble("new_balance"));
				transaction.setTransactionDate(resultSet.getDate("transaction_date"));
				transaction.setStatus(resultSet.getString("status"));
				transaction.setLinkedTransactionId(resultSet.getInt("linked_transaction_id"));

				retrievedAccount = new Account(resultSet.getInt("account_id"));
				transaction.setAccount(retrievedAccount);
				retrievedCustomer = new Customer(resultSet.getInt("customer_id"));
				transaction.setCustomer(retrievedCustomer);
				transactionsList.add(transaction);
			}
			
			if (transactionsList.size() == 0) {
				throw new BusinessException("No Transactions found in the DB");
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error. Contact SYSADMIN! getAllTransactionss");
		}
		return transactionsList;
	}

	@Override
	public Account getBalance(Account account) throws BusinessException {
		//Account account = new Account();
		this.account = new Account();  //check!!
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select a.balance from bago_national_bank.account a where a.account_id = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, account.getAccountId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				this.account.setBalance(resultSet.getFloat("balance"));  
			} else {
				throw new BusinessException("\nNo Account with Account Id: "+account.getAccountId()+ " found.");
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error occurred. Contact Admin!!! getBalance");
		}
		
		return this.account;
	}

	@Override
	public Customer getCustomerByAccountId(Account account) throws BusinessException {
		Customer customer = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select c.customer_id, c.first_name, c.last_name, c.gender, c.dob, c.address1, c.address2, c.city, c.state, c.zip5, c.zip4, c.phone1, c.phone2, c.email "
					+ "from bago_national_bank.customer c "
					+ "join bago_national_bank.account a on c.customer_id = a.customer_id "
					+ "where a.account_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, account.getAccountId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				customer = new Customer();
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
				customer.setGender(resultSet.getString("gender"));
				customer.setDob(resultSet.getDate("dob"));
				customer.setAddress1(resultSet.getString("address1"));
				customer.setAddress2(resultSet.getString("address2"));
				customer.setCity(resultSet.getString("city"));
				customer.setState(resultSet.getString("state"));
				customer.setZip5(resultSet.getString("zip5"));
				customer.setZip4(resultSet.getString("zip4"));
				customer.setPhone1(resultSet.getString("phone1"));
				customer.setPhone2(resultSet.getString("phone2"));
				customer.setEmail(resultSet.getString("email"));
			} else {
				throw new BusinessException("\nNo Customer found with Account ID: "+account.getAccountId());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());  //remove when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getCustomersByAccountId");
		}
		return customer;
	}

	@Override
	public Customer getCustomerByCustomerId(Customer customer) throws BusinessException {
		Customer retrievedCustomer = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select c.customer_id, c.first_name, c.last_name, c.gender, c.dob, c.address1, c.address2, c.city, c.state, c.zip5, c.zip4, c.phone1, c.phone2, c.email "
						+ "from bago_national_bank.customer c "
						+ "where c.customer_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, customer.getCustomerId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				retrievedCustomer = new Customer();
				retrievedCustomer.setCustomerId(resultSet.getInt("customer_id"));
				retrievedCustomer.setFirstName(resultSet.getString("first_name"));
				retrievedCustomer.setLastName(resultSet.getString("last_name"));
				retrievedCustomer.setGender(resultSet.getString("gender"));
				retrievedCustomer.setDob(resultSet.getDate("dob"));
				retrievedCustomer.setAddress1(resultSet.getString("address1"));
				retrievedCustomer.setAddress2(resultSet.getString("address2"));
				retrievedCustomer.setCity(resultSet.getString("city"));
				retrievedCustomer.setState(resultSet.getString("state"));
				retrievedCustomer.setZip5(resultSet.getString("zip5"));
				retrievedCustomer.setZip4(resultSet.getString("zip4"));
				retrievedCustomer.setPhone1(resultSet.getString("phone1"));
				retrievedCustomer.setPhone2(resultSet.getString("phone2"));
				retrievedCustomer.setEmail(resultSet.getString("email"));
			} else {
				throw new BusinessException("\nNo Customer found with Customer ID: " + customer.getCustomerId());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());  //remove when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getCustomerByCustomerId");
		}
		return retrievedCustomer;
	}

	@Override
	public List<Customer> getCustomersByCustomerName(String firstName, String lastName) throws BusinessException {
		List<Customer> customers = new ArrayList<>();
		Customer customer = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select c.customer_id, c.first_name, c.last_name, c.gender, c.dob, c.address1, c.address2, c.city, c.state, c.zip5, c.zip4, c.phone1, c.phone2, c.email "
						+ "from bago_national_bank.customer c "
						+ "where upper(c.first_name) = ? "
						+ "and upper(c.last_name) = ? ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, firstName.toUpperCase());
			preparedStatement.setString(2, lastName.toUpperCase());

			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				customer = new Customer();
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
				customer.setGender(resultSet.getString("gender"));
				customer.setDob(resultSet.getDate("dob"));
				customer.setAddress1(resultSet.getString("address1"));
				customer.setAddress2(resultSet.getString("address2"));
				customer.setCity(resultSet.getString("city"));
				customer.setState(resultSet.getString("state"));
				customer.setZip5(resultSet.getString("zip5"));
				customer.setZip4(resultSet.getString("zip4"));
				customer.setPhone1(resultSet.getString("phone1"));
				customer.setPhone2(resultSet.getString("phone2"));
				customer.setEmail(resultSet.getString("email"));
				customers.add(customer);
			} 
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());  //remove when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getCustomerByCustomerName");
		}
		return customers;
		
	}

	@Override
	public Customer getCustomerByTransactionId(Transaction transaction) throws BusinessException {
		Customer customer = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select c.customer_id, c.first_name, c.last_name, c.gender, c.dob, c.address1, c.address2, c.city, c.state, c.zip5, c.zip4, c.phone1, c.phone2, c.email "
					+ "from bago_national_bank.customer c "
					+ "join bago_national_bank.transactions t on c.customer_id = t.customer_id "
					+ "where t.transaction_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, transaction.getTransactionId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				customer = new Customer();
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
				customer.setGender(resultSet.getString("gender"));
				customer.setDob(resultSet.getDate("dob"));
				customer.setAddress1(resultSet.getString("address1"));
				customer.setAddress2(resultSet.getString("address2"));
				customer.setCity(resultSet.getString("city"));
				customer.setState(resultSet.getString("state"));
				customer.setZip5(resultSet.getString("zip5"));
				customer.setZip4(resultSet.getString("zip4"));
				customer.setPhone1(resultSet.getString("phone1"));
				customer.setPhone2(resultSet.getString("phone2"));
				customer.setEmail(resultSet.getString("email"));
			} else {
				throw new BusinessException("\nNo Customer found with Transaction ID: " + transaction.getTransactionId());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());  //remove when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getCustomerByTransactionId");
		}
		return customer;
	}

	@Override
	public List<Transaction> getTransactionsByAccountId(Account account, String status) throws BusinessException {
		List<Transaction> transactionsList = new ArrayList<>();
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select transaction_id, transaction_type, amount, old_balance, new_balance, transaction_date, account_id, customer_id, status, linked_transaction_id "
					+ "from bago_national_bank.transactions where account_id = ? and status = ? order by transaction_date, transaction_id";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, account.getAccountId());
			preparedStatement.setString(2, status);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			Account retrievedAccount = null;
			Customer retrievedCustomer = null;
			
			while (resultSet.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(resultSet.getInt("transaction_id"));
				transaction.setTransactionType(resultSet.getString("transaction_type"));
				transaction.setAmount(resultSet.getDouble("amount"));
				transaction.setOldBalance(resultSet.getDouble("old_balance"));
				transaction.setNewBalance(resultSet.getDouble("new_balance"));
				transaction.setTransactionDate(resultSet.getDate("transaction_date"));
				transaction.setStatus(resultSet.getString("status"));
				transaction.setLinkedTransactionId(resultSet.getInt("linked_transaction_id"));
				
				retrievedAccount = new Account(resultSet.getInt("account_id"));
				transaction.setAccount(retrievedAccount);
				retrievedCustomer = new Customer(resultSet.getInt("customer_id"));
				transaction.setCustomer(retrievedCustomer);
				transactionsList.add(transaction);
			} 
			
			if (transactionsList.size() == 0) {
				throw new BusinessException("\nNo Transactions associated with Account ID: " +account.getAccountId());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());  //remove when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getTransactionsByAccountId");
		}
		
		return transactionsList;
	}

	@Override
	public List<Transaction> getTransactionsByCustomerId(Customer customer, String status) throws BusinessException {
		List<Transaction> transactionsList = new ArrayList<>();
		Transaction transaction = null; 
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			//String sql = "select transaction_id, transaction_type, amount, old_balance, new_balance, transaction_date, account_id, customer_id "
				//	+ "from bago_national_bank.transactions where customer_id = ? order by transaction_date";
			
			String sql = "select t.transaction_id, t.transaction_type, t.amount, t.old_balance, t.new_balance, t.transaction_date, t.account_id, t.customer_id, t.status, t.linked_transaction_id, a.account_type "
				+ "from bago_national_bank.transactions t "
				+ "join bago_national_bank.account a on t.account_id = a.account_id "
				+ "where t.customer_id = ? and t.status = ? order by t.account_id, t.transaction_date, t.transaction_id";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, customer.getCustomerId());
			preparedStatement.setString(2, status);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			Account retrievedAccount = null;
			Customer retrievedCustomer = null;
			
			while (resultSet.next()) {
				transaction = new Transaction();
				transaction.setTransactionId(resultSet.getInt("transaction_id"));
				transaction.setTransactionType(resultSet.getString("transaction_type"));
				transaction.setAmount(resultSet.getDouble("amount"));
				transaction.setOldBalance(resultSet.getDouble("old_balance"));
				transaction.setNewBalance(resultSet.getDouble("new_balance"));
				transaction.setTransactionDate(resultSet.getDate("transaction_date"));
				transaction.setStatus(resultSet.getString("status"));
				transaction.setLinkedTransactionId(resultSet.getInt("linked_transaction_id"));
				
				retrievedAccount = new Account(resultSet.getInt("account_id"));
				retrievedAccount.setType(resultSet.getString("account_type"));
				transaction.setAccount(retrievedAccount);
				retrievedCustomer = new Customer(resultSet.getInt("customer_id"));
				transaction.setCustomer(retrievedCustomer);
				transactionsList.add(transaction);
			} 
			if (transactionsList.size() == 0) {
				throw new BusinessException("\nNo Transactions associated with Customer ID: " +customer.getCustomerId());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());  //remove when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getTransactionsByCustomerId");
		}
		
		return transactionsList;
	}

	@Override
	public boolean isAccount(Account account) throws BusinessException {
		String status = "active";
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select distinct account_id from bago_national_bank.account where account_id = ? and status = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, account.getAccountId());
			preparedStatement.setString(2, status);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error. Contact SYSADMIN! isAccount");
		}
	}
	
	@Override
	public boolean isUsername(Username username) throws BusinessException {
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select username from bago_national_bank.username where username = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, username.getUsername());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		}	 catch (ClassNotFoundException | SQLException e) {
				 throw new BusinessException("Internal error. Contact SYSADMIN! isUsername");
			}
	}
	
	

	@Override
	public Customer logIn(Pin customerCredentials) throws BusinessException {
		Customer customer = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select u.username, u.customer_id, p.pin, c.first_name, c.last_name "
					+ "from bago_national_bank.username u "
					+ "join bago_national_bank.pin p on u.username_id = p.username_id "
					+ "join bago_national_bank.customer c on u.customer_id = c.customer_id "
					+ "where u.username = ? and p.pin = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, customerCredentials.getUsername().getUsername());
			preparedStatement.setString(2, customerCredentials.getPin());
			
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				customer = new Customer();
				customer.setCustomerId(resultSet.getInt("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
			} else {
				throw new BusinessException("\nNo Customer found with Username: "+customerCredentials.getUsername().getUsername()+ " and Pin: "+customerCredentials.getPin());
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error occurred. Contact Admin!!! logIn");
		}
					
		return customer;
	}

	@Override
	public boolean setLinkedTransactionId (Transaction transaction, int linkedTransactionId) throws BusinessException {
		boolean success = false;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "update bago_national_bank.transactions set linked_transaction_id = ? where transaction_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, linkedTransactionId);
			preparedStatement.setInt(2, transaction.getTransactionId());
				
			int transactionUpdate = preparedStatement.executeUpdate();
			
			if (transactionUpdate == 1) {
				success = true;
			} else {
				throw new BusinessException("Update transactions failed. Contact SYSADMIN! setLinkedTransactionId");
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error. Contact SYSADMIN! setLinkedTransactionId");
		}
		
		return success;
	}
	
	@Override
	public Transaction updateBalance(Account account, double amount, String status) throws BusinessException {
		int update = 0;
		int transactionId = 0;
		String transactionType = null;
		Transaction transaction = null;
		Account oldBalance = getBalance(account);
		double newBalance = oldBalance.getBalance() + amount;
		
		if (amount > 0) {
			transactionType = "credit";
		} else {
			transactionType = "debit";
		}
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "insert into bago_national_bank.transactions (transaction_type, amount, old_balance, new_balance, account_id, customer_id, status) values (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, transactionType);
			preparedStatement.setDouble(2, amount);
			preparedStatement.setDouble(3, oldBalance.getBalance());
			preparedStatement.setDouble(4, newBalance);
			preparedStatement.setInt(5, account.getAccountId());
			preparedStatement.setInt(6, account.getCustomer().getCustomerId());
			preparedStatement.setString(7, status);
			
			update = preparedStatement.executeUpdate();
			
			
			sql = "select max(transaction_id) transaction_id from bago_national_bank.transactions";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				transactionId = resultSet.getInt("transaction_id");
			} else {
				throw new BusinessException("\nCan't find max transaction_id. updateBalance");
			}

			
			sql = "select transaction_id, transaction_type, amount, old_balance, new_balance, transaction_date, account_id, customer_id, status from bago_national_bank.transactions where transaction_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, transactionId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				transaction = new Transaction();
				transaction.setTransactionId(transactionId);
				transaction.setTransactionType(resultSet.getString("transaction_type"));
				transaction.setAmount(resultSet.getDouble("amount"));
				transaction.setOldBalance(resultSet.getDouble("old_balance"));
				transaction.setNewBalance(resultSet.getDouble("new_balance"));
				transaction.setTransactionDate(resultSet.getDate("transaction_date"));
				transaction.setAccount(account);
				transaction.setCustomer(getCustomerByAccountId(account));
				transaction.setStatus(resultSet.getString("status"));
			} else {
				throw new BusinessException("\nCan't find max transaction_id. updateBalance");
			}


			if (status.equals("confirmed")) {
				sql = "update bago_national_bank.account set balance = balance + ? where account_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				
				preparedStatement.setDouble(1, amount);
				preparedStatement.setInt(2, account.getAccountId());
				
				update = preparedStatement.executeUpdate();
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e.getMessage());
			throw new BusinessException("Internal error. Contact SYSADMIN! updateBalance");
		}
		
		return transaction;
	}

	

	

	
}
