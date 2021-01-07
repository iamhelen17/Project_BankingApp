package com.bagonationalbank.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bagonationalbank.app.dao.BankingOperationsDAO;
import com.bagonationalbank.app.dao.dbutil.PostresqlConnection;
import com.bagonationalbank.app.exception.BusinessException;
import com.bagonationalbank.app.model.Account;
import com.bagonationalbank.app.model.Customer;
import com.bagonationalbank.app.model.Pin;
import com.bagonationalbank.app.model.Transactions;

public class BankingOperationsDAOImpl implements BankingOperationsDAO {

private static Logger log = Logger.getLogger(BankingOperationsDAOImpl.class);
private Account account;
	
	
	@Override
	public String createNewAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer logIn(Pin customerCredentials) throws BusinessException {
		Customer customer = null;
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select u.username, u.customer_id, p.pin, c.first_name, c.last_name "
					+ "from bago_national_bank.username u "
					+ "join bago_national_bank.pin p on u.username_id = p.username_id "
					+ "join bago_national_bank.customer c on u.customer_id = c.customer_id where u.username = ? and p.pin = ?";
			
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
	public int updateBalance(Account account, double amount) throws BusinessException {
		int update = 0;
		String transactionType = null;
		Account oldBalance = getBalance(account);
		double newBalance = oldBalance.getBalance() + amount;
		
		if (amount > 0) {
			transactionType = "CREDIT";
		} else {
			transactionType = "DEBIT";
		}
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "update bago_national_bank.account set balance = balance + ? where account_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setDouble(1, amount);
			preparedStatement.setInt(2, account.getAccountId());
			
			update = preparedStatement.executeUpdate();
			
			sql = "insert into bago_national_bank.transactions (transaction_type, amount, old_balance, new_balance, account_id, customer_id) values (?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			
			
			preparedStatement.setString(1, transactionType);
			preparedStatement.setDouble(2, amount);
			preparedStatement.setDouble(3, oldBalance.getBalance());
			preparedStatement.setDouble(4, newBalance);
			preparedStatement.setInt(5, account.getAccountId());
			preparedStatement.setInt(6, account.getCustomer().getCustomerId());
			
			update = preparedStatement.executeUpdate();
			
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error. Contact SYSADMIN! updateBalance");
		}
		
		return update;
	}

	
	@Override
	public boolean isValidAccount(Account account) throws BusinessException {
		try (Connection connection = PostresqlConnection.getConnection()) {
			
			String sql = "select distinct account_id from bago_national_bank.account where account_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setInt(1, account.getAccountId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException("Internal error. Contact SYSADMIN! isValidAccount");
		}
	}
	
	

	@Override
	public List<Customer> getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> getAllAccounts(Customer customer) throws BusinessException {
		//log.info("Customer ID: " + customer.getCustomerId());
		List<Account> accountsList = new ArrayList<>();
		
		try (Connection connection = PostresqlConnection.getConnection()) {
			String sql = "select account_id, account_type, balance from bago_national_bank.account where customer_id = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, customer.getCustomerId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Account account = new Account();
				account.setAccountId(resultSet.getInt("account_id"));
				account.setType(resultSet.getString("account_type"));
				account.setBalance(resultSet.getFloat("balance"));
				accountsList.add(account);				
			}
			
			if (accountsList.size() == 0) {
				throw new BusinessException("No Accounts found with Account ID: "+resultSet.getInt("account_id"));  
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			log.info(e); //Take off this line when app is live
			throw new BusinessException("Internal error. Contact SYSADMIN! getAllAccounts");
		}
		
		return accountsList;
	}

	@Override
	public List<Transactions> getAllTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
